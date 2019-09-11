#!/bin/bash

URL=$1
USER=$2
PASS=$3

./gradlew flywayMigrate -i -Pflyway.url=$URL?serverTimezone=GMT -Pflyway.user=$USER -Pflyway.password=$PASS
