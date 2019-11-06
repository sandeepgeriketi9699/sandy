FROM frolvlad/alpine-oraclejdk8:slim
ADD ["target/iaassmssecurity.war", "security.jar"]
EXPOSE 8080 8060
ENV JAVA_OPTS="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=8060,suspend=n"
RUN sh -c 'touch /security.jar'
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=docker -jar /security.jar" ]
