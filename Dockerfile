# Use an official Maven image with a JDK. Choose a version appropriate for your project.
FROM maven:3.8-openjdk-17 AS builder

WORKDIR /app

COPY pom.xml .
COPY settings.xml .

RUN mvn dependency:go-offline --settings settings.xml -B --X

COPY src ./src

# Run clean install to build the project and prepare resources
RUN mvn clean install --settings settings.xml -DskipTests

# Expose the port your application will listen on.
# Cloud Run will set the PORT environment variable, which your app should use.
EXPOSE 8080

# The command to run your application.
# You can have multiple agents in this directory and all of them will be available in the Dev UI.
ENTRYPOINT ["mvn", "exec:java", \
    "-Dexec.mainClass=com.google.adk.web.AdkWebServer", \
    "-Dexec.classpathScope=compile", \
    "-Dexec.args=--server.port=${PORT:8080} --adk.agents.source-dir=src/main/java" \
]