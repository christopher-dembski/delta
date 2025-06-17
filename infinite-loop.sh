# to inspect Docker container file-system, the container needs to be running
# can use this script to run infinite loop so can inspect files to troubleshoot build issues
# replace ENTRYPOINT in Docker file
# ENTRYPOINT ["sh", "./infinite-loop.sh"]

echo "Docker container running"

while true
do
  sleep 1
done
