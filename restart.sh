FOLDER=$(cat DEPLOY_PATH)
PID_FILE="${FOLDER}RUNNING_PID"
kill -9 $(cat $PID_FILE) || true
rm $PID_FILE || true
COMMAND="${FOLDER}scienceprovider-$1/bin/scienceprovider -Dpidfile.path=${FOLDER}RUNNING_PID -Dplay.http.secret.key=$2"
nohup $COMMAND
