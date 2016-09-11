#!/bin/bash

export STORM_HOME=/usr/local/apache-storm-1.0.1/

echo $STORM_HOME

$STORM_HOME/bin/storm jar /media/sf_digital/psd2/master/psd2/psd2integration/target/psd2integration-1.0.1-jar-with-dependencies.jar com.ibm.psd2.integration.App \
--zookeeper.host 172.30.77.154:2181 \
--kafka.topic psd2payments \
--mongodb.host mongodb://172.30.46.38:27017 \
--job.submission.type storm  \
--psd2api.db psd2api \
--payments.collection TxnRequests \
--bankaccounts.collection BankAccountDetails \
--transactions.collection Transactions

