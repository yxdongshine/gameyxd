#!/bin/sh
pid=`cat pid.gameserver`
echo "stop gameserver $pid"
kill $pid
rm -rf pid.gameserver
