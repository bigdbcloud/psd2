#!/bin/bash

export MOUNTPATH=/data/db

adduser mongodb root

chmod -R 777 $/data/db
chmod -R 777 /logs
chmod -R 777 /data/configdb

echo $MOUNTPATH

ls -al $MOUNTPATH
ls -al /logs
ls -al /data/configdb

set -e


#echo "Starting mongodb logs available at: /logs/mongodb.log"
#exec mongod --fork --logpath /logs/mongodb.log
#echo "API Server logs available at: /logs"
#exec java -jar /usr/local/mcabuddyapp/mcabuddyapi-beta.jar

mongod --logpath /logs/mongodb.log & java -jar /usr/local/psd2/bigbankoauth2server-0.0.1-SNAPSHOT.jar &  java -jar /usr/local/psd2/psd2api-0.0.1-SNAPSHOT.jar

