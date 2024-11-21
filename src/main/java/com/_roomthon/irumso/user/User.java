package com._roomthon.irumso.user;

import com._roomthon.irumso.refreshToken.RefreshToken;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})  // Hibernate proxy 필드 무시
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "nickname", nullable = false, unique = true)
    private String nickname; // 이메일 제거, 닉네임만 사용

    @JsonBackReference  // 순환 참조 방지를 위해 RefreshToken을 무시
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "refresh_token_id")
    private RefreshToken refreshToken;

    @Builder
    public User(String nickname) {
        this.nickname = nickname;
    }

    // UserDetails 인터페이스 메소드 구현
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 기본 권한을 ROLE_USER로 설정
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return null; // 소셜 로그인에서는 비밀번호가 필요하지 않음
    }

    @Override
    public String getUsername() {
        return nickname; // 닉네임을 username으로 사용
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
