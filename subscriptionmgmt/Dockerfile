FROM java
MAINTAINER tanmay ambre
COPY ./target/subscriptionmgmt-0.0.1-SNAPSHOT.jar /usr/local/subscriptionmgmt/
RUN ls -al /usr/local/subscriptionmgmt
RUN java -version
RUN echo $JAVA_HOME
EXPOSE 8084
VOLUME /logs
WORKDIR /usr/local/subscriptionmgmt
ENTRYPOINT java -jar subscriptionmgmt-0.0.1-SNAPSHOT.jar > /logs/subscriptionmgmt.system.out.log
