## Smsserver

implementation smpp transmitter server

 * rabbitmq interface
 * http interface
 * store message in redis


## Required for run

* java 1.7
* commons-daemon >= 1.0.7
* jsvc >= 1.0.7
* rabbitmq >= 3.0
* redis >= 2.6


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
    <sms_servers>
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
    </sms_servers>
    <channel_users>
        <user>
            <username>user1</username>
            <password>test</password>
            <channel>smscru</channel>
        </user>
    </channel_users>
    <!--rabbit-->
    <rabbit-host>10.211.55.254</rabbit_host>
    <rabbit-port>5672</rabbit_port>
    <rabbit-vhost>/</rabbit_vhost>
    <rabbit-queue>message</rabbit_queue>
    <rabbit-user>guest</rabbit_user>
    <rabbit-password>guest</rabbit_password>
    <!--redis-->
    <redis-max-pool>20</redis_max_pool>
    <redis-timeout>10</redis_timeout>
    <redis-host>10.211.55.254</redis_host>
    <redis-port>6379</redis_port>
    <redis-db>0</redis_db>
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

sms_servers sections:

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


channel_users sections:
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
            <pattern>[%d{yyyy-MM-dd - HH:mm:ss}]  [%thread] %-5level - %msg%n</pattern>
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



    <logger name="org.eclipse.jetty" level="ERROR"/>
    <logger name="org.eclipse.jetty.jndi" level="ERROR"/>
    <logger name="org.eclipse.jetty.jndi.local" level="ERROR"/>

    <logger name="org.springframework" level="ERROR"/>

    <logger name="net.alfss.smsserver.http.WebServerRequestLog" level="info" additivity="false">
        <appender-ref ref="WebRequestLog"/>
    </logger>

    <root level="ERROR">
        <appender-ref ref="AnyLogs" />
    </root>
</configuration>
```


## Example start script
[[include:etc/smsserver.sh]]
https://github.com/alfss/smsserver/blob/master/etc/smsserver.sh

## Example connect setup
<center>
<img src="https://raw.github.com/alfss/smsserver/master/use%20schema.png" alt="2">
</center>

## Example send message HTTP
```
curl http://localhost:8082/api/kannel/sendsms\?username\=alfss1\&to\=7*********\&password\=test\&text\=%04%1F%04%40%048%042%045%04B%00
```

* username - user name
* password - password
* to - destination address
* text - text UTF-16BE code to url encode



## Example send message RabbitMQ

java:
```
        String message = new String("{\"phone\":\"7*********\",\"messagetext\":\"Привет\", \"priority\":\"0\", \"channel\":\"smscru\"}")
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

* phone - destination address
* messagetext - text message
* priority - priority
* channel - channel name







