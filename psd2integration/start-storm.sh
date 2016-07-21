#!/bin/bash

export STORM_HOME=/usr/local/digiplat/software/apache-storm-1.0.1/

echo $STORM_HOME

$STORM_HOME/bin/storm jar /media/sf_psd2/psd2/psd2integration/target/psd2integration-1.0.1-jar-with-dependencies.jar com.ibm.psd2.integration.App \
--zookeeper.host inmbz2239.in.dst.ibm.com:2181 \
--kafka.topic psd2payments \
--mongodb.host mongodb://inmbz2239.in.dst.ibm.com:27017 \
--job.submission.type storm  \
--psd2api.db psd2api \
--payments.collection payments \
--bankaccounts.collection bankaccounts \
--transactions.collection transactions

