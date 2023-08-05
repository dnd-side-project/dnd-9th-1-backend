#!/bin/bash

cd /home/ubuntu/app
DOCKER_APP_NAME=milestone

EXIST_BLUE=$(sudo docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml ps | grep Up)

if [ -z "$EXIST_BLUE" ]; then
  echo "> blue up"
  sudo docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml up -d --build
  BEFORE_COMPOSE_COLOR="green"
  AFTER_COMPOSE_COLOR="blue"
else
  echo "> green up"
  sudo docker-compose -p ${DOCKER_APP_NAME}-green -f docker-compose.green.yml up -d --build
  BEFORE_COMPOSE_COLOR="blue"
  AFTER_COMPOSE_COLOR="green"
fi

sleep 10

EXIST_AFTER=$(sudo docker-compose -p ${DOCKER_APP_NAME}-${AFTER_COMPOSE_COLOR} -f docker-compose.${AFTER_COMPOSE_COLOR}.yml ps | grep UP)

if [ -n "$EXIST_AFTER" ]; then
  echo "> nginx reload .. "
  NGINX_CONT=$(docker ps -q --filter ancestor=nginx)
  sudo docker exec -it "$NGINX_CONT" bash -c "cp /etc/nginx/nginx.${AFTER_COMPOSE_COLOR}.conf /etc/nginx/nginx.conf;nginx -s reload"

  sudo docker-compose -p ${DOCKER_APP_NAME}-${BEFORE_COMPOSE_COLOR} -f docker-compose.${BEFORE_COMPOSE_COLOR}.yml down
  sudo docker rm $(docker ps -q -a -f "name=${BEFORE_COMPOSE_COLOR}")
  echo "$BEFORE_COMPOSE_COLOR down"
fi

echo "> Start removing existing image"
sudo docker image prune -af