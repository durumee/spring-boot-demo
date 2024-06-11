## (작업중)

- H2 DB 를 도커 기반 MySQL 로 변경 중
- docker build 테스트
  - > docker build --progress=plain --no-cache -t demo3:latest .
- docker compose 테스트
  - > docker-compose up --build -d
- mysqld, mongodb 구동 후 부트를 시작하는게 초기 DB 구성에 필요하여  wait-for-it.sh 사용됨
  - > bash 가 필요해서 bash도 추가됨
  - > 도커 내 리눅스 구동 시 쉘 파일 개행문자는 LF 여야 문제없이 동작


## 이 브랜치의 구성은...

> RDB, NoSQL 구성에 대한 예제
> > 컨트롤러에 적용된 Swagger UI 의 예제
> > 간략한 스프링 시큐리티 권한 처리만 적용

- Spring Data
  - (개발) H2 DB, JPA
    - Member* 클래스가 예제
    - Swagger UI 구성 예제 적용됨
  - MongoDB (로컬 서버 구성이 된 경우만 로딩)
    - User* 클래스가 예제
  - --spring.profiles.active=dev
    - 로컬 구동 옵션 설정 시
    - DataInitializer.java 에서 테스트 데이터 생성
- Swagger UI
  - application.yml 내의 구성 및
  - MemberRestController.java 구성 참고
  - SecurityConfig 에 url 필터 처리도 참고
  - index.html 에 swagger ui 링크 있음

> MongoDB docker 활용 설치
> > 1. docker 설치
> >    * 윈도우의 경우 wsl2 설치 필요
> > 2. 윈도우인 경우 docker desktop 실행하고 명령 입력
> >    * docker run --name my-mongo -d -p 27017:27017 mongo:latest
> >    * Mongo GUI 클라이언트 링크
> >      * https://www.mongodb.com/try/download/compass?jmp=docs