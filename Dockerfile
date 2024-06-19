# 베이스 이미지 선택
FROM openjdk:17-jdk-alpine as build

# 작업 디렉토리 설정
WORKDIR /app

# Gradle Wrapper와 필요한 파일 복사
COPY gradlew /app/gradlew
COPY gradle /app/gradle
COPY build.gradle /app/build.gradle
COPY settings.gradle /app/settings.gradle
COPY src /app/src

# 실행 권한 설정
RUN chmod +x /app/gradlew

# dos2unix 설치 및 줄바꿈 변경
# 윈도우10+WSL2+DockerDesktop 기준: gradlew not found 등의 메시지가 나와서 내부 빌드 시 개행문자 처리를 하니 해결됨
RUN apk add --no-cache dos2unix
RUN dos2unix /app/gradlew

# Gradle Wrapper를 실행하여 필요한 파일 다운로드 (다운로드 캐시를 위해 추가)
RUN /app/gradlew --version

# Gradle 빌드 실행
RUN /app/gradlew clean bootJar

# 실행 단계
FROM openjdk:17-jdk-alpine

# 작업 디렉토리 설정
WORKDIR /app

# 빌드된 JAR 파일을 복사
COPY --from=build /app/build/libs/*.jar /app/app.jar

# bash 설치는 wait-for-it 스크립트 동작이 필요해서
RUN apk add --no-cache bash

# wait-for-it 스크립트를 복사하고 실행 권한 부여
COPY wait-for-it.sh /app/wait-for-it.sh
RUN chmod +x /app/wait-for-it.sh

# 빌드 인수 받기
ARG PROFILE

# 환경 변수 설정
ENV SPRING_PROFILES_ACTIVE=${PROFILE}

# 애플리케이션 실행 명령어
ENTRYPOINT ["/bin/bash", "/app/wait-for-it.sh", "dev-mysql:3306", "--", "/bin/bash", "/app/wait-for-it.sh", "dev-mongodb:27017", "--", "java", "-jar", "/app/app.jar", "--spring.profiles.active=${SPRING_PROFILES_ACTIVE}"]
