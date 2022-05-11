# spring-boot-labs

## Index
- [Thymeleaf기초](#Thymeleaf기초)
- [컴포넌트스캔](#컴포넌트스캔)
- [빈 충돌 상황](#빈-충돌-상황)
---
- [의문 리스트](#의문-리스트)

## Thymeleaf기초
controller에 등록된 html은 ```templates/~```에서 찾는다.

## 컴포넌트스캔

## 빈 충돌 상황
컴포넌트 스캔에 의해 자동으로 스프링빈이 등록되는데, 이때 클래스 이름의 앞글자를 소문자로 바꿔 스프링빈 등록이 된다.
그 이름이 같은 경우 스프링은 오류를 발생시킨다. 

- url mapping이 있으면 동작함..;;;[^1]

### 수동 빈 등록 VS 자동 빈 등록 이름 충동
수동 빈과 자동 빈의 이름이 충돌되면 어떻게 될까?

> :warning: Consider renaming one of the beans or enabling overriding by setting spring.main.allow-bean-definition-overriding=true

---

## 의문 리스트
[^1]: conclictController conflict2()동작함.
```@GetMapping("!Captitalize")```있다고 빈이 등록됨... 뭥미?