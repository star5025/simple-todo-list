# 使用最通用的Java镜像
FROM openjdk:17
WORKDIR /app
COPY . .
RUN chmod +x ./mvnw
RUN ./mvnw clean package -DskipTests
EXPOSE 8088
ENTRYPOINT ["java", "-jar", "target/back-end-0.0.1-SNAPSHOT.jar"]
