FROM frolvlad/alpine-oraclejdk8:slim
ADD ["target/multitenantservice.war", "multitenant.jar"]
EXPOSE 8080 8040
ENV JAVA_OPTS="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=8040,suspend=n"
RUN sh -c 'touch /multitenant.jar'
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=docker -jar /multitenant.jar" ]
