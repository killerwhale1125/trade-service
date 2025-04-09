//package carrot.market.util;
//
//import carrot.market.member.entity.Member;
//import carrot.market.member.repository.MemberJpaRepository;
//import carrot.market.post.dto.PostRequestDto;
//import carrot.market.post.entity.Category;
//import carrot.market.post.entity.Post;
//import carrot.market.post.repository.CategoryRepository;
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.util.Arrays;
//import java.util.List;
//
////@Component
//public class ProductInitializer {
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    private MemberJpaRepository memberJpaRepository;
//
//    @Autowired
//    private CategoryRepository categoryRepository;
//
//    @PostConstruct
//    public void init() {
//        // State, City, Town 값을 리스트로 정의
//        List<String> states = Arrays.asList("seoul", "busan", "incheon", "daegu", "daejeon", "gwangju", "ulsan", "sejong", "gyeonggi", "gangwon");
//        List<String> cities = Arrays.asList("gangnam", "jongno", "mapo", "songpa", "yangcheon", "buk-gu", "nam-gu", "dong-gu", "seo-gu", "suyeong");
//        List<String> towns = Arrays.asList("yeoksam", "nonhyeon", "sinsa", "apgujeong", "banpo", "seocho", "guro", "gasan", "gasan-dong", "myeongdong");
//
//        /**
//         * 회원가입
//         */
//        for(int j=1; j<=500000; j++) {
//            // 인덱스를 리스트 크기로 나눈 나머지 값으로 순환
//            String state = states.get((j - 1) % states.size());
//            String city = cities.get((j - 1) % cities.size());
//            String town = towns.get((j - 1) % towns.size());
//
//            MemberDto memberDto = new MemberDto("User" + j + "@gmail.com", "Woals12345!", "nickname" + j, state, city, town);
//            Member member = MemberDto.toEntity(memberDto, passwordEncoder);
//            memberJpaRepository.save(member);
//        }
//
//        /**
//         * 카테고리 생성
//         */
//        categoryRepository.save(Category.builder().categoryName("perfume").build());
//        categoryRepository.save(Category.builder().categoryName("food").build());
//        categoryRepository.save(Category.builder().categoryName("fashion").build());
//        categoryRepository.save(Category.builder().categoryName("electronic").build());
//        categoryRepository.save(Category.builder().categoryName("beauty").build());
//        categoryRepository.save(Category.builder().categoryName("sport").build());
//        categoryRepository.save(Category.builder().categoryName("kid").build());
//        categoryRepository.save(Category.builder().categoryName("book").build());
//        categoryRepository.save(Category.builder().categoryName("animal").build());
//        categoryRepository.save(Category.builder().categoryName("health").build());
//
//        /**
//         * 게시물 생성
//         */
//        for(int i=1; i<=500000; i++) {
//            Member member = memberJpaRepository.findById((long) i).get();
//            for(int j=0; j<2; j++) {
//                Post post = new PostRequestDto().toEntity(member, "title_" + i + "_" + j, "content" + i + "_" + j);
//                long categoryNumber = (i / 100000 + 1);
//                post.setCategory(categoryRepository.findById(categoryNumber).get());
//            }
//        }
//
//    }
//}
