package carrot.market.config;

import carrot.market.common.security.filter.CustomAuthorizationFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {

    @Value("${security.bcrypt.strength}")
    private int strength;

    private final CustomAuthorizationFilter customAuthorizationFilter;
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
                            authorizeRequests
//                                .requestMatchers(PathRequest.toH2Console()).permitAll()
                                    .requestMatchers("/", "/api/v1/auth/**", "/api/v1/search/**", "/file/**").permitAll()
                                    .requestMatchers(HttpMethod.GET, "/api/v1/board/**", "/api/v1/user/*").permitAll()
                                    .anyRequest().authenticated()
            )
            .exceptionHandling((exceptionConfig) ->
                    exceptionConfig.authenticationEntryPoint(new FailedAuthenticationEntryPoint()))
            .addFilterBefore(customAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(strength);
    }
}

class FailedAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);   // 403 권한없음
        response.getWriter().write("{ \"code\": \"NP\", \"message\": \"Authorization Failed\" }");
    }
}
