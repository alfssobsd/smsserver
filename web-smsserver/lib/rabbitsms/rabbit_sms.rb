class RabbitSms
  attr_accessor :uri, :queue_prefix, :connect_timeout, :conn, :exchange

  def initialize
    self.uri = SMS_SERVER_CONF[:rabbitmq][:uri]
    self.queue_prefix = SMS_SERVER_CONF[:rabbitmq][:queue_prefix]
  end

  def create_sms(channelt, messaget)

    self.connect
    message = JSON.dump({ subject: 'Test', id: 'abc' })
    self.publish message
    self.disconnect
  end


  protected

  def connect
    self.conn = Bunny.new(uri)
    self.conn.start
    channel  = conn.create_channel
    self.exchange = channel.direct("helloworld", durable: true, auto_delete: false)
    queue = channel.queue("helloworld", durable:true, exclusive: false, auto_delete: false, arguments: { "x-ha-policy" => "all" }).bind(self.exchange)
  end

  def publish(message)
    self.exchange.publish message, persistent:true
  end

  def disconnect
    self.conn.close
  end


end