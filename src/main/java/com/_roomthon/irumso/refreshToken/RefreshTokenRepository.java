package com._roomthon.irumso.refreshToken;

import com._roomthon.irumso.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUser(User user);

    Optional<RefreshToken> findRefreshTokenByRefreshToken(String accessToken);
}
