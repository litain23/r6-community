REPOSITORY=/home/ubuntu/codedeploy
PROJECT_NAME=r6-community

cd $REPOSITORY/$PROJECT_NAME

chmod +x ./gradlew

CURRENT_PID=$(pgrep -f r6-community-*.jar)

echo "RUNNING PID : $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
  echo "NOT FOUND RUNNING APP"
else
  echo "KILL RUNNING APP"
  sudo kill -15 $CURRENT_PID
  sleep 5
fi

echo "START NEW BUILD FILE"

JAR_NAME=$(ls -tr $REPOSITORY/$PROJECT_NAME | grep *.jar | tail -n 1)

sudo nohup java -jar \
  -Dspring.config.location=classpath:/application.properties,/home/ubuntu/codedeploy/r6-community/application-real-db.properties \
  -Dspring.profiles.active=real \
  $REPOSITORY/$PROJECT_NAME/$JAR_NAME 2>&1 &

