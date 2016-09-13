FROM java
MAINTAINER tanmay ambre
COPY ./target/psd2subscriptionapi-0.0.1-SNAPSHOT.jar /usr/local/psd2subscriptionapi/
RUN ls -al /usr/local/psd2subscriptionapi
RUN java -version
RUN echo $JAVA_HOME
RUN ls -al /usr/lib/jvm/java-8-openjdk-amd64/jre/lib/security/
COPY ./src/main/resources/oauth2server.crt /usr/local/psd2subscriptionapi/
RUN /usr/lib/jvm/java-8-openjdk-amd64/bin/keytool -import -alias oauth2 -v -noprompt -file /usr/local/psd2subscriptionapi/oauth2server.crt -keystore /usr/lib/jvm/java-8-openjdk-amd64/jre/lib/security/cacerts -storepass changeit -keypass password01
RUN ls -altr /etc/ssl/certs/java/cacerts
EXPOSE 8082
VOLUME /logs
WORKDIR /usr/local/psd2subscriptionapi
ENTRYPOINT java -jar psd2subscriptionapi-0.0.1-SNAPSHOT.jar > /logs/psd2subscriptionapi.system.out.log
