<p align="center">중고거래 플랫폼 API 서버 토이 프로젝트입니다.</p>

### ✅ <span>당근마켓</span>, <span>번개장터</span> 같은 중고거래 플랫폼은 어떤 방식으로 구성되어 있을까?

* 어떻게 수 많은 사용자들에게 서비스를 제공하고있을까?
* 대량의 요청에 서버는 어떤 방식으로 대응할까?
* 장애 발생 시 서버 로그를 통해 빠르게 처리하는 방법은 없을까?
  <br>
  <br>
이러한 궁금증으로 인해 간단히 구현해보고 접해보는 프로젝트를 진행하게 되었습니다.

### ✅ 사용 기술 및 개발 환경

Java, Spring Boot, Spring AOP, IntelliJ, Gradle, JPA, Querydsl, MySQL, Redis, Nginx, Jenkins, Docker

### ✅ 프로젝트의 어떤 부분에 중점을 두었는지?

* 대량의 요청에 있어 어떤 방식을 도입하고 아키텍쳐를 설계하여 조금이라도 부하를 줄일 수 있을지?
* 장애 발생 시 개발한 로그 추적 기능이 성능에 영향을 미치지 않고 가독성 있게 구현하여 대처하기 쉬운지?
* 핵심 기능과 부가기능(cross-cutting concerns)을 깔끔히 분리하였는지?
* 최소한의 객체만으로 어떻게 객체지향적으로 설계할지?
* 프론트엔드 부분은 생략하고 벡엔드에 초점을 맞춰 백엔드 개발에 주력하였습니다.

### ✅ 프로젝트를 진행하며 고민한 Technical Issue

* [사용자 증가에 따른 서버 확장법과 부하 분산을 위한 MySQL Replication 구성](https://fluorescent-sceptre-6b9.notion.site/626c4d1235184c1c83913a6cca1ad819)
* [부하를 줄이기위한 캐싱 적용과 ngrinder 성능 측정](https://fluorescent-sceptre-6b9.notion.site/e5e730e11a4e4c40a1f1b2569c950ac4)
* [분산 처리 환경에서의 세션 불일치 문제와 토큰 기반 인증 관리](https://fluorescent-sceptre-6b9.notion.site/b0ca0b7162b748ebb5116f3193a28a27)
* [Redis 성능 향상을 위한 Redis 세션 저장소와 캐시 저장소의 분리]
* [빠른 장애 대처를 위한 Log 추적기능과 프록시 적용]

### ✅ 사용 아키텍쳐
* [Redis 아키텍쳐](https://fluorescent-sceptre-6b9.notion.site/Redis-df37c069a91f4aff90f41db73b575b9b)
* [Mysql 아키텍쳐](https://fluorescent-sceptre-6b9.notion.site/Mysql-42d407fb10674feabaf60065163b49de)

* 그 밖의 수행 기록 : [notion link Click!](https://fluorescent-sceptre-6b9.notion.site/b0cf9a22d63541ea930d7b20b51d2b57)

### ✅ 프로젝트 전체 구성도
![image](https://github.com/user-attachments/assets/6955635b-d00a-44b5-aae7-6fe8a93cd2c4)

