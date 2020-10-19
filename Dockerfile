# you can copy/paste this content as-is
FROM maven:3.5.0-jdk-8
WORKDIR /apps
COPY . /apps
RUN mvn clean install
CMD mvn exec:java -Dexec.mainClass="application.Battle"