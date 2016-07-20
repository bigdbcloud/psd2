FROM java
MAINTAINER tanmay ambre
COPY ./target/pisp-0.0.1-SNAPSHOT.jar /usr/local/pisp/
RUN ls -al /usr/local/pisp
RUN java -version
RUN echo $JAVA_HOME
EXPOSE 8083
VOLUME /logs
WORKDIR /usr/local/pisp
ENTRYPOINT java -jar pisp-0.0.1-SNAPSHOT.jar > /logs/pisp.system.out.log
