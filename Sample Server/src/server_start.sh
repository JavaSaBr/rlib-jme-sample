#!/bin/sh

ROOT_PATH=$0
ROOT_PATH=${ROOT_PATH%/*}

cd "$ROOT_PATH/";

java -jar -XX:-RestrictContended -XX:+AggressiveOpts -XX:+UseParallelGC -XX:+UseTLAB ./server.jar