FROM java
MAINTAINER tanmay ambre
COPY ./target/bigbankoauth2server-0.0.1-SNAPSHOT.jar /usr/local/bigbankoauth2server/
RUN ls -al /usr/local/bigbankoauth2server
RUN java -version
RUN which java
RUN ls -al $JAVA_HOME/jre/lib/security
EXPOSE 8081
VOLUME /logs
WORKDIR /usr/local/bigbankoauth2server
ENTRYPOINT java -jar bigbankoauth2server-0.0.1-SNAPSHOT.jar > /logs/bigbankoauth2server.system.out.log
