FROM java
MAINTAINER tanmay ambre
VOLUME /logs
EXPOSE 8085
WORKDIR /usr/local/cashewapi
COPY ./target/cashewapi-0.0.1-SNAPSHOT.jar /usr/local/cashewapi/
ENTRYPOINT java -jar cashewapi-0.0.1-SNAPSHOT.jar > /logs/cashewapi.system.out.log
