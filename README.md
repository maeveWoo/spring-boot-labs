# spring-boot-labs

## Index
- [Thymeleaf기초](#Thymeleaf기초)
- [컴포넌트스캔](#컴포넌트스캔)
- [빈 충돌 상황](#빈-충돌-상황)
- [라이브템플릿 세팅](#라이브템플릿-세팅)

---
- [의문 리스트](#의문-리스트)

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
- ```ANNOTATION``` : 기본값, 어노테이션을 인식해 동잦ㄱ
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