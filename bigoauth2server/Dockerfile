FROM java
MAINTAINER tanmay ambre
COPY ./target/bigoauth2server-beta.jar /usr/local/bigoauth2server/
VOLUME /logs
EXPOSE 8084
WORKDIR /usr/local/bigoauth2server
ENTRYPOINT java -jar bigoauth2server-beta.jar > /logs/bigoauth2server.system.out.log
