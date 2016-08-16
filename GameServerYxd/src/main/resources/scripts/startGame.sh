#!/bin/bash
if [ -e log ];then
	echo "log is exist"
else
	mkdir log
fi
JAVA=java
jar=GameServer.jar
MAIN=com.lx.game.start.ServerStart
libs=../libs/*

"$JAVA" -cp "$libs":"$jar" "$MAIN" -Dfile.encoding=UTF-8 >log/game.log  2>> log/game.log &
pid=$!
echo "$pid" > pid.gameserver
echo "$pid"