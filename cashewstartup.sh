#!/bin/bash

export MOUNTPATH=/data/db

adduser mongodb root
#adduser elasticsearch root

chmod -R 777 $MOUNTPATH
chmod -R 777 /logs
chmod -R 777 /data/configdb
chmod -R 777 /usr/share/elasticsearch/data
chmod -R 777 /
chmod -R 777 /usr/local/cashew/elastic.sh

echo $MOUNTPATH

ls -al $MOUNTPATH
ls -al /logs
ls -al /data/configdb

set -e

#echo "Starting mongodb logs available at: /logs/mongodb.log"
#exec mongod --fork --logpath /logs/mongodb.log
#echo "API Server logs available at: /logs"
#exec java -jar /usr/local/mcabuddyapp/mcabuddyapi-beta.jar

#elasticsearch -Des.insecure.allow.root=true & mongod --logpath /logs/mongodb.log & java -jar /usr/local/cashew/cashewapi-0.0.1-SNAPSHOT.jar & java -jar /usr/local/cashew/bigoauth2server-beta.jar
/usr/local/cashew/elastic.sh elasticsearch & mongod --logpath /logs/mongodb.log & java -jar /usr/local/cashew/cashewapi-0.0.1-SNAPSHOT.jar & java -jar /usr/local/cashew/bigoauth2server-beta.jar

