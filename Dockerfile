# 베이스 이미지 선택
FROM openjdk:17-jdk-alpine

# JAR 파일 위치 설정
ARG JAR_FILE=./build/libs/*.jar

# JAR 파일을 컨테이너에 복사
COPY ${JAR_FILE} app.jar

# 애플리케이션 실행 명령어
ENTRYPOINT ["java", "-jar", "/app.jar"]
