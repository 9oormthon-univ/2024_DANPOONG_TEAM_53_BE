package com._roomthon.irumso.config.oauth;

import com._roomthon.irumso.config.cookie.CookieUtil;
import com._roomthon.irumso.config.jwt.TokenProvider;
import com._roomthon.irumso.refreshToken.RefreshToken;
import com._roomthon.irumso.refreshToken.RefreshTokenRepository;
import com._roomthon.irumso.user.User;
import com._roomthon.irumso.user.UserRepository;
import com._roomthon.irumso.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofDays(1);
    public static final String HOME_REDIRECT_PATH = "http://localhost:3000/home";
    public static final String SIGNUP_REDIRECT_PATH = "http://localhost:3000/signin/userdata";

    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;
    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String nickname = getKakaoNickname(oAuth2User);

        User user = saveOrUpdateUser(nickname);

        String refreshToken = tokenProvider.generateToken(user, REFRESH_TOKEN_DURATION);
        saveRefreshToken(user, refreshToken);
        addRefreshTokenToCookie(request, response, refreshToken);

        String accessToken = tokenProvider.generateToken(user, ACCESS_TOKEN_DURATION);
        System.out.println("Generated Access Token: " + accessToken);

        String targetUrl = user.getNickname() == null
                ? getTargetUrl(accessToken, false)
                : getTargetUrl(accessToken, true);

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private String getKakaoNickname(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        return (String) profile.get("nickname");
    }

    @Transactional
    private User saveOrUpdateUser(String nickname) {
        return userRepository.findByNickname(nickname)
                .orElseGet(() -> userRepository.save(new User(nickname)));
    }

    @Transactional
    private void saveRefreshToken(User user, String newRefreshToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByUser(user)
                .map(entity -> entity.update(newRefreshToken))
                .orElse(new RefreshToken(user, newRefreshToken));

        refreshTokenRepository.save(refreshToken);
        user.setRefreshToken(refreshToken);
        userRepository.save(user);
    }

    private void addRefreshTokenToCookie(HttpServletRequest request, HttpServletResponse response, String refreshToken) {
        int cookieMaxAge = (int) REFRESH_TOKEN_DURATION.toSeconds();
        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN_COOKIE_NAME);
        CookieUtil.addCookie(response, REFRESH_TOKEN_COOKIE_NAME, refreshToken, cookieMaxAge);
    }

    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private String getTargetUrl(String token, boolean nicknameExist) {
        return nicknameExist
                ? UriComponentsBuilder.fromUriString(HOME_REDIRECT_PATH)
                .queryParam("token", token)
                .build()
                .toUriString()
                : UriComponentsBuilder.fromUriString(SIGNUP_REDIRECT_PATH)
                .queryParam("token", token)
                .build()
                .toUriString();
    }
}
