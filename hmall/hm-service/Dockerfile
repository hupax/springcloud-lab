# 使用JDK 21的官方镜像
FROM openjdk:21-slim

# 设定时区为上海（如果有需要）
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 将应用程序的JAR包复制到容器中
COPY hm-service.jar /app.jar


# 启动应用程序，并为容器设置JVM的容器感知参数
ENTRYPOINT ["java", "-jar", "/app.jar"]
