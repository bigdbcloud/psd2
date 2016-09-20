#!/bin/bash

ELASTICSEARCH_HOST=172.30.174.24
ELASTICSEARCH_PORT=9300
PSD2_URL=http://172.30.91.54:8082/psd2api
MONGODB_URI=mongodb://172.30.46.38:27017/psd2apirise

export ELASTICSEARCH_HOST
export ELASTICSEARCH_PORT
export MONGODB_URI
export PSD2_URL

echo $MONGODB_URI

java -jar target/cashewapi-0.0.1-SNAPSHOT.jar > sys.log &

