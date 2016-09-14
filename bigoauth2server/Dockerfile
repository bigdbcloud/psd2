FROM java
MAINTAINER tanmay ambre
COPY ./target/oauth2server-beta.jar /usr/local/oauth2server/
RUN ls -al /usr/local/oauth2server
RUN java -version
EXPOSE 8084
WORKDIR /usr/local/oauth2server
ENTRYPOINT java -jar oauth2server-beta.jar
