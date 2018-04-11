#! /bin/bash

PORT_LOCAL="${PORT:=9000}"

echo "running on port $PORT_LOCAL"

docker run \
  -it \
  --rm \
  -p $PORT_LOCAL:$PORT_LOCAL \
  -e PORT=$PORT_LOCAL \
  $1
