FOLDER=$(cat DEPLOY_PATH)
git pull
./sbt dist
DISTPATH=target/universal/
DISTNAME=$(basename $(find $DISTPATH -name "*.zip"))
mv $DISTPATH$DISTNAME $FOLDER
unzip $FOLDER$DISTNAME -d $FOLDER
rm -f $FOLDER$DISTNAME
