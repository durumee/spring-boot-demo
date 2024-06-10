## 목적

- 기본 세션방식 formLogin 인증 설정이 되어 있음
- 기본 웹 컨트롤러 동작 및 정적리소스 서버
- Dockerfile 파일 추가
  - docker desktop 설치
  - 스프링부트 빌드 (gradle 태스크 bootJar 로 실행)
  - 도커 이미지 생성 (프로젝트 루트, Dockerfile 파일이 있는 위치)
    > docker build -t demo:latest .
  - 도커 이미지 확인
    > docker image ls
  - 도커 컨테이너 실행
    > docker run -d --name demo1 -p 5000:8080 demo:latest
  - 모든 컨테이너 목록
    > docker ps -a<br>
    `# -a 없으면 실행중인 컨테이너만 확인`
  - 컨테이너 시작 (docker run 이후)
    > docker start demo1<br>
    `# -a attach stream (콘솔 출력)`<br>
    docker start -a demo1
  - 컨테이너 재시작
    > docker restart demo1
  - 컨테이너 삭제
    > docker rm demo1

- 기타 설명
  - docker build
    - > -t
      - 이미지에 태그를 설정하는 옵션 (-t)
    - > demo:latest
      - app이름:버전 (1.0, 1.1 등도 가능, latest는 최신을 의미)
  - docker run
    - > -d
      - daemon (백그라운드로 실행)
    - > --name demo1
      - 컨테이너 이름
    - > -p 5000:8080
      - 외부포트:내부포트 (내부란 스프링부트 자체 설정 포트)