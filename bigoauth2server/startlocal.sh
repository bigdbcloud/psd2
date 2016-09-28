#!/bin/bash

BIGOAUTH2SERVER_MONGO_SVC_URL=mongodb://172.30.46.38:27017/bigoauth2server
export BIGOAUTH2SERVER_MONGO_SVC_URL

echo $BIGOAUTH2SERVER_MONGO_SVC_URL

java -jar target/bigoauth2server-0.0.1-SNAPSHOT.jar > sys.log &

