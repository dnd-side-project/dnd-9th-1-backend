#!/bin/bash

# Crawl current connected port of WAS
CURRENT_PORT=$(cat /home/ubuntu/dev_url.inc  | grep -Po '[0-9]+' | tail -1)
TARGET_PORT=0

echo "> Nginx currently proxies to ${CURRENT_PORT}."

# Toggle port number
if [ ${CURRENT_PORT} -eq 8083 ]; then
    TARGET_PORT=8084
elif [ ${CURRENT_PORT} -eq 8084 ]; then
    TARGET_PORT=8083
else
    echo "> No WAS is connected to nginx"
    exit 1
fi

# Change proxying port into target port
echo "set \dev_url http://127.0.0.1:${TARGET_PORT};" | tee /home/ubuntu/dev_url.inc

echo "> Now nginx proxies to ${TARGET_PORT}."

# Reload nginx
sudo service nginx reload

echo "> Nginx reloaded."