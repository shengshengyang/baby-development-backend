# Runtime-only image for mvc module using prebuilt jar
FROM eclipse-temurin:17-jre
ENV TZ=Asia/Taipei
WORKDIR /app

# Copy the prebuilt jar from local workspace
COPY mvc/target/*-mvc-*.jar /app/app.jar

EXPOSE 8084
ENTRYPOINT ["java","-XX:MaxRAMPercentage=75","-Dfile.encoding=UTF-8","-Duser.timezone=Asia/Taipei","-jar","/app/app.jar"]
