#!/bin/bash

echo "> Start run_new_was.sh"

# Parse port number from 'service_url.inc'
CURRENT_PORT=$(cat /home/ubuntu/service_url.inc | grep -Po '[0-9]+' | tail -1)
CURRENT_PROFILE=$(cat /home/ubuntu/profile.inc)
TARGET_PORT=0

echo "> Current port of running WAS is ${CURRENT_PORT}."

# Find target port to switch
if [ ${CURRENT_PORT} -eq 8081 ]; then
    TARGET_PORT=8082
elif [ ${CURRENT_PORT} -eq 8082 ]; then
    TARGET_PORT=8081ubu6
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
nohup sudo java -jar -Duser.timezone="Asia/Seoul" -Dspring.profiles.active=${CURRENT_PROFILE} -Dserver.port=${TARGET_PORT} /home/ubuntu/app/build/libs/backend-0.0.1-SNAPSHOT.jar > /home/ubuntu/nohup.out 2>&1 &
echo "> Now new WAS runs at profile : ${CURRENT_PROFILE}."
echo "> Now new WAS runs at port number : ${TARGET_PORT}."
exit 0