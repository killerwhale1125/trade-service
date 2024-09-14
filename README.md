<p align="center">중고거래 플랫폼 API 서버 토이 프로젝트입니다.</p>

### ✅ <span>당근마켓</span>, <span>번개장터</span> 같은 중고거래 플랫폼은 어떤 방식으로 구성되어 있을까?

* 어떻게 수 많은 사용자들에게 서비스를 제공하고있을까?
* 대량의 요청에 서버는 어떤 방식으로 대응할까?
* 요청마다 로그 출력 시 핵심기능과 분리하여 출력할 수 있을까?
  <br>
  <br>
이러한 궁금증으로 인해 간단히 구현해보고 접해보는 프로젝트를 진행하게 되었습니다.

### ✅ 사용 기술 및 개발 환경

Java, Spring Boot, Spring AOP, IntelliJ, Gradle, JPA, Querydsl, MySQL, Redis, Nginx, Jenkins, Docker, AWS EC2, RDS

### ✅ 프로젝트의 어떤 부분에 중점을 두었는지?

* 대량의 요청에 있어 어떤 방식을 도입하고 아키텍쳐를 설계하여 조금이라도 부하를 줄일 수 있을지?
* 요청 정보를 위한 log 출력에 있어 핵심 기능과 부가기능(cross-cutting concerns)을 잘 분리했는지?
* 최소한의 객체만으로 어떻게 객체지향적으로 설계할지?
* 프론트엔드 부분은 생략하고 벡엔드에 초점을 맞춰 백엔드 개발에 주력하였습니다.

### ✅ 프로젝트를 진행하며 고민한 Technical Issue

* [사용자 증가에 따른 서버 확장법과 부하 분산을 위한 MySQL Replication 구성](https://fluorescent-sceptre-6b9.notion.site/626c4d1235184c1c83913a6cca1ad819)
* [부하를 줄이기위한 캐싱 적용과 ngrinder 성능 측정](https://fluorescent-sceptre-6b9.notion.site/e5e730e11a4e4c40a1f1b2569c950ac4)
* [분산 처리 환경에서의 세션 불일치 문제와 토큰 기반 인증 관리](https://fluorescent-sceptre-6b9.notion.site/b0ca0b7162b748ebb5116f3193a28a27)
* [Spring AOP는 어떻게 Proxy를 생성하고 횡단 관심사를 분리할까?](https://fluorescent-sceptre-6b9.notion.site/Spring-AOP-Proxy-0e7cb619f60b49de8dcf01d0ad6a703d)

### ✅ 사용 아키텍쳐 
* [Redis 아키텍쳐](https://fluorescent-sceptre-6b9.notion.site/Redis-df37c069a91f4aff90f41db73b575b9b)
![image](https://github.com/user-attachments/assets/9a9b88a4-79de-47bb-b4b3-a72cdaf70108)
* [Mysql 아키텍쳐](https://fluorescent-sceptre-6b9.notion.site/Mysql-42d407fb10674feabaf60065163b49de)
![image](https://github.com/user-attachments/assets/77b6794c-970a-4d86-bef9-71846c21e9e2)

### ✅ REST API 명세서
* [AUTH](https://fluorescent-sceptre-6b9.notion.site/Auth-bc8158a621e54af8be50acb4ec5aaaad)
* [POST](https://fluorescent-sceptre-6b9.notion.site/Post-53b13fc67e2b42aeb22f85aad6749700)
* [POST SEARCH](https://fluorescent-sceptre-6b9.notion.site/PostSearch-a4f49b44930f4652a2d931ba508956e9)
* [USER](https://fluorescent-sceptre-6b9.notion.site/User-31bcde04f58d409e87151826cf19dcf0)
* [JWT EXCEPTION](https://fluorescent-sceptre-6b9.notion.site/JWT-Exception-4424734ce2f74dc3ab8059b9570ae841)

* 그 밖의 수행 기록 : [notion link Click!](https://fluorescent-sceptre-6b9.notion.site/b0cf9a22d63541ea930d7b20b51d2b57)

### ✅ 프로젝트 전체 구성도
![image](https://github.com/user-attachments/assets/6955635b-d00a-44b5-aae7-6fe8a93cd2c4)

