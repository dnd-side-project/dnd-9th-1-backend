#!/bin/bash

echo "> Start run_new_was.sh"

# Parse port number from 'service_url.inc'
CURRENT_PORT=$(cat /home/ubuntu/service_url.inc | grep -Po '[0-9]+' | tail -1)
TARGET_PORT=0

echo "> Current port of running WAS is ${CURRENT_PORT}."

# Find target port to switch
if [ ${CURRENT_PORT} -eq 8081 ]; then
    TARGET_PORT=8082
elif [ ${CURRENT_PORT} -eq 8082 ]; then
    TARGET_PORT=8081
else
    echo "> No WAS is connected to nginx"
fi

# query pid using the TCP protocol and using the port 'TARGET_PORT'
# TARGET_PID=$(lsof -Fp -i TCP:${TARGET_PORT} | grep -Po 'p[0-9]+' | grep -Po '[0-9]+')

# if [ ! -z ${TARGET_PID} ]; then
#  echo "> Kill WAS running at ${TARGET_PORT}."
#  sudo kill ${TARGET_PID}
# fi

echo "> Kill WAS running at ${TARGET_PORT}."
sudo kill $(sudo lsof -t -i:${TARGET_PORT})

# run jar file in background
nohup sudo java -jar -Dspring.profiles.active=dev -Dserver.port=${TARGET_PORT} /home/ubuntu/app/build/libs/backend-0.0.1-SNAPSHOT.jar > /home/ubuntu/nohup.out 2>&1 &
echo "> Now new WAS runs at ${TARGET_PORT}."
exit 0