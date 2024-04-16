#!/bin/bash

source ./var.sh
echo "> env variable setting complete"

touch crontab_delete
crontab crontab_delete
rm crontab_delete
echo "> cron delete complete"

chmod u+x ${HOME}/server/gradlew
echo "> gradlew 실행 권한 부여"

cd ${HOME}/server/src/test
rm -rf java/
cd
echo "> 테스트 코드 임시 삭제"

cd ${HOME}/server
./gradlew build
echo "> build 시작"

echo "> build 파일명: $JAR_NAME" >> ${HOME}/log.out

echo "> 현재 실행중인 애플리케이션 pid 확인" >> ${HOME}/log.out

if [ -z $CURRENT_PID ]
then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다." >> ${HOME}/log.out
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 3
fi

echo "> 배포" >> ${HOME}/log.out
nohup java -jar -Dspring.config.location=classpath:/application-prod.properties,${HOME}/properties/application-oauth.properties,${HOME}/properties/application-db.properties,${HOME}/properties/application-mail.properties,${HOME}/properties/application-s3.properties $DEPLOY_JAR >> ${HOME}/log.out 2>${HOME}/err.out &

touch crontab_new
echo "* * * * * ${HOME}/check-and-restart2.sh" 1>>crontab_new
crontab crontab_new
rm crontab_new