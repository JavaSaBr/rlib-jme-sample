@echo off
java -jar -XX:-RestrictContended -XX:+AggressiveOpts -XX:+UseParallelGC -XX:+UseTLAB ./server.jar