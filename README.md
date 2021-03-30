# sonar service
---

## 설명

## 기능



## 사용한 프레임워크
<hr>

- Spring Framework 4.3.20.RELEASE
  - Spring-webmvc
  - Spring-test
  - Spring-jdbc
  - Spring-context
- mybatis 3.4.1
- mysql 8.0.18
- jstl 1.2

- pom.xml에서 dependency를 확인해주세요!


## 어플리케이션 시작 방법

**시작전 주의사항**
1. 해당 프로젝트는 Spring 4.3.20.RELEASE 버전으로 빌드 되었습니다.
2. MySQL 8.0.18 버전을 설치해주세요
3. Tomcat 9.0 버전을 설치해주세요


**실행 방법**
1. 적절한 directory에 파일 클론

2. 아래의 명령어로 mysql 실행
```java
mysql.server start

or

brew services start mysql
```
3. eclipse로 해당 디렉토리 작업 폴더로 열기
4. server 추가후 tomcat 9.0 추가
5. 해당 프로젝트에 마우스오른쪽 -> maven -> update project 로 라이브러리 추가
6. 해당 프로젝트에 마우스 오른쪽 run as -> run on server -> tomcat 9로 실행



<br><br>

## Application 흐름도
<hr>

![application](readmeSource/application.png)

- 5번 과정에서 DAO를 이용
  - Service -> DAO -> mapper 즉 mybatis 를 활용하여 db 접속 및 데이터 추출
- 6번 과정에서는 BoardVO CommentVO 가 각각 존재
  - 해당 요청에 맞는 Java Beans로 model을 생성




## DB Diagram
<hr>

![dber](readmeSource/dber.png)


<br><br>


## Class Diagram
<hr>

![classdiagram](readmeSource/classDiagram.png)
