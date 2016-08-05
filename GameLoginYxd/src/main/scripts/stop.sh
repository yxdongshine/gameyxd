#i/bin/sh
pid=`cat pid.loginserver`
echo "stop loginserver $pid"
kill $pid
rm -rf pid.loginserver
