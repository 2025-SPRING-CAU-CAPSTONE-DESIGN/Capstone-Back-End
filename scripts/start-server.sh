#!/bin/bash
set -eux

echo "===== 서버 배포 시작 ====="

AWS_ACCOUNT_ID=905809970090
REGION=ap-northeast-2
ECR_REGISTRY="${AWS_ACCOUNT_ID}.dkr.ecr.${REGION}.amazonaws.com"

# ← 이 부분을 추가!
echo "로그인: $ECR_REGISTRY"
aws ecr get-login-password --region $REGION \
  | sudo docker login \
      --username AWS \
      --password-stdin $ECR_REGISTRY

echo "컨테이너 정리..."
sudo docker stop storyforest-server || true
sudo docker rm   storyforest-server || true

echo "이미지 풀 & 실행..."
sudo docker pull $ECR_REGISTRY/storyforest-server:latest
sudo docker run -d --name storyforest-server -p 8080:8080 \
    $ECR_REGISTRY/storyforest-server:latest

echo "===== 서버 배포 끝 ====="
