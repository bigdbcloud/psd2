FROM java
MAINTAINER tanmay ambre
RUN java -version
RUN echo $JAVA_HOME
RUN ls -al /usr/lib/jvm/java-8-openjdk-amd64/jre/lib/security/
COPY ./src/main/resources/oauth2server.crt /usr/local/psd2api/
RUN /usr/lib/jvm/java-8-openjdk-amd64/bin/keytool -import -alias oauth2 -v -noprompt -file /usr/local/psd2api/oauth2server.crt -keystore /usr/lib/jvm/java-8-openjdk-amd64/jre/lib/security/cacerts -storepass changeit -keypass password01
RUN ls -altr /etc/ssl/certs/java/cacerts
EXPOSE 11112
VOLUME /logs
WORKDIR /usr/local/psd2api
COPY ./target/psd2api-0.0.1-SNAPSHOT.jar /usr/local/psd2api/
RUN ls -al /usr/local/psd2api
ENTRYPOINT java -jar psd2api-0.0.1-SNAPSHOT.jar > /logs/psd2api.system.out.log
