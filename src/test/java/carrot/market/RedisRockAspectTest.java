package carrot.market;

import carrot.market.util.jwt.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class RedisRockAspectTest {
    @Autowired
    private JwtTokenProvider tokenService;

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;


//    @Test
//    public void testRedisLockAspectForTokenCreation() {
//        String username = "aop1@gmail.com"; // 동일한 사용자 계정
//        String password = "Woals12345!"; // 비밀번호
//
//        /**
//         * 1. username + password 를 기반으로 Authentication 객체 생성
//         * 이때 authentication 은 인증 여부를 확인하는 authenticated 값이 false
//         */
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
//
//        /**
//         * 2. 실제 검증. authenticate() 메서드를 통해 요청된 Member 에 대한 검증 진행
//         */
//        AuthenticationManager authenticationManager = authenticationManagerBuilder.getObject();
//
//        /**
//         * Manager에게 인증되지 않은 authenticationToken 객체를 넘겨줘서 판별
//         * authenticate 메서드가 실행될 때 MemberDetailsService 에서 만든 loadUserByUsername 메서드 실행
//         * Manager는 Authentication 객체를 인증할 적절한 Provider를 찾아야 함
//         * 따라서 loadUserByUsername을 호출하는 것은 AuthenticationProvider에 의해 호출됨
//         */
//        Authentication authentication = authenticationManager.authenticate(authenticationToken);
//
//        ExecutorService executor = Executors.newFixedThreadPool(5); // 5개의 스레드 풀 생성
//
//        // 여러 스레드에서 refresh token 생성 시도
//        for (int i = 0; i < 5; i++) {
//            executor.submit(() -> {
////                String token = tokenService.createRefreshToken(authentication);
//                String token = null;
//                System.out.println("Refresh Token: " + token);
//            });
//        }
//
//        try {
//            // 모든 작업이 완료될 때까지 대기
//            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
//                executor.shutdownNow(); // 60초 후에도 작업이 완료되지 않으면 강제 종료
//            }
//        } catch (InterruptedException e) {
//            executor.shutdownNow(); // 인터럽트 발생 시 강제 종료
//        }
//    }
}
