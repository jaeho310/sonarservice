## 프로젝트 설명
메이븐파일을 올리면 Sonarqube를 활용하여 Maven프로젝트에 대한 품질을 표현주는 프로젝트입니다.<br>
인증 인가는 로그인시 Session을 부여하여 인터셉터에서 확인하는 방식이며<br>
서버 PC에 소나큐브 서버가 올라가있다는 가정하에 만들어졌습니다.<br>


## 개발환경
- spring boot(2.4.4)
- java(11)
- maven(3.6.3)
- mybatis(2.1.6)
- mysql
- thymeleaf
- bootstrap
- jquery

## 기능
1. 회원가입, 로그인
2. Maven 파일 업로드
3. 소나큐브 분석
4. 대시보드 모니터링

## 사용시 주의사항
1. application.properties 파일에 datasource 설정
2. application.properties 파일에 spring.datasource.initialization-mode를 always로 변경(shcema.sql 파일 실행)
3. application.properties 파일에 파일다운로드 위치 및 소나큐브 정보 입력(url,id,password,token)
4. 소나큐브다운로드 및 실행(소나큐브 다운로드 링크 : https://www.sonarqube.org/)
