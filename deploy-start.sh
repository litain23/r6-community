#!/bin/bash

DEPLOYMENT_ID=$(aws deploy create-deployment \
  --application-name codedeploy-from-s3 \
  --deployment-group-name codedeploy-from-s3 \
  --s3-location bucket=${{ secrets.AWS_S3_BUCKET_NAME }},key=community-build/buildFile.zip,bundleType=zip)

echo $DEPLOYMENT_ID | jq '.deploymendId'

#sleep 60
#
#IS_SUCCESS=$(aws deploy --deployment-id $DEPLOYMENT_ID | python -c "import sys, json; print("json.load(sys.stdin)['deploymentInfo']['status'])")
#
#if [ "$IS_SUCCESS" = "Succeeded" ];
#then
#  echo "SUCCESS DEPLOY!"
#else
#  echo "FAIL DEPLOY!"
#fi