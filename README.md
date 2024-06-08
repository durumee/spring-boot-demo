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
- Swagger UI
  - application.yml 내의 구성 및
  - MemberRestController.java 구성 참고
  - SecurityConfig 에 url 필터 처리도 참고
  - index.html 에 swagger ui 링크 있음
