#!/bin/bash
if [ -e log ];then
	echo "log is exist"
else
	mkdir log
fi
JAVA=java
jar=mmoLoginServer.jar
MAIN=com.wx.server.start.LoginServer
libs=./*

"$JAVA" -cp "$libs":"$jar" "$MAIN" 11 -Dfile.encoding=UTF-8 >log/login.log  2>> log/login.log &
pid=$!
echo "$pid" > pid.loginserver
echo "$pid"