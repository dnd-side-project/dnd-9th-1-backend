#!/bin/bash

CURRENT_PORT=$(cat /home/ubuntu/service_url.inc | grep -Po '[0-9]+' | tail -1)
TARGET_PORT=0

echo "> ${CURRENT_PORT} port currently proxying by nginx."

if[ ${CURRENT_PORT} -eq 8081]; then
  TARGET_PORT=8082;
elif [ ${CURRENT_PORT} -eq 8082]; then
  TARGET_PORT=8081;
else
  echo "> Currently Running WAS does not exist.";
  exit 1
fi

echo "set \$service_url http://dnd9th.site:${TARGET_PORT};" | tee /home/ubuntu/service_url.inc

echo "> ${CURRENT_PORT} port currently proxying by nginx after switching."

sudo service nginx reload;
echo "> Nginx reloaded"
