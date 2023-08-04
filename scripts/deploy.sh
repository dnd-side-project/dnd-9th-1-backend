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
fi

TARGET_PID=$(lsof -Fp -i TCP:${TARGET_PORT} | grep -Po 'p[0-9]+' | grep -Po '[0-9]+')

if[ ! -z ${TARGET_PID}]; then
  echo "> Exiting WAS for port ${TARGET_PORT}."
  sudo kill ${TARGET_PID}
fi

source ~/.bash_profile

nohop java -jar \
  -Dspring.profiles.active=prod \
  -Dserver.port=${TARGET_PORT} \
  /home/ubuntu/app/build/libs/*.SNAPSHOT.jar > /home/ubuntu/nohup.out 2>&1 &
echo "> A new WAS for ${TARGET_PORT} is running."
exit 0
