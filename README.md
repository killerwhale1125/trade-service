<p align="center">중고거래 플랫폼 API 서버 토이 프로젝트입니다.</p>

### ✅ 사용자 수가 많은 중고거래 플랫폼은 어떤 방식으로 구성되어 있으며 어떻게 안정적인 서비스를 제공할까?
- 빠른 데이터 조회
- 트래픽 분산 처리
- 장애 발생 시 신속한 대응을 위한 로그 추적
- 안정적인 트랜잭션 처리

이러한 궁금증으로 인하여 간단히 프로젝트를 진행하였습니다.
<br/>

### ✅ 프로젝트 전체 구성도
![image](https://github.com/user-attachments/assets/7990f15f-3978-4592-8f36-4d79329915d6)

### ✅ 기술 스택

Java, Spring Boot, Spring AOP, IntelliJ, Gradle, JPA, Querydsl, Redis, Nginx, Docker, AWS EC2, RDS MySQL

### ✅ 프로젝트 개선 사항 정리 블로그

* [불안정한 Redis 트랜잭션의 원자성과 AOP Proxy 해결책](https://killerwhale1125.github.io/posts/Redis-%ED%8A%B8%EB%9E%9C%EC%9E%AD%EC%85%98%EC%9D%98-%ED%95%9C%EA%B3%84%EB%A1%9C-%EC%9D%B8%ED%95%9C-%ED%95%B4%EA%B2%B0%EC%B1%85/)
* [Redis 캐싱을 활용한 게시물 조회 성능 증가와 로드밸런싱 활용](https://killerwhale1125.github.io/posts/%EC%A1%B0%EA%B1%B4%EB%B6%80-%EC%BA%90%EC%8B%B1-%EB%B0%8F-%EC%84%B1%EB%8A%A5-%ED%85%8C%EC%8A%A4%ED%8A%B8/)

### ✅ 프로젝트 개선 사항 및 트러블 슈팅

**1. 불안정한 Redis 트랜잭션에 AOP Porxy 도입하여 Atomic 보장**

**문제점**
- Redis 작업 이후 다른 작업의 예외로 인해 모든 데이터를 Rollback 시켜야할 때 Redis Rollback의 불가능
- Redis 데이터 일관성 문제

<br/>
**개선사항**

![image](https://github.com/user-attachments/assets/bd4d5817-2f6b-4e52-87ae-11a862714be2)

- AOP Proxy를 활용해 Redis 작업 전 / 후로 나뉘어 트랜잭션을 관리한다.
- AOP로 비즈니스 코드와 공통 관심사를 분리해 비즈니스 코드에 집중할 수 있으며, 코드의 간결함을 유지한다.

<br/>

**2. 1억건 규모 데이터 상황의 쿼리 튜닝 최적화 과정**

**문제점**

![image](https://private-user-images.githubusercontent.com/165141435/434194059-fecf632f-1170-4dd5-9774-b9b95e25239d.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3NDQ3ODc2MzksIm5iZiI6MTc0NDc4NzMzOSwicGF0aCI6Ii8xNjUxNDE0MzUvNDM0MTk0MDU5LWZlY2Y2MzJmLTExNzAtNGRkNS05Nzc0LWI5Yjk1ZTI1MjM5ZC5wbmc_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjUwNDE2JTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI1MDQxNlQwNzA4NTlaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT1mYzkwOGNiNzFlMjQ3YWQ1NTYzYWQ3NThlOTY1M2M0OGRkNzVhMzVhNDM0NDBjMWNkMTQxMTAwODFmZDRhZjlmJlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCJ9.ypk5IcJ-jA-XeqmQ_6UD_S3mmS0b48gYj3bQNJtASMA)
- 응답 지연(평균 10초 이상) 문제
- 인덱스 활용이 제대로 이루어지지 않아 Disk I/O 과부하 발생

**서브 쿼리 최적화 및 Vus 2000 부하테스트 결과**

![image]([https://github.com/killerwhale1125/trade-service/issues/2#issue-2998669420](https://private-user-images.githubusercontent.com/165141435/434194842-f16f2ae9-425b-497e-a467-012e8f07048c.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3NDQ3ODc0OTMsIm5iZiI6MTc0NDc4NzE5MywicGF0aCI6Ii8xNjUxNDE0MzUvNDM0MTk0ODQyLWYxNmYyYWU5LTQyNWItNDk3ZS1hNDY3LTAxMmU4ZjA3MDQ4Yy5wbmc_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjUwNDE2JTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI1MDQxNlQwNzA2MzNaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT1iNmFlMzFlZjE3YmRjYjhmMWNkY2NjY2UxM2I0NzRmZGQ2NDBmYTFhYmFhNTc1NmYwMzNkZTY0OGU5ZmRjMjQ2JlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCJ9.dLENsKS3rmNStf_wwiIE090p_YpYv2100p4UfleP-Yo))
![image.png]([https://github.com/killerwhale1125/trade-service/issues/3#issue-2998671359](https://private-user-images.githubusercontent.com/165141435/434195203-97a450ae-1a17-429e-8ba4-4c2f12f32ad1.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3NDQ3ODc1MDQsIm5iZiI6MTc0NDc4NzIwNCwicGF0aCI6Ii8xNjUxNDE0MzUvNDM0MTk1MjAzLTk3YTQ1MGFlLTFhMTctNDI5ZS04YmE0LTRjMmYxMmYzMmFkMS5wbmc_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjUwNDE2JTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI1MDQxNlQwNzA2NDRaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT0wNzhlNmM1NDRjMjRmMjQyZTkxN2U1ZjE5MzY4OTU2ZjEzOWVkNGQyOTRjMTlmYjg4YzkzNjVjOGUzMTBjZTQ3JlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCJ9.g_-GNWKmi53IuvKInQpeldhFV0AfzTT5OaPxd3ViEtg))

**임시테이블 생성 부하 발생**

![image.png]([https://github.com/killerwhale1125/trade-service/issues/4#issue-2998672655](https://private-user-images.githubusercontent.com/165141435/434195399-fdcac47c-3c82-4bb4-9e6f-7b6c615d5163.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3NDQ3ODc1MTYsIm5iZiI6MTc0NDc4NzIxNiwicGF0aCI6Ii8xNjUxNDE0MzUvNDM0MTk1Mzk5LWZkY2FjNDdjLTNjODItNGJiNC05ZTZmLTdiNmM2MTVkNTE2My5wbmc_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjUwNDE2JTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI1MDQxNlQwNzA2NTZaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT05NDlmYzQzNDg5M2YxYjYyOGY4ZTY0ZGFlMDA2OTYwMWIwMjhiNmI1ODM2ZGJmNmZhMmI1ODU2NDUyOWQ1YTYyJlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCJ9.0HVFqSGdRLqADWiH9C9waxSbmUC4uMS1oVrCg0kl6-s))

| 항목              | 결과                       | 향상률         |
|-------------------|----------------------------|----------------|
| CPU 사용량        | WAS & DB 20% 동일 병목 X   | ✅              |
| TPS               | 1.98 k/s (1900개)          | ✅              |
| Latency           | 80초 이상 → 3 ms           | ✅              |
| InnoDB Cache Hit  | 100%                        | ✅              |
| 임시 테이블 생성  | 생성 수 많음               | ❌              |

**결과**
전체적인 응답 속도 및 Disk I/O는 개선 되었지만, 불필요한 리소스 낭비 발생

**커버링 인덱스 최적화**

![image.png](https://private-user-images.githubusercontent.com/165141435/434195847-a80a03ba-c904-4315-bf21-9acac86d0b4e.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3NDQ3ODc1NDcsIm5iZiI6MTc0NDc4NzI0NywicGF0aCI6Ii8xNjUxNDE0MzUvNDM0MTk1ODQ3LWE4MGEwM2JhLWM5MDQtNDMxNS1iZjIxLTlhY2FjODZkMGI0ZS5wbmc_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjUwNDE2JTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI1MDQxNlQwNzA3MjdaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT00N2U0MjJhOTdhMDA1ZTU0MTQ0YWU3NmY5NGI2NTIwN2QyYjY3MzNiOTVhYTU0ZTU2MzJhOWY5M2EzMmUwOWVmJlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCJ9.L2r9LESZ6wtjYWa6JXQ1FP4Kr3OCXQJMFrpBMgw1VII)


**이전 결과와 비교되는 임시 테이블 사용량**

![image.png](https://private-user-images.githubusercontent.com/165141435/434196019-780962b8-ea6d-432a-90d8-b1860427046f.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3NDQ3ODc1NjEsIm5iZiI6MTc0NDc4NzI2MSwicGF0aCI6Ii8xNjUxNDE0MzUvNDM0MTk2MDE5LTc4MDk2MmI4LWVhNmQtNDMyYS05MGQ4LWIxODYwNDI3MDQ2Zi5wbmc_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjUwNDE2JTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI1MDQxNlQwNzA3NDFaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT1jY2E4MjI2MjQ0OWFlMmFjMzA4ZmRkNWE4MzU5NGU3NWU4YzRhMGY2YTUxNzdjNDlhODkwOTY2ZDg4NWIxYTM1JlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCJ9.iyh73RJDG9ZO7AHYOEDkekS1QPeakX6jxH3D3yW-LUY)

**최종 결과**

| 항목                         | 기존 쿼리     | 서브 쿼리    | 커버링 인덱스    | 향상률              |
|------------------------------|---------------|---------------|------------------|---------------------|
| TPS                          | 테스트 X      | 1.9k/s        | 1.9k/s           | ✅                  |
| Latency                      | 80초 이상     | 3.925ms       | 3.125ms          | 80초 → 3ms 단축     |
| 임시 테이블 생성             | 다수 발생     | 빈번          | 거의 없음        | ✅                  |
| Disk IO                      | 과도함        | 완화됨        | 안정적 유지      | ✅                  |
| 안정성 (vuser 2000 부하 테스트 기준) | 테스트 불가 | 일부 지연      | 안정적 트래픽 대응 | ✅                  |


**3. 자주 변경되지 않는 데이터 성능 향상을 위한 Redis 캐싱과 ScaleOut 성능 개선**

**문제점**
- 메인페이지 조회 요청 시 여러개의 게시물 조회 요청으로 인한 느린 페이지 응답
- 사용자 수가 증가함에 있어 조회 시 성능 문제점
<br/>

**개선사항**
- 캐싱에 적합한 조건에 한하여 Redis 캐싱 적용
- ScaleOut을 통하여 로드밸런싱 트래픽 분산
<br/>

**캐싱 성능 개선 결과**

![image](https://github.com/user-attachments/assets/e0b47d95-bffb-4391-8d43-27d6ee251d12)
- TPS : 231.7 -> 822.1 ( 3.55배 개선 )
- Latency : 3,363.13ms → 1,012.79ms ( 3.32배 단축 )
- 테스트 횟수 : 3,249회 -> 11,521회 ( 3.55배 개선 )
<br/>

**캐싱 & ScaleOut 성능 개선 결과**

![image](https://github.com/user-attachments/assets/48ec0a1a-32e6-4e83-9c44-ee166e35b601)
- TPS : 231.7 -> 1,091.2 ( 4.71배 개선 )
- Latency : 3,363.13ms → 462.75ms ( 7.27배 단축 )
- 테스트 횟수 : 3,249회 -> 19,171회 ( 5.90배 개선 )
