FROM java
MAINTAINER tanmay ambre
COPY ./target/psd2demoapp-0.0.1-SNAPSHOT.jar /usr/local/psd2demoapp
RUN ls -al /usr/local/psd2demoapp
RUN java -version
RUN echo $JAVA_HOME
EXPOSE 8084
VOLUME /logs
WORKDIR /usr/local/psd2demoapp
ENTRYPOINT java -jar psd2demoapp-0.0.1-SNAPSHOT.jar
