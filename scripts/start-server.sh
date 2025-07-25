#!/bin/bash
set -eux

echo "--------------- 서버 배포 시작 -----------------"
sudo docker stop storyforest-server || true
sudo docker rm   storyforest-server || true
sudo docker pull 905809970090.dkr.ecr.ap-northeast-2.amazonaws.com/storyforest-server:latest
sudo docker run -d --name storyforest-server -p 8080:8080 \
  905809970090.dkr.ecr.ap-northeast-2.amazonaws.com/storyforest-server:latest
echo "--------------- 서버 배포 끝 -----------------"
