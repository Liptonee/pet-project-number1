# Используем официальный образ OpenJDK 21 (LTS)
FROM eclipse-temurin:25-jre-alpine

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем jar-файл (предварительно собрать через mvn clean package)
COPY target/taskManager-0.0.1-SNAPSHOT.jar app.jar

# Открываем порт, который слушает приложение (по умолчанию 8080)
EXPOSE 8080

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"]