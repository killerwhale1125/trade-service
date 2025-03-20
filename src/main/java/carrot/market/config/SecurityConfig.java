package carrot.market.config;

import carrot.market.util.jwt.JwtAuthenticationFilter;
import carrot.market.util.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {
    @Value("${security.bcrypt.strength}")
    private int strength;

    private final JwtTokenProvider jwtTokenProvider;
    private final CorsConfig corsConfig;

    /**
     * http 보안 설정
     * cors ->
     * csrf -> 해커가 사용자 의지 없이 공격하도록 만듬 (로그인 시 사용자 모르게 해커가 의도한 url 요청하여 정보를 가져오는 등 공격 )
     */
    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
            .cors(corsCustomizer -> corsCustomizer.configurationSource(corsConfig))
            .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .httpBasic(HttpBasicConfigurer::disable)    // ID와 비밀번호를 직접 입력하여 서버에 인증하는 방식 비활성화
            .csrf((csrfConfig) -> csrfConfig.disable())
            .authorizeHttpRequests((authorizeRequests) ->
                            authorizeRequests.requestMatchers("/**").permitAll()
                            .anyRequest().authenticated())
             .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        // JWT 필터 등록 -> JWT 인증으로 인해 Authentication 객체가 인증된 객체인지 아닌지 컨트롤러 호출 전 판별됨
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(strength);
    }
}
