# spring-boot-labs

## Index
- [SpringBoot기초](#SpringBoot기초)
- [Thymeleaf기초](#Thymeleaf기초)
- [컴포넌트스캔](#컴포넌트스캔)
- [빈 충돌 상황](#빈-충돌-상황)
- [라이브템플릿 세팅](#라이브템플릿-세팅)

---
- [의문 리스트](#의문-리스트)

## SpringBoot기초
```@SpringBootApplication```은 아래 어노테이션을 포함하고는 편리한 어노테이션이다.
- ```@Configuration``` : application context에대한 빈 정의의 소스로, 클래스에 태그를 한다.
- ```@EnableAutoConfiguration``` : 클래스 패스 세팅, 다른 빈들과 다양한 프로퍼티 세팅을 기반으로 빈을 등록하게 한다.
> 예를들어 spring-webmvc 가 클래스패스에 있으면, 이 어노테이션은 웹 어플리케이션으로 플래그하고, DispatcherServlet세팅을과 같은 핵심 동작을 활성화한다.
- ```@ComponentScan``` : 스프링이 다른 컴포넌트들과, 설정들과 서비스들을 컨트롤러에게 찾도록 한다.

### Run the Application
gradle 기반
```terminal
./gradlew bootRun
```

maven
```terminal
./mvnw spring-boot:run
```

### Add Unit Tests
```MockMvc```는  Spring Test에서 왔다. 이것은 편리한 빌더 클래스들과 HTTP request들을 DispatcherServlet에 보낼 수 있게한다.
그리고 결과에 대한 assertions을 만들어준다.

MockMvc를 주입하기위해  사용된 ```@AutoConfigureMockMvc```와 ```@SpringBootTest``` 사용을 기록하자.

```@SpringBootTest```를 사용하면, 전체 어플리케이션 컨텍스트가 생성되도록 요청한다.

대안으로 ```@WebMvcTest```를 사용하면 컨텍스트의 웹 계층만 생성하도록 스프링부트에 요청한다.

이 두가지 경우 모두, 스프링 부트는 자동으로 어플리케이션의 메인 어플리케이션 클래스로 위치하려 시도한다.

하지만 개발자는 덮어쓰거나, 약간의 다른 설정을 할 수 있게 한다.

뿐만아니라, 스프링부투를 사용해 풀스택 통합테스트를 작성할 수 있게, HTTP Request cycle을 모킹한다.

[참고](src/test/java/io/springbootlabs/app/web/in/HelloControllerTestIT.java)

내장서버는 ```webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT```때문에 랜덤 포트로 시작된다.

그리고 실제 포트는 자동으로 TestRestTemplate를 위한 기본URL에 설정된다.

## Add Production-grade Services
스부는 health, audits, beans,..등의 서비스는 actuator module과 함께 제공한다.

```groovy
implementation 'org.springframework.boot:spring-boot-starter-actuator'
```
이 위존성을 추가하고 bootRun을 실행하면, 새로운 RESTful 엔드포인트들이 어플리케이션에 추가된걸 볼 수 있다.

```
management.endpoint.configprops-org.springframework.boot.actuate.autoconfigure.context.properties.ConfigurationPropertiesReportEndpointProperties
management.endpoint.env-org.springframework.boot.actuate.autoconfigure.env.EnvironmentEndpointProperties
management.endpoint.health-org.springframework.boot.actuate.autoconfigure.health.HealthEndpointProperties
management.endpoint.logfile-org.springframework.boot.actuate.autoconfigure.logging.LogFileWebEndpointProperties
management.endpoints.jmx-org.springframework.boot.actuate.autoconfigure.endpoint.jmx.JmxEndpointProperties
management.endpoints.web-org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties
management.endpoints.web.cors-org.springframework.boot.actuate.autoconfigure.endpoint.web.CorsEndpointProperties
management.health.diskspace-org.springframework.boot.actuate.autoconfigure.system.DiskSpaceHealthIndicatorProperties
management.info-org.springframework.boot.actuate.autoconfigure.info.InfoContributorProperties
management.metrics-org.springframework.boot.actuate.autoconfigure.metrics.MetricsProperties
management.metrics.export.simple-org.springframework.boot.actuate.autoconfigure.metrics.export.simple.SimpleProperties
management.server-org.springframework.boot.actuate.autoconfigure.web.server.ManagementServerProperties
```
actuator는 다음을 노출한다.
- actuator/health
- actuator

```/actuator/shutdown``` 엔드포인트도 있다, 하지만 이는 JMX[^JMX]를 통해서만 볼 수 있다.
이를 HTTP 엔트포인트로 만들기위해 ```management.endpoint.shutdown.enable=true``` 를 어플리케이션 프로퍼티에 추가하자.
그리고 ```management.endpoints.web.exposure.include=health,info,shutdown``` 요것도 노출시키자.
그러나, 너는 공개 어플리케이션에서 shutdown 엔드포인트를 활성화하면 안될 수도 있다.

> 안대는데?

### JAR 지원 및 Groovy 지원
[^SpringBoot-loader-module] 덕분에 기존의 WAR 파일 배포를 지원할 뿐만 아니라 실행 가능한 JAR를 함께 넣을 수 있다.

```spring-boot-gradle-plugin```, ```spring-boot-maeven-plugin```을 통해 이둘을 데모를 지원하는 다양한 가이드가있다.

하나의 파일만큼 작은 Spring MVC  web 어플리케이션을 빌드할 수 있게, 스부는 Groovy도 지원한다.

(와씨 대박!!)

root 경로에 app.groovy를 생성하고 다음코드를 넣는다.
```groovy
@RestController
class ThisWillActuallyRun {

    @GetMapping("/")
    String home() {
        return "Hello, World!"
    }

}
```

먼저 [Springboot CLI를 설치](https://plein-de-verite.tistory.com/165)(mac용)

아래 커멘드 실행
```
$ spring run app.groovy
```

스부는 동적으로 핵심 애노테이션들과 코드를 넣어서 실행한다. 

[GroovyGrape](http://docs.groovy-lang.org/latest/html/documentation/grape.html)[^2]를 사용해서 앱을 실행하기 위한 라이브러리를 받는다.

## Thymeleaf기초
controller에 등록된 html은 ```templates/~```에서 찾는다.

## 컴포넌트스캔

### Scope
- ```@ComponentScan``` 어노테이션이 있는 파일의 패키지 아래를 찾는다.
- ```basePackages``` / ```basePackageClasses```로 지정도 가능
- 권장 방법: 구성파일에 등록시 프로젝트 최상단에 두기.
  - SpringBoot는 ```@SpringBootApplication```에 포함되어있음.


### 빈 충돌 상황
컴포넌트 스캔에 의해 자동으로 스프링빈이 등록되는데, 이때 클래스 이름의 앞글자를 소문자로 바꿔 스프링빈 등록이 된다.
그 이름이 같은 경우 스프링은 오류를 발생시킨다. 

>  :question: url mapping이 있으면 동작함..;;;[^1]

수동 빈과 자동 빈의 이름이 충돌되면 어떻게 될까?

> :warning: Consider renaming one of the beans or enabling overriding by setting spring.main.allow-bean-definition-overriding=true

빈 주입시 주입 대상이 여러개일 경우 충돌이 난다. ```@Qualifier```, ```@Named```, ```@Primary```를 사용해 충돌을 방지하자 

### 옵션
특정 어노테이션을 포함/제외 시킬 수 있음
- includeFilters : 컴포넌트 스캔 대상으로 추가
- excludeFilters : ~" 제외
```
@ComponentScan(
        includeFilters = @ComponentScan.Filter(type = FilterType.Annotation, classes = IncludeComponent.class),
        excludeFilters = @ComponentScan.Filter(type = FilterType.Annotation, classes = OutcludeComponent.class)
)
```
FilterType 옵션
- ```ANNOTATION``` : 기본값, 어노테이션을 인식해 동작
- ```ASSIGNABLE_TYPE``` : 지정한 타입과 자식 타입을 인식해 동작
- ```ASPECTJ``` : AspectJ 패턴 사용
- ```REGEX``` : 정규 표현식
- ```CUSTOM``` : TypeFilter이라는 인터페이스를 구현해서 처리

## 라이브템플릿 세팅
```
@org.junit.jupiter.api.Test
public void $EXPR$() {
    org.assertj.core.api.Assertions.assertThat($END$)
}
```

---

## 의문 리스트
[^1]: conclictController conflict2()동작함.
```@GetMapping("!Captitalize")```있다고 빈이 등록됨... 뭥미?

[^JMX]: [Java Management eXtension](https://docs.spring.io/spring-boot/docs/2.1.1.RELEASE/reference/html/production-ready-jmx.html)

[^SpringBoot-loader-module]:
[^2]: 침고이는 Groovy Grape