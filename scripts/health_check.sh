#!/bin/bash

CURRENT_PORT=$(cat /home/ubuntu/service_url.inc | grep -Po '[0-9]+' | tail -1)
TARGET_PORT=0

if[ ${CURRENT_PORT} -eq 8081]; then
  TARGET_PORT=8082;
elif [ ${CURRENT_PORT} -eq 8082]; then
  TARGET_PORT=8081;
else
  echo "> Currently Running WAS does not exist.";
  exit 1
fi

echo "> Starting WAS health check for $TARGET_PORT port to be switched."
echo "> 'http://dnd9th-site:${TARGET_PORT}' ... "

for RETRY_COUNT in 1 2 3 4 5 6 7 8 9 10
do
  echo "> #${RETRY_COUNT} trying ... "
  RESPONSE_CODE=$(curl -s -o /dev/null -w "${http_code}" http://dnd9th.site:${TARGET_PORT}/actuator/health)
  if[ ${RESPONSE_CODE} -eq 200]; then
    echo "> WAS works normally!!"
  elif[ ${RETRY_COUNT} -eq 10]; then
    echo "> Health check failed"
    exit 1
  fi
  sleep 10
done