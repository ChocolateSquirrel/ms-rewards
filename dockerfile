FROM openjdk:8
EXPOSE 7070
COPY build/libs/ms-rewards-1.0.0.jar rewards.jar
ENTRYPOINT ["java", "-jar", "/rewards.jar"]