# spring-boot-labs

## Index
- [SpringBoot기초](#SpringBoot기초)
- [Spring Security Architecture](#Spring-Security-Architecture)
- [Thymeleaf 기초](#Thymeleaf기초)
- [Thymeleaf Security](#Thymeleaf-Security)
- [Thymeleaf layout dialet](#Thymeleaf-layout-dialet)
- [파일업로드](#파일업로드)
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

예를들어, 웹 UI는 인증이 실패했다는 페이지를 렌더링할 수 있고,  ```WWW-Authenticate```header가 같이(같이 안올 슈도 있음), backend HTTP는 401 응답을 줄것이다.

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
이 예제는 웹 어플리케이션과 관련있다. 하지만, ```AuthenticationManagerBuilder```는 더 넓게 쓰일 수 있다.([web security](https://spring.io/guides/topicals/spring-security-architecture/#web-security)[^WebSecurity])

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
> :warning: In Spring-security 5.7 M2 deprecated ```WebSecurityConfigurerAdapter``` 사용자가 컴포넌트 기반의 security 설정을 하도록 권장하고있다.
> docs : https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter

만약 설정에서 메소드에 ```@Override```를 사용했다면, ```AuthenticationManagerBuilder```는 global의 child가 될 "local"```AuthenticationManager```를 구성할 뿐이다.

스부에서는 global ```AuthenticationManagerBuilder```를 다른 빈에 주입```@Autowired```할 수 있다. 

그러나, local인 것을 명시적으로 노출하지 않는 한, ```@Autowired```로 주입할 수 없다.

스부는 default global ```AuthenticationManager```를 제공한다.
(사용자가 ```AuthenticationManager```타입의 빈을 선점해서 제공하지 않는 한)

이 기본값은 충분히 안전하므로 사용자 정의 global이 적극적으로 필요하지 않는 한 크게 걱정할 필요가 없다.

만약 ```AuthenticationManager``` 빌드를 위해 어떤 설정을 하려한다면, global 기본값에대해 고민하지않고, 주로 사용자는 보호하려는 리소스를 위해 locally하게 만들면된다.

### Authorizatiob or Access Control
authorization의 핵심 전략은 "AccessDecisionManager"이다. 

프레임워크는 3가지 구현체를 제공한다. 그리고 이 세가지는 "AccessDecisionVoter"의 체인 인스턴스에 위임한다.

"ProviderManager"가 "AuthenticationProviders"에 위임하는 것과약간 비슷하다. 

"AccessDecisionVoter"는 "Authentication"을 고려하고(원칙(principal 을 표현)), "ConfigAttributes"로 데코레트된 "Object"를 보호한다.

```java
boolean supports(ConfigAttribute attribute);

boolean supports(Class<?> clazz);

int vote(Authentication authentication, S object, Collection<ConfigAttribute> attributes);
```
"AccessDecisionManager", "AccessDecisionVoter"의 시그니처에서 "Object"는 완전히 제네릭이다.

사용자가 접근(웹 리소스나, 자바 메서드 두개는 가장 일반적인 케이스)을 원하는 어떠한 것도 표현할 수 있다.

"ConfigAttributes"역시 제네릭이다. 또, 그것에 접근하기 위해 필요한 권한레벨을 결정하는 메타데이터와 함께 "Object"의 보안 데코레이션을 보여준다.

"ConfigAttribute" 는 인터페이스이다. 얘는 하나의 메서드만 갖는다(이 메서드는 제네릭이고 "String"을 반환한다.) 그래서 이 문자열은 자원의 소유자의 의도대로 암호화(encode)된다.

이는 누가 그것에 접근할 수 있는지 규칙을 표현한다.

유형별 "ConfigAttribute"는 사용자 역할의 이름이며("ROLE_ADMIN", "ROLE_AUDOT"같은) 그리고, 야들은 특별한 포맷("ROLE_"같은)이 있거나 평가가 필요한 표현을 보여준다.


일반적으로 "ConfigAttributes"sms Spring Expression Language(SpEL)표현으로 사용한다.

예를 들어, "isFullyAuthenticated() && hasRole('user')" 이 표현은 "AccessDecisionVoter"가 지원해준다. 

"AccessDecisionVoter"는 표현을 다룰 수 있고, 그들을 위해 컨텍스트를 생성한다.

 표현법의 범위를 핸들링할 수 있게 확장하기 위해 "SecurityExpressionRoot"과 가끔 "SecurityExpressionHandler"의 커스텀 구현체를 필요로한다.

### Web Security
웹 계층에서 Spring Security는 Servlet "Filters"에 기본으로있다. 그래서, "Filters"의 역할을 먼저 보는 것이 도움이된다.

아래 그림은 하나의 HTTP 요청에대한 핸들러의 유형별로 계증층을 보여준다. 
![filter](img/filters.png)

클라이언트는 어플리케이션에 하나의 요청을 보내고, 컨테이나는 어떤 필터와 어떤 서블렛을 적용할지를 요청 URI의 경로를 기반으로 결정한다. 

대게 하나의 서블렛은 하나의 요청을 다룰 수 있지만, 필터는 체인으로 형성되고, 정렬된다.

사실 필터는 만약에 요청을 자체적으로 처리하길 원하면, 나머지 체인을 거부할 수 있다.

필터는 요청과 응답(다운스트림 필터와 서블릿에서 사용되는)을 변경할 수 있다.(?)

필터체인의 정렬은 굉장히 중요하다. 스프링 부트는 이것을 두가지 매커니즘으로 관리한다.

```@Bean```타입의 필터는 ```@Order```를 갖거나 ```Ordered```를 구현한다.

그리고, 얘들은 ```FilterRegisterationBean```의 한 부분이 될 수 있다. ```FilterRegisterationBean```은 스스로 자신의 API의 한 부분으로 요청한다.

어떤 만들어진 필터는 도움요청(help signal)에 충실하게 정의되어있다. 

예를들어, Spring Session의```SessionRepositoryFilter```는 "Integer.MIN_VALUE + 50"의 "DEFAULT_ORDER"를 갖는다.

이는 체인에서 좀더 앞서고 싶지만, 다른 필터들이 나오기 전에 규칙을 어기지 않는다.

스프링 시큐러티는 체인에서 하나의 필터로 설치되어있다. 그리고 이는 ```FilterChainProxy```타입으로 고정(concrete)이다.

스프링 부트 어플리케이션에서, security 필터는 어플리케이션 컨텍스트에서 ```@Bean```이다.

그리고 야는 기본으로 설치되어있고, 모든 요청에 적용할 수 있다. 

security 필터는 "SecurityProperties.DEFAULT_FILTER_ORDER" 에 의해 정의된 위치에 설치된다.

"SecurityProperties.DEFAULT_FILTER_ORDER"는 "FilterRegistrationBean.REQUEST_WRAPPER_FILTER_MAX_ORDER"에의해 고정된다.

> (뭐라는거야)

![spring security filter](img/security-filters.png)

Spring Security는 하나의 물리적인 필터이다. 그러나 내부 필터들의 체인에 과정을 위임시킨다.

사실, security 필터에서 간접적인 계층이 하나 더 있다. 
얘는 "DelegatingFilterProxy"라는 컨테이너에서 설치된다. 얘는 Spring ```@Bean```일 필요가 없다.

이 프록시는 "FilterChainProxy"에게 위임을한다. "FilterChainProxy"는 항상 ```@Bean```이고, "springSecurityFilterChain"이라는 고정된 이름을 갖는다.

이 "FilterChainProxy"는 내부적으로 필터의 체인으로 구설된 보인 로직을 전부 포함한다.

모든 필터는 같은 API를 갖는다.(필터들은 전부 서블릿 설명서의 "Filter" 인터페이스로 구현된다.), 모든 필터는 나머지 체인들을 거부할 수 있는 기회가있다.

"FilterChainProxy"와 같은 탑레벨에서 Spring Security로 관리되는 모든 필터체인은 많을 수 있다.
그리고, 모드 컨체이너에게 알려지지 않는다.

Spring Security 필터는 필터체인의 리스트를 갖고있고, 필터와 매칭되는 첫번째 체인과 요청을 분리한다. 

아래 이미지는 요청 경로 매칭에 기반한 분리(dispatch) 상황을 보여준다.
이는 아주 일반적이지만 요청을 매칭하는것이 유일한 방법이 아니다. 

이 분리(dispatch) 과정의 가장 중요한 특징은 하나의 체인만이 요청을 다른다는 것이다.

![Security filters dispatch](img/security-filters-dispatch.png)

Spring Security "FilterChainProxy"는 먼저 매칭되는 첫번째 체인에 요청을 분리한다.

커스텀 security 설정이 없는 vanilla Spring Boot 어플리케이션은 n개의 필터 체인이있다. 보통 n = 6.

첫번째 체인(n-1)은 static 자원의 패턴들을 무시한다. (/css/**, /images/**, /error 뷰 같은)
(그 경로는 SecurityProperties에서 "security.ignored"로 사용자가 수정할 수 있다.)

마지막 체인은 catch-all 경로(/**) 매칭하고, 인증의 정보를 포함하고, 인가, 예외처리, 세션 처리, 헤더 작성 등등 더 많은 작업을한다.

기본(default) 체인에서 전부 11개의 필터가 있다. 일반적으로 사용자는 어떤 필터가 언제 사용되는지에 대한 걱정을 할 필요는 없다.

> Spring Security 내의 모든 필터는 사실 컨테이너에게 알려지지 않았다. 이는 중요하다. 특히 Spring Boot 어플리케이션에서,
기본적으로 모든 Filter 타입의 ```@Bean```은 컨테이너에 자동으로 등록된다. 그래서 사용자가 커스텀 필터를 security 체인에 추가하고 싶다면,
```@Bean```을 만들지 말거나, 컨테이너가 등록을 목하도록 명시적으로 "FilterRegistrationBean"으로 감싸야한다.

### 필터 페인의 생성과 커스터마이징
Spring Boot 어플리케이션에서 기본 필터체인은 "SecurityProperties.BASIC_AUTH_ORDER"의 주문에의해 먼저 정의되있다.

"security.basic.enabled=false" 세팅을 통해 완전히 설정을 끌 수 있고, 아니면 대비책으로 기본설정을 사용할 수 있다.
그리고, 더 낮은 우선순위를 갖는(lower order) 다른 규칙을 정의할 수 있다.

나중에, "WebSecurityConfigurerAdapter"또는 "WebSecurityConfigurer" 타입의 ```@Bean```을 추가할 수 있다.

그리고 ```@Order```를 이용해 클래스를 데코레이트할 수 있다.

:warning:  spring security 5.7 "WebSecurityConfigurerAdapter" deprecated

```java
@Configuration
@Order(SecurityProperties.BASIC_AUTH_ORDER - 10)
public class ApplicationConfigurerAdapter extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/match1/**")
                ...;
    }
}
```
이 빈은 Spring Security가 새로운 필터체인을 추가하고, 기본 설정 이후의 우산순위를 갖게야기한다.

많은 어플리케이션은 리소스 집합에 대해 완전히 다른 접근 규칙을 가지고 있다.

각 리소스의 세트는 자신들만의 유일한 순서(order)와 요청 매칭(request matcher)과 함께 "WebSecurityConfigurerAdapter"를 갖는다.

만약 매칭룰리 오버랩되면, 가장 우선순위가 높은 필터체인이 이긴다.

### 분리(Dispatch)와 인증을 위한 요청 매치
security 필터 체인(="WebSecurityConfigurerAdapter)은 요청 매칭(request matcher)을 갖는다.
얘는 HTTP 요청에 이 필터를 적용할지 말지 결정한다.

특정 필터 체인을 적용하기로 결정되면, 다른 필터 체인은 적용되지 않는다.

필터체인을 적용하면, "HttpSecurity" 설정에서 추가적인 매칭을 세팅하면 더 섬세하게 인가를 설정할 수 있다.

```java
@Configuration
@Order(SecurityProperties.BASIC_AUTH_ORDER - 10)
public class ApplicationConfigurerAdapter extends WebSecurityConfigurerAdapter {
    @Override
    protected  void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/match1/**")
                .authorizeRequests()
                .antMatchers("/match1/user").hasRole("USER")
                .antMatchers("/match1/spam").hasRole("SPAM")
                .anyRequest().isAuthenticated();
    }
}
```


## 파일업로드
스프링 부트 MVC 어플리케이션은(thymeleaf, spring-boot-starter-web 이 추가된) 파일 업로드를 하기 위해서는 "MultipartConfigElement"를 등록해야한다.
스부는 모두 auto-config!

### Flash Attribute
참고 자료들
- [spring docs](https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-flash-attributes)
- [baeldung](https://www.baeldung.com/spring-web-flash-attributes)

## Thymeleaf기초
controller에 등록된 html은 ```templates/~```에서 찾는다.

form태그와 함께 사용하는 ```th:object="${greeting}"``` 표현은 폼데이터를 수집하기 위한 모델 객체는 선언한다.

@, * 가붙은 EL(?) 표현식을 잘 파악해 보자

### Form Handling
참고 : HelloController - greeting GET/POST

form 요소에서 ```th:action="@{/greeting}"``` 표현은 POST "/greeting"엔드포인트로 바로간다는데.. 안감.

```method="post"``` 넣어줘야 POST되는디? 기본은 GET으로 감.

### Validation 하기
gradle
```implementation 'org.springframework.boot:spring-boot-starter-validation'```

#### :closed_book: ***Errors, BindingResult*** 주의 사항
[슈퍼 간단한 예제때매 똥뻘짓하다 발견한 문서 내용](https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-ann-arguments)

Errors, BindingResult: 또는, @RequestBody, @RequestPart 인자의 유효성 검사와 명령 객체(@ModelAttribute 인자)에 데이터 바인딩에서 에러에 접근하기위해 사용.
***"Errors", "BindingResult" 인자는 유효성 검사용 메소드 인자의 직후에 위치해야되는걸 반드시 확실히해라***

## Thymeleaf Security
### extras spring security
[thymeleaf extras spring security](https://github.com/thymeleaf/thymeleaf-extras-springsecurity)
[extras springsecurity 잘 정리된 문서](https://www.javadevjournal.com/spring-security/spring-security-with-thymeleaf/)

- 유틸리티 객체 표현법 사용
```html
<div th:text="${#authentication.name}">
  The value of the "name" property of the authentication object should appear here.
</div>
<div th:if="${#authorization.expression('hasRole(''ROLE_ADMIN'')')}">
  This will only be displayed if authenticated user has role ROLE_ADMIN.
</div>
```

```#authorization```는 ```org.thymeleaf.extras.springsecurity[5|6].auth.Authorization```의 객체이다.

- 어트리뷰트 사용하기 
```sec:authentication``` 어트리뷰트는 ```#authorization```랑 같다. 사용법만 다름
```html
<div sec:authentication="name">
  The value of the "name" property of the authentication object should appear here.
</div>
```

```sec:authorize```와 ```sec:authorize-expr```어트리뷰트는 같다. 
스프링 시큐러티 expression에서 ```th:if```는 유틸리티 객체 표현법에서 ```#authorization.expression(...)```와 같은기능 제공.

- 네임스페이스
dialect의 모든 버전의 네임스페이스는 ```http://www.thymeleaf.org/extras/spring-security```이다.

잘못된 네임스페이스를 가져오는 것은 템플릿 처리에 영향을 미치지 않지만, 템플릿의 제안/자동완성 같은 IDE에 영향을 줄 수 있다.

## Thymeleaf layout dialet
[thymeleaf layout dialet 정리 잘 된 문서](https://ultraq.github.io/thymeleaf-layout-dialect/)

```layout:decorate="~{layouts/default}"```
```layout:decorate```을 사용햐서, 페이지를 연결한다. 얘는 페이지의 루트 요소를 기준으로 동작한다. 

controller에서 응답페이지는 content 경로를 잡아주면된다. 헉헉

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
[^WebSecurity]: 