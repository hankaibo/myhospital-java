FROM openjdk:11
ADD ./target/myhospital-0.0.1.RELEASE.jar app.jar
ENTRYPOINT java ${JAVA_OPTS}  -jar app.jar