FROM java
MAINTAINER tanmay ambre
COPY ./target/aip-0.0.1-SNAPSHOT.jar /usr/local/aip/
RUN ls -al /usr/local/aip
RUN java -version
RUN echo $JAVA_HOME
EXPOSE 8082
VOLUME /logs
WORKDIR /usr/local/aip
ENTRYPOINT java -jar aip-0.0.1-SNAPSHOT.jar > /logs/aip.system.out.log

