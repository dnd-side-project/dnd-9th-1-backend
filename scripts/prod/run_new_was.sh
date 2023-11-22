#!/bin/bash

echo "> Start run_new_was.sh"

# Parse port number from 'prod_url.inc'
CURRENT_PORT=$(cat /home/ubuntu/prod_url.inc | grep -Po '[0-9]+' | tail -1)
CURRENT_PROFILE=prod
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
echo "> Kill WAS running at ${TARGET_PORT}."
sudo kill $(sudo lsof -t -i:${TARGET_PORT})

# run jar file in background
nohup sudo java -jar -Duser.timezone="Asia/Seoul" -Dspring.profiles.active=${CURRENT_PROFILE} -Dserver.port=${TARGET_PORT} /home/ubuntu/prod/build/libs/backend-0.0.1-SNAPSHOT.jar > /home/ubuntu/prod/nohup.out 2>&1 &
echo "> Now new WAS runs at profile : ${CURRENT_PROFILE}."
echo "> Now new WAS runs at port number : ${TARGET_PORT}."
exit 0