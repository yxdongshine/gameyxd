#i/bin/sh
pid=`cat pid.GateStart`
echo "stop GateStart $pid"
kill $pid
rm -rf pid.GateStart
