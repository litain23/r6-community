#!/bin/bash

CODE=$(aws deploy get-deployment --deployment-id d-GAW77VHU4 | python -c "import sys, json; print(json.load(sys.stdin)['deploymentInfo']['status'])")
echo $CODE


