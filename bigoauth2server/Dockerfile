FROM java
MAINTAINER tanmay ambre
COPY ./target/bigoauth2server-0.0.1-SNAPSHOT.jar /usr/local/bigoauth2server/
VOLUME /logs
EXPOSE 11113
WORKDIR /usr/local/bigoauth2server
ENTRYPOINT java -jar bigoauth2server-0.0.1-SNAPSHOT.jar > /logs/bigoauth2server.system.out.log
