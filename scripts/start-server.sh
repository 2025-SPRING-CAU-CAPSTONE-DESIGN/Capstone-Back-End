#!/bin/bash
set -eux

echo "================ 서버 배포 시작 ================"

# 필요한 환경변수(예: ECR 레포, AWS 계정 ID)를 환경에서 넘겨 주셨다면 그대로 사용
ECR_REGISTRY="905809970090.dkr.ecr.ap-northeast-2.amazonaws.com"

# 1) ECR 로그인 (ubuntu 사용자 권한으로)
aws ecr get-login-password --region ap-northeast-2 \
  | sudo docker login \
      --username AWS \
      --password-stdin $ECR_REGISTRY

# 2) 기존 컨테이너 제거
sudo docker stop storyforest-server || true
sudo docker rm   storyforest-server || true

# 3) 최신 이미지 pull & run
sudo docker pull $ECR_REGISTRY/storyforest-server:latest
sudo docker run -d \
  --name storyforest-server \
  -p 8080:8080 \
  $ECR_REGISTRY/storyforest-server:latest

echo "================ 서버 배포 끝 ================"
