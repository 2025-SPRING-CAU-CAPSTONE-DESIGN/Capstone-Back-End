#!/bin/bash

echo "--------------- 서버 배포 시작 -----------------"
docker stop storyforest-server || true
docker rm storyforest-server || true
docker pull {ECR Repository 주소}/storyforest-server:latest
docker run -d --name storyforest-server -p 8080:8080 {ECR Repository 주소}/storyforest-server:latest
echo "--------------- 서버 배포 끝 -----------------"