CONTAINER_NAME="scrollo-back"
DIR="/root/inst"

ssh -l root 157.90.31.19 "cd $DIR && sudo rm -rf ./sources"

rsync -a --exclude "node_modules/*" ./ root@157.90.31.19:/root/inst/sources

ssh -l root 157.90.31.19 "cd $DIR/sources && docker build -t localhost:5000/$CONTAINER_NAME ./ && docker push localhost:5000/$CONTAINER_NAME && cd ../ && sudo rm -rf ./sources"

while getopts “c” OPTION
do
     case $OPTION in
         c)
             RERUN_COMPOSE=true
             ;;
         ?)
             exit
             ;;
     esac
done

if [ "$RERUN_COMPOSE" = true ]
   then
     ssh -l root 157.90.31.19 "cd $DIR && docker-compose down && docker-compose pull && docker-compose up -d"
 fi