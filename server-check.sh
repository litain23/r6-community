#!/bin/bash

echo "Server deploy test"
for RETRY_COUNT in {1..15}
do
  RESPONSE_CODE=$(curl -I http://127.0.0.1:8080/api/c/topic/free | head -n 1 | grep '200')
  if [ -z "$RESPONSE_CODE" ]; then
    echo "RETRY TEST";
  else
    echo "Server deploy complete"
    break;
  fi

  if [ $RETRY_COUNT -eq 10 ]
  then
    echo "Server deploy fail"
    exit 1
  fi
  sleep 10;
done
exit 0

