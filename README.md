<p align="center">
  <br>  
  <div align="center"><img src="https://www.notion.so/image/https%3A%2F%2Fs3-us-west-2.amazonaws.com%2Fsecure.notion-static.com%2F4fdd23a0-886e-4f5c-9448-14e8cd2dc9d8%2F0cf1d10235c37b2635c02719125da37cc1bd632518198b1e1da7f5a364156540.png?table=block&id=a5fce4ec-5eaa-4f13-984a-30f29b85f4d1&width=3330&userId=&cache=v2" width="40%"/></div>
</p>


<p align="center"><span>당근마켓</span>을 모티브로 만든 중고거래 플랫폼 API 서버 토이 프로젝트입니다.</p>

<br>
<br>

### 🥕 월 1000만명 이상 사용하고 있는 <span>당근마켓</span>은 어떻게 서비스를 하고있을까?
<p>
* 당근마켓은 어떻게 수 많은 사용자들에게 서비스를 제공하고있을까? <br>
* 당근마켓은 어떻게 대용량의 트래픽을 견디고 있을까? <br>이러한 궁금증으로 인해 직접 당근마켓 서버를 간단히 구현해보는 프로젝트를 진행하게 되었습니다.<p>

<br>

### 🥕 단순히 <span>당근마켓</span>의 기능을 위해 CRUD만 구현하는 방식은 이제 그만!

* 대규모 트래픽을 어떤 방식으로 처리하고있는지
* 당근 마켓에 필요한 오브젝트들의 상호작용을 어떻게 객체지향적으로 풀어냈는지

대용량 트래픽에도 장애없이 동작할 수 있도록 성능과 유지보수성을 고려한 서비스를 만들기 위해서, 읽기 좋은 코드 객체지향적 설계를 위해 노력하였습니다.

<br>
<br>

### 🥕 프로젝트 수행 중 발생한 이슈 및 트러블 슈팅

* 분산 서버 환경에서 세션 불일치 문제 해결하기
    * [사용자가 증가하면 서버를 어떻게 확장해야 할까?](https://see-one.tistory.com/4)
    * [분산서버 환경에서 발생할 수 있는 Session 불일치 문제를 해결해봅시다 (1) - Sticky Session](https://see-one.tistory.com/10)
<!--     * [분산서버 환경에서 발생할 수 있는 Session 불일치 문제를 해결해봅시다 (2) - Session Clustering]() <img src="https://img.shields.io/badge/-WRITING-gray"><br> 
    * [분산서버 환경에서 발생할 수 있는 Session 불일치 문제를 해결해봅시다 (3) - Redis와 Memcached]() <img src="https://img.shields.io/badge/-WRITING-gray"><br>  -->
<!-- * 중복되는 로그인 체크 기능을 인터셉터를 이용해서 구현하기 
    * [사용자 로그인 체크는 어디서 해야할까요 - Filter와 Interceptor]() <img src="https://img.shields.io/badge/-WRITING-gray"><br>  -->
* `@Email` Validation이 정상적으로 동작하지 않는 문제 해결하기
    * [무심코 적용한 Validation 의심해볼 필요가 있습니다](https://see-one.tistory.com/14) 
<!-- * `@Cacheable` 이 정상적으로 동작하지 않는 문제 해결하기
    * [당신의 @Cacheable이 동작하지 않는 이유 - Spring Dynamic Proxy 기반 AOP의 동작과정]() <img src="https://img.shields.io/badge/-WRITING-gray"><br> 
* RDBMS의 가용성과 부하 분산을 위해 데이터베이스 이중화 적용하기 
    * [RDBMS 서버의 부하 분산과 가용성을 위해 이중화를 적용해봅시다]() <img src="https://img.shields.io/badge/-WRITING-gray"><br>
    * [Spring Data Jpa 프로젝트에 Mutli DataSource와 RoutingDataSource 적용하기]() <img src="https://img.shields.io/badge/-WRITING-gray"><br>
    * [Spring 빈들의 순환참조가 발생하는 이유와 순환참조 문제 해결하기]() <img src="https://img.shields.io/badge/-WRITING-gray"><br>
* Jenkins와 Docker를 이용하여 CI/CD 구현하기 
    * [기업들은 왜 CI와 CD를 적용할까요?]() <img src="https://img.shields.io/badge/-WRITING-gray"><br>
    * [Jenkins 파이프라인을 이용한 CI 구축하기]() <img src="https://img.shields.io/badge/-WRITING-gray"><br>
    * [스프링 프로젝트를 Docker를 이용해서 배포하기]() <img src="https://img.shields.io/badge/-WRITING-gray"><br>
* 내가 만든 서버는 얼마나 많은 사용자가 이용할 수 있는지 성능 테스트하기
    * [내가 만든 서버는 얼마나 많은 사용자가 이용할 수 있을까요 - nGrinder를 이용한 성능 테스트]() <img src="https://img.shields.io/badge/-WRITING-gray"><br> -->

<br>


<br>

### 🥕 프로젝트 전체 구성도
<div align="center"><img src=""></div>
<br>
<br>

### 🥕 사용한 기술 스택

<br>

<div align="center"><img src=""></div>

<br>

### 🥕 프로젝트 화면 구성도 

<br>
<div align="center"><img src="">
