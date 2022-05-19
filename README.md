# spring-boot-labs

## Index
- [SpringBoot기초](#SpringBoot기초)
- [Spring Security Architecture](#Spring-Security-Architecture)
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

### Add Production-grade Services
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
SpringBoot Loader Module[^SpringBootLoaderModule] 덕분에 기존의 WAR 파일 배포를 지원할 뿐만 아니라 실행 가능한 JAR를 함께 넣을 수 있다.

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

GroovyGrape[^GroovyGrape]를 사용해서 앱을 실행하기 위한 라이브러리를 받는다.

## Spring Security Architecture
애플리케이션 보인은 인증(Authentication : 당신은 누구?)과 권한 부여(인가)(Authorization : 당신은 뭘 할 수 있음?)라는 두 가지 독립적인 문제로 요약된다. 

때때로 사람들은 "권한 부여(인가)"대신 "접근 관리"라고도 한다.

Spring Security는 인증과 인가를 분리하도록 설계뙨 아키텍처가 있으며, 이는 이둘에 전략과 확장 포인트를 갖게한다.

### 인증(Authentication)
인증을 위한 주 전략 인터페이스는 ```AuthenticationManager```이다. ```AuthenticationManager```는 하나의 메소드만 가진다.

```java
public interface AuthenticationManager {
  Authentication authenticate(Authentication authentication)
          throws AuthenticationException;
}
```

```AuthenticationManager```은 ```authenticate()``` 메소드로 3가지 일을 할 수 있다.
- 입력한 값이 유효한 원칙(principal)인지 검증할 수 있다면, ```Authentication```(일반적으로 authenticated=true와 함께)을 반환
- 입력한 값이 유효하지 않은 원칙임을 확인한다면, ```AuthenticationException```을 던진다.
- 결정할 수 없을 경우 ```null```을 반

```AuthenticationException```은 Runtime Exception이다. 

```AuthenticationException```은 보통 일반적인 방법으로 어플리케이션에서 스타일이나, 어플리케이션의 목적에 따라 처리된다.

즉, 사용자의 코드는 일반적으로 이 익셉션을 잡거나 핸들링하기를 바라지 않는다.

예를들어, 웹 UI는 인증이 실패했다는 페이지를 렌더링할 수 있고,  ```WWW-Authenticate```header가 와 같이(같이 안올 슈도 있음), backend HTTP는 401 응답을 줄것이다.

더 일반적인 ```AuthenticationManager```의 구현은 ```ProviderManager```이다.

```ProviderManager```은 ```AuthenticationProvider```한 인스턴스 체인에게 위임을 한다.

```AuthenticationProvider```는 ```AuthenticationManager```과 약간 비슷하지만, 다른 메서드를 갖는다.

이 메서드는 ```AuthenticationProvider```이 주어진 ***인증타입***을 지원하는지 호출자에게 물을 수 있게 허락한다.
```java
public interface AuthenticationProvider {

  Authentication authenticate(Authentication authentication)
          throws AuthenticationException;

  boolean supports(Class<?> authentication);
}
```
이 ```supports()```메소드의  ```Class<?>``` 인자는 실제로```Class<? extends Authentication>```이다.
(이 인자는 authenticate() 메소드를 지원하는지만 물어본다.)

```ProviderManager```는 ```AuthenticationProviders```의 체인에게 같은 애플리케이션이 위임을 통해 여러개의 다른 인증 메커니즘을 제공할 수 있다.

만약 ```ProviderManager```가 인증 인스턴스의 타입 일부를 인식하지 못하면, 이는 넘어간다.(skip)

```ProviderManager``` 선택적으로 부모를 같을 수 있으며. 이는 만약 모든 제공자가 null을 반환한다면 고려해볼 수 있다.
(뭔소리야 제공자가 전부 null을 반환하면 부모를 갖는게 좋다는 뜻인듯)

만약 부모가 유효하지 않다면, null 인증 결과는 ```AuthenticationException```이다.

때때로, 어플리케이션은 보호된 리소스의 논리적 그룹을 갖는다.(ex: path pattern 매칭과 관련된 모든 웹 자원)

그리고 각 그룹은 그들의 고유한 ```AuthenticationManager```를 갖는다.

자주, 각각의 ```AuthenticationManager```는 ```ProviderManager```이고,  이들은 부모를 공유한다.

이 부모는 "global"자원의 한 종류로, 모든 제공자에게 예비품(fallback)처럼 동작한다.

### Customizing Authentication Managers
스프링 시큐러티는 사용자 어플리케이선에 빠르게 일반적인 인증 매니저의 특징을 셋업할 수 있도록 몇몇 설정을 제공한다.

가장 일반적으로 사용되는 헬퍼는 ```AuthenticationManagerBuilder```이다. 

```AuthenticationManagerBuilder```는 인메모리, JDBC, LDAP 사용자 세부사항 또는 커스텀한```UserDetailService```추가 셋업에 훌륭하다.

다음 예제는 global(parent) ```AuthenticationManager```설정을한 어플리케이션을 보여준다.

```java
import javax.sql.DataSource;

@Configuration
public class ApplicationSecurity extends WebSecurityConfigurerAdapter {
  ... // web stuff  here

  @Autowired
  public void initialize(AuthenticationManagerBuilder builder, DataSource dataSource) {
      builder.jdbcAuthentication().dataSource(dataSource).withUser("dave").password("secret").roles("USER");
  }
}
```
이 예제는 웹 어플리케이션과 관련있다. 하지만, ```AuthenticationManagerBuilder```는 더 넓게 쓰일 수 있다.([web security](https://spring.io/guides/topicals/spring-security-architecture/#web-security)[^web security])

이```AuthenticationManagerBuilder```는 ```@Autowired```이다. 이 메소드는```@Bean```으로 등록됬다.

이는 ```AuthenticationManager```를 global(parent)를 제공하게한다. 

대조적으로 아래 예시를 보자

```java
import javax.sql.DataSource;

@Configuration
public class ApplicationSecurity extends WebSecurityConfigurerAdapter {

  @Autowired
  DataSource dataSource;
  
  ... // web stuff here 
  
  @Override
  public void configure(AuthenticationManagerBuilder builder) {
      builder.jdbcAuthentication().dataSource(dataSource).withUser("dave").password("secret").roles("USER");
  }
}
```
만약 설정에서 메소드에 ```@Override```를 사용했다면, ```AuthenticationManagerBuilder```는 global의 child가 될 "local"```AuthenticationManager```를 구성할 뿐이다.

스부에서는 global ```AuthenticationManagerBuilder```를 다른 빈에 주입```@Autowired```할 수 있다. 

그러나, local인 것을 명시적으로 노출하지 않는 한, 저렇게 주입할 수 없다.

스부는 default global ```AuthenticationManager```를 제공한다.
(사용자```AuthenticationManager```타입의 빈을 선점해서 제공하지 않는 한)

이 기본값은 충분히 안전하므로 사용자 정의 global이 적극적으로 필요하지 않는 한 크게 걱정할 필요가 없다.

만약 ```AuthenticationManager``` 빌드를 위해 어떤 설정을 하려한다면, global 기본값에대해 고민하지않고, 주로 사용자는 보호하려는 리소스를 위해 locally하게 만들면된다.

### Authorizatiob or Access Control
authorization의 핵심 전략은 "AccessDecisionManager"이다. 

프레임워크는 3가지 구현체를 제공한다. 그리고 이 세가지는 "AccessSecisionVoter"의 체인 인스턴스에 위임한다.

"ProviderManager"가 "AuthenticationProviders"에 위임하는 것과약간 비슷하다. 

"AccessDecisionVoter"는 "Authentication"을 고려하고(원칙(principa을 표현)), "ConfigAttributes"로 데코레트된 "Object"를 보호한다.

```java
boolean supports(ConfigAttribute attribute);

boolean supports(Class<?> clazz);

int vote(Authentication authentication, S object, Collection<ConfigAttribute> attributes);
```
"AccessDecisionManager", "AccessDecisionVoter"의 시그니처에서 "Object"는 완전히 제네릭이다.

사용자가 접근(웹 리소스나, 자바 메서드 두개는 가장 일반적인 케이스)을 원하는 어떠한 것도 표현할 수 있다.

"ConfigAttributes"역시 제네릭이다. 또, 보안 "Object"의 데코레이션을 보여준다.

"Object"는 그것에 접근하기 위해 필요한 권한레벨을 결정하는 메타데이터와 함께

## Thymeleaf기초
controller에 등록된 html은 ```templates/~```에서 찾는다.

form태그와 함께 사용하는 ```th:object="${greeting}"``` 표현은 폼데이터를 수집하기 위한 모델 객체는 선언한다.

@, * 가붙은 EL(?) 표현식을 잘 파악해 보자

### Form Handling
참고 : HelloController - greeting GET/POST

form 요소에서 ```th:action="@{/greeting}"``` 표현은 POST "/greeting"엔드포인트로 바로간다는데.. 안감.

```method="post"``` 넣어줘야 POST되는디? 기본은 GET으로 감.



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

[^SpringBootLoaderModule]: Spring-boole-loader 모듈은 실행가능 jar와 war파일을 지원한다. [Docs](https://docs.spring.io/spring-boot/docs/current/reference/html/executable-jar.html)
[^GroovyGrape]: Grape는 Groovy에 내장되있는 JAR 종속성 매니저이다. [Docs](http://docs.groovy-lang.org/latest/html/documentation/grape.html)
[^web security]: 