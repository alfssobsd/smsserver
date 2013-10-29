## SMSServer

implementation smpp transmitter server

 * rabbitmq interface
 * http interface
 * store message in redis


## Required for run

* java 1.7
* commons-daemon >= 1.0.7
* jsvc >= 1.0.7
* rabbitmq >= 3.0 (server only)
* redis >= 2.6 (server only)


## Create jar

`mvn package`

out:

`smsserver-0.1.jar`


## Setup settings

Create main config and logback.

main config:

``` xml
<?xml version="1.0" encoding="UTF-8" ?>
<configuration>
    <!--sms servers settings-->
    <sms-servers>
        <server>
            <channel>smscru</channel>
            <channel-queue>smscru</channel-queue>
            <host>10.10.10.254</host>
            <send-port>2775</send-port>
            <username>smppclient1</username>
            <password>password</password>
            <enable-playload>false</enable-playload>
            <source-addr>nameinsms</source-addr>
            <source-addr-ton>5</source-addr-ton>
            <source-addr-npi>1</source-addr-npi>
            <dest-addr-ton>1</dest-addr-ton>
            <dest-addr-npi>1</dest-addr-npi>
            <system-type>SINGLE</system-type>
            <recconnect-timeout>30</recconnect-timeout>
            <max-message>12</max-message>
            <message-per-second>100</message-per-second>
            <enquire-link-interval>60</enquire-link-interval>
            <wait-resend-timeout>5</wait-resend-timeout>
            <is-fake-channel>false</is-fake-channel>
        </server>
    </sms-servers>
    <channel-users>
        <user>
            <username>user1</username>
            <password>test</password>
            <channel>smscru</channel>
        </user>
    </channel-users>
    <!--rabbit-->
    <rabbit-host>10.211.55.254</rabbit-host>
    <rabbit-port>5672</rabbit-port>
    <rabbit-vhost>/</rabbit-vhost>
    <rabbit-queue>message</rabbit-queue>
    <rabbit-user>guest</rabbit-user>
    <rabbit-password>guest</rabbit-password>
    <!--redis-->
    <redis-max-pool>20</redis-max-pool>
    <redis-timeout>10</redis-timeout>
    <redis-host>10.211.55.254</redis-host>
    <redis-port>6379</redis-port>
    <redis-db>0</redis-db>
    <!--<redis-password></redis-password>-->
    <!--http -->
    <http-address>0.0.0.0</http-address>
    <http-port>8082</http-port>
    <http-min-pool>1</http-min-pool>
    <http-max-pool>10</http-max-pool>
    <http-out-buffer>32768</http-out-buffer>
    <http-req-header>8192</http-req-header>
    <http-res-header>8192</http-res-header>
</configuration>
```

sms-servers sections:

* server - configure connect channel to smpp
    * channel - channel name
    * channel-queue - queue name
    * host - address smpp host
    * send-port - port smpp host
    * username - auth smpp user
    * password - password  smpp user
    * enable-playload - enable playload mode (default false)
    * source-addr - source address in sms
    * source-addr-ton - source address TON
    * source-addr-npi - source address NPI
    * dest-addr-ton - destination address TON
    * dest-addr-npi - destination address NPi
    * system-type - system type
    * recconnect-timeout - recconnect timout to smpp server
    * max-message - max multipart sms
    * message-per-second - max message per seconds to smpp server
    * enquire-link-interval - enquire link interval
    * wait-resend-timeout - timout if got warning from smpp server
    * is-fake-channel - fake or real channel (default false)


channel-users sections:

* user - user for http interface
    * username - name user
    * password - password user
    * channel - association with the channel

connection to rabbitmq:

* rabbit-host - host RabbitMQ
* rabbit-port - port RabbitMQ
* rabbit-vhost - virtual host RabbitMQ
* rabbit-queue - queue name RabbitMQ
* rabbit-user - user connect to RabbitMQ
* rabbit-password - password

connection to redis

* redis-max-pool - max connection to redis
* redis-timeout - timout connection
* redis-host - redis host
* redis-port - redis port
* redis-db - redis db
* redis-password - redis password

http interface settings

* http-address - listen addres
* http-port - listen port
* http-min-pool - min http thread
* http-max-pool - max http thread
* http-out-buffer - out buffer size
* http-req-header - request header size
* http-res-header - response header size



logback config (please read official documentation logback.qos.ch):

``` xml
<?xml version="1.0" encoding="UTF-8" ?>
<configuration>


    <appender name="AnyLogs" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>/tmp/smsserver/app.log</file>
        <encoder>
            <pattern>[%d{yyyy-MM-dd - HH:mm:ss}] %class [%thread] %-5level - %msg%n</pattern>
        </encoder>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!-- daily rollover -->
            <fileNamePattern>app.%d{yyyy-MM-dd}.log</fileNamePattern>

            <!-- keep 30 days' worth of history -->
            <maxHistory>30</maxHistory>
        </rollingPolicy>
    </appender>

    <appender name="WebRequestLog" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>/tmp/smsserver/web-request.log</file>
        <encoder>
            <pattern>%msg%n</pattern>
        </encoder>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!-- daily rollover -->
            <fileNamePattern>web-request.%d{yyyy-MM-dd}.log</fileNamePattern>

            <!-- keep 30 days' worth of history -->
            <maxHistory>30</maxHistory>
        </rollingPolicy>
    </appender>

    <appender name="SuccessSendSms" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>/tmp/smsserver/success-send-sms.log</file>
        <encoder>
            <pattern>[%d{yyyy-MM-dd - HH:mm:ss}] %msg%n</pattern>
        </encoder>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!-- daily rollover -->
            <fileNamePattern>success-send-sms.%d{yyyy-MM-dd}.log</fileNamePattern>

            <!-- keep 30 days' worth of history -->
            <maxHistory>30</maxHistory>
        </rollingPolicy>
    </appender>

    <appender name="FailSendSms" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>/tmp/smsserver/fail-send-sms.log</file>
        <encoder>
            <pattern>[%d{yyyy-MM-dd - HH:mm:ss}] %msg%n</pattern>
        </encoder>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!-- daily rollover -->
            <fileNamePattern>fail-send-sms.%d{yyyy-MM-dd}.log</fileNamePattern>

            <!-- keep 30 days' worth of history -->
            <maxHistory>30</maxHistory>
        </rollingPolicy>
    </appender>



    <logger name="org.eclipse.jetty" level="ERROR"/>
    <logger name="org.eclipse.jetty.jndi" level="ERROR"/>
    <logger name="org.eclipse.jetty.jndi.local" level="ERROR"/>

    <logger name="org.springframework" level="ERROR"/>

    <logger name="net.alfss.smsserver.sms.logger.SuccessSend" level="info" additivity="false">
        <appender-ref ref="SuccessSendSms"/>
    </logger>

    <logger name="net.alfss.smsserver.sms.logger.FailSend" level="info" additivity="false">
        <appender-ref ref="FailSendSms"/>
    </logger>

    <logger name="net.alfss.smsserver.http.WebServerRequestLog" level="info" additivity="false">
        <appender-ref ref="WebRequestLog"/>
    </logger>

    <root level="DEBUG">
        <appender-ref ref="AnyLogs" />
    </root>
</configuration>
```


## Example start script

https://raw.github.com/alfss/smsserver/master/etc/smsserver.sh

```bash
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

    *)
	log_action_msg "Usage: /etc/init.d/smsserver {start|stop|restart|regen}"
	exit 1
    ;;
esac

exit 0

```



## Example connect setup
<center>
<img src="https://raw.github.com/alfss/smsserver/master/use%20schema.png" alt="2">
</center>

## Example send message HTTP
```
curl http://localhost:8082/api/sendsms/simple\?username\=alfss1\&to\=7*********\&password\=test\&text\=%04%1F%04%40%048%042%045%04B%00
```

* username - user name
* password - password
* to - destination address
* text - text UTF-16BE code to url encode


## Example send message HTTP POST

URL:

```
http://localhost:8082/api/sendsms/json_post
```

Set auth header

```
X-AUTH-USER:alfss1
X-AUTH-PASSWORD:test
```

Json Object

```
{"from": "mynumber", "to":"79062783751","messagetext":"Hello text", "priority":"0", "channel":"smscru", "expiretime":"300"}
```


JSON message field

* to - destination address
* from - source address (optional)
* messagetext - text message
* priority - priority
* channel - channel name
* expiretime - lifetime in seconds (optional) 0 - disable expired


## Example send message RabbitMQ

java:
```
        String message = new String("{\"from\":\"mynumber\",\"to\":\"7*********\",\"messagetext\":\"Привет\", \"priority\":\"0\", \"channel\":\"smscru\", \"expiretime\":\"0\"}")
        ConnectionFactory factory =  new ConnectionFactory();
        factory.setHost(globalConfig.getRabbitHost());
        factory.setPort(globalConfig.getRabbitPort());
        factory.setVirtualHost(globalConfig.getRabbitVhost());
        factory.setUsername(globalConfig.getRabbitUser());
        factory.setPassword(globalConfig.getRabbitPassword());
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        String exchangeName = globalConfig.getRabbitQueue();

        channel.queueDeclare(exchangeName, false, false, false, null);
        channel.exchangeDeclare(exchangeName, "direct");
        channel.basicPublish("", exchangeName, null, message.getBytes());
        channel.close();
```

JSON message field

* to - destination address
* from - source address (optional)
* messagetext - text message
* priority - priority
* channel - channel name
* expiretime - lifetime in seconds (optional) 0 - disable expired







