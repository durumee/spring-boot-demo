# github oauth2 기본 흐름

## Client Request

* github.com/login/oauth/authorize?client_id=[발급된_oauth2_client_id]
  * **스프링시큐리티OAuth2** 에서는 /oauth2/authorization/github 호출하면 처리됨

## Callback

* 위 url 호출 후 사용자가 승인하면 설정된 콜백url 호출됨
  * 스프링시큐리티에서 해당 경로를 permitAll 해야함
  * **스프링시큐리티OAuth2** 에서는 **application.yml** 내의 redirect-uri 를 다음처럼 설정하면 기본값으로 호환됨
    * redirect-uri: "{baseUrl}/login/oauth2/code/{registrationId}"
  * baseUrl 설명: 현재 서버의 기본 url
  * registrationId 설명: SecurityConfig.java 내의 ClientRegistration 할때 설정한 id
    * 예) ClientRegistration.withRegistrationId("github")

## Github User Api

* api.github.com/user 호출 시 승인된 토큰이 있는 경우 정보 조회 가능
