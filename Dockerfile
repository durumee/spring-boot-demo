# 베이스 이미지 선택
FROM openjdk:17-jdk-alpine as build

# 패키지 업데이트 및 필요한 패키지 설치
#RUN apt-get update && apt-get install -y passwd

# appuser 사용자 및 그룹 생성
#RUN groupadd -r appuser && useradd -r -g appuser appuser

# 작업 디렉토리 생성 및 소유자 변경
#RUN mkdir /app && chown appuser:appuser /app

# appuser로 변경
#USER appuser

# 작업 디렉토리 설정
WORKDIR /app

# Gradle Wrapper와 필요한 파일 복사
COPY gradlew /app/gradlew
COPY gradlew.bat /app/gradlew.bat
COPY gradle /app/gradle
COPY build.gradle /app/build.gradle
COPY settings.gradle /app/settings.gradle
COPY src /app/src

# 실행 권한 설정
RUN chmod +x /app/gradlew

# 디버깅 목적으로 파일과 권한 확인
#RUN sh -c 'ls -l /app && sleep 1d'
RUN ls -l /app

# Gradle 빌드 실행
RUN /app/gradlew clean bootJar --stacktrace

# 실행 단계
FROM openjdk:17-jdk-alpine

# 작업 디렉토리 설정
WORKDIR /app

# 빌드된 JAR 파일을 복사
COPY --from=build /app/build/libs/*.jar /app/app.jar

# 애플리케이션 실행 명령어
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
