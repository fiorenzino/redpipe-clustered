#!/usr/bin/env bash

echo "                  __      _           ";
echo "   ________  ____/ /___  (_)___  ___  ";
echo "  / ___/ _ \/ __  / __ \/ / __ \/ _ \ ";
echo " / /  /  __/ /_/ / /_/ / / /_/ /  __/ ";
echo "/_/   \___/\__,_/ .___/_/ .___/\___ / ";
echo "               /_/     /_/            ";

set -e

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

rm -fr docker/fatdocker/lib/*
rm -fr docker/fatdocker/redpipes/*
rm -fr docker/fatdocker/conf/*

mkdir -p docker/fatdocker/lib
mkdir -p docker/fatdocker/redpipes
mkdir -p docker/fatdocker/conf

mvn package -Dmaven.test.skip=true -Dmaven.javadoc.skip=true -Dsource.skip=true dependency:copy-dependencies -B -T4C


cp -r api-gateway/target/dependency/* docker/fatdocker/lib/
cp -r api-gateway/conf/api-gateway.json docker/fatdocker/conf/
cp -r api-gateway/target/api-gateway-0.0.2-SNAPSHOT.jar docker/fatdocker/redpipes/

cp -r clubs/target/dependency/* docker/fatdocker/lib/
cp -r clubs/conf/clubs.json docker/fatdocker/conf/
cp -r clubs/target/clubs-0.0.2-SNAPSHOT.jar docker/fatdocker/redpipes/

cp -r users/target/dependency/* docker/fatdocker/lib/
cp -r users/conf/users.json docker/fatdocker/conf/
cp -r users/target/users-0.0.2-SNAPSHOT.jar docker/fatdocker/redpipes/


docker build -t "docker.flw.ovh/redpipe/redpipe-clustered" "$DIR/../docker/fatdocker/"

docker tag docker.flw.ovh/redpipe/redpipe-clustered docker.flw.ovh/redpipe/redpipe-clustered:0.0.2-SNAPSHOT


