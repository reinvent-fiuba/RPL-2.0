#!/bin/bash

set -x #echo on

mysql_pod=$1;

kubectl exec -it "$mysql_pod" -- mysqldump --all-databases --hex-blob -uroot -pBh2gujvNJOD7e9sa > all-databases.sql;
sed -i 1d all-databases.sql;
tar -zcvf all-databases.tar.gz all-databases.sql;