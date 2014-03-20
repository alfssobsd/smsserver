#!/bin/sh -e

### BEGIN INIT INFO
# Provides:          smsserver
# Required-Start:    $remote_fs
# Required-Stop:     $remote_fs
# Should-Start:      $network $syslog
# Should-Stop:       $network $syslog
# Default-Start:     2 3 4 5
# Default-Stop:      0 1 6
# Short-Description: Start and stop smsserver
# Description:       smsserver
### END INIT INFO

PATH=/sbin:/bin:/usr/sbin:/usr/bin

VERSION=0.1
JAVA_HOME=/usr/lib/jvm/java-7-sun/jre/bin/java
LOGBACK=/etc/smsserver/logback.xml
CONF=/etc/smsserver/smsserver.xml
JSVC=/usr/bin/jsvc
JVM_OPT="-Xms256m -Xmx1024m  -XX:+UseParNewGC -XX:+CMSParallelRemarkEnabled -XX:+UseConcMarkSweepGC"
PIDFILE=/var/run/smsserver.pid
DAEMONJAR=/opt/smsserver/smsserver-$VERSION.jar
COMMON_DAEMON=/usr/share/java/commons-daemon.jar
MAINCLASS=net.alfss.smsserver.Main
JAVA=/usr/bin/java

JAVA_HOME=""
JDK_DIRS="/usr/lib/jvm/java-7-oracle /usr/lib/jvm/java-6-openjdk /usr/lib/jvm/java-6-sun /usr/lib/jvm/java-1.5.0-sun /usr/lib/j2sdk1.5-sun /usr/lib/j2sdk1.5-ibm"
# Look for the right JVM to use
for jdir in $JDK_DIRS; do
    if [ -r "$jdir/bin/java" -a -z "${JAVA_HOME}" ]; then
	JAVA_HOME="$jdir"
    fi
done

test -f /etc/default/smsserver && . /etc/default/smsserver


. /lib/lsb/init-functions

case "$1" in
    start)
        $JSVC -java-home $JAVA_HOME -jvm server -pidfile $PIDFILE -Dlogback.configurationFile=$LOGBACK $JVM_OPT \
            -cp $DAEMONJAR $MAINCLASS -c $CONF
    ;;

    stop)
        $JSVC -stop -pidfile $PIDFILE $MAINCLASS
    ;;

    restart)
	$0 stop
	$0 start
    ;;

    regen)
        $JAVA -Dlogback.configurationFile=$LOGBACK $JVM_OPT -jar $DAEMONJAR -c $CONF -g
    ;;

    status)
        if [ -f $PIDFILE ]; then
            sms_pid=$(cat $PIDFILE)
            is_running=$(ps -U $USER | grep $sms_pid)
            if [ -n "$is_running" ]; then
                echo "Smsserver is running"
                exit 0
            else
                echo "Pid-file presents, but process seems to be dead"
                exit 1
            fi
        else
            echo "Smsserver is not running"
            exit 3
        fi
    ;;

    *)
	    log_action_msg "Usage: /etc/init.d/smsserver {start|stop|restart|regen}"
	    exit 1
    ;;

esac

exit 0