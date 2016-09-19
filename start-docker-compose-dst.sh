#!/bin/bash

export ENV=DST
/usr/local/docker/docker-compose stop
/usr/local/docker/docker-compose up -d
