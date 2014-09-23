#!/bin/sh

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

USER=smssrv
LOGBACK=/etc/smsserver/logback.xml
CONF=/etc/smsserver/smsserver.xml
JSVC=/usr/bin/jsvc
PIDFILE=/var/run/smsserver/smsserver.pid
DAEMONJAR=/opt/smsserver/smsserver-1.0.jar
COMMON_DAEMON=/usr/share/java/commons-daemon.jar
MAINCLASS=net.alfss.smsserver.Main
JAVA=/usr/bin/java

JAVA_HOME=""
JDK_DIRS="/usr/lib/jvm/java-8-oracle /usr/lib/jvm/java-7-oracle"
# Look for the right JVM to use
for jdir in $JDK_DIRS; do
    if [ -r "$jdir/bin/java" -a -z "${JAVA_HOME}" ]; then
	JAVA_HOME="$jdir"
    fi
done

test -f /etc/default/smsserver && . /etc/default/smsserver

if [ ! -d /var/run/smsserver ]; then
    mkdir -p /var/log/smsserver
    mkdir -p /var/run/smsserver
    chown ${USER}:${USER} /var/run/smsserver
    chown ${USER}:${USER} /var/log/smsserver
fi

. /lib/lsb/init-functions

case "$1" in
    start)
        sudo -u ${USER} -H bash -c "$JSVC -java-home $JAVA_HOME -jvm server -pidfile $PIDFILE -Dlogback.configurationFile=$LOGBACK $JVM_OPT \
            -cp $DAEMONJAR $MAINCLASS -c $CONF"
    ;;

    stop)
        sudo -u ${USER} -H bash -c "$JSVC -stop -pidfile $PIDFILE $MAINCLASS"
    ;;

    restart)
        $0 stop
        $0 start
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
    regen)
        $JAVA -Dlogback.configurationFile=$LOGBACK $JVM_OPT -jar $DAEMONJAR -c $CONF -g
    ;;

    *)
        log_action_msg "Usage: /etc/init.d/smsserver { start | stop | restart | status | regen }"
        exit 1
    ;;
esac

exit 0