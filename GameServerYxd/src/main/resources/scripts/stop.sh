#i/bin/sh
pid=`cat pid.GameStart`
echo "stop GameStart $pid"
kill $pid
rm -rf pid.GameStart
