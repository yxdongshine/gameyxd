#!/bin/bash
if [ -e log ];then
	echo "log is exist"
else
	mkdir log
fi
JAVA=java
jar=GameStart.jar
MAIN=com.lx.game.start.GameStart
libs=./*

"$JAVA" -cp "$libs":"$jar" "$MAIN" -Dfile.encoding=UTF-8 >log/login.log  2>> log/login.log &
pid=$!
echo "$pid" > pid.GameStart
echo "$pid"