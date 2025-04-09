package carrot.market;

import carrot.market.member.service.MemberServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class LoginTest {

    @Autowired
    private MemberServiceImpl memberServiceImpl;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 로그인 요청을 시뮬레이션할 메서드
//    private void attemptLogin(String username, String password) {
//        try {
//            memberServiceImpl.signIn(username, password);
//            System.out.println(username + " 로그인 성공");
//        } catch (BaseException e) {
//            System.out.println(username + " 로그인 실패: " + e.getMessage());
//        }
//    }

//    @Test
//    public void testConcurrentLogin() {
//        String username = "aop1@gmail.com"; // 동일한 사용자 계정
//        String password = "Woals12345!"; // 비밀번호
//
//        ExecutorService executor = Executors.newFixedThreadPool(5); // 5개의 스레드 풀 생성
//
//        // 5명의 사용자 로그인 시도
//        for (int i = 0; i < 5; i++) {
//            executor.submit(() -> attemptLogin(username, password));
//        }
//
//        executor.shutdown(); // 더 이상 작업을 추가할 수 없음
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

