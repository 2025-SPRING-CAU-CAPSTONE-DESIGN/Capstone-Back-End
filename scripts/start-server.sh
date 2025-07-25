#!/bin/bash

echo "--------------- 서버 배포 시작 -----------------"
docker stop storyforest-server || true
docker rm storyforest-server || true
docker pull 905809970090.dkr.ecr.ap-northeast-2.amazonaws.com/storyforest-server:latest
docker run -d --name storyforest-server -p 8080:8080 905809970090.dkr.ecr.ap-northeast-2.amazonaws.com/storyforest-server:latest
echo "--------------- 서버 배포 끝 -----------------"