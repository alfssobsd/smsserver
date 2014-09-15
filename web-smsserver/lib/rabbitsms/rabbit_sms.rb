class RabbitSms
  attr_accessor :uri, :queue_prefix, :connect_timeout, :conn, :exchange

  def initialize
    self.uri = Settings.rabbitmq.uri
    self.queue_prefix = Settings.rabbitmq.queue_prefix
    self.connect_timeout = Settings.rabbitmq.connect_timeout
  end

  def create_sms(inbound_message)
    p inbound_message.channel
    p inbound_message.channel.queue_name
    self.connect(self.queue_prefix + inbound_message.channel.queue_name)
    self.publish inbound_message.to_json(:only => [ 'to', 'from', 'message_text' ])
    self.disconnect
  end

  protected

  def connect(queue_bind)
    self.conn = Bunny.new(uri)
    self.conn.start
    channel  = conn.create_channel
    self.exchange = channel.direct(queue_bind, durable: true, auto_delete: false)
    queue = channel.queue(queue_bind, durable:true, exclusive: false, auto_delete: false, arguments: { "x-ha-policy" => "all" }).bind(self.exchange)
  end

  def publish(message)
    self.exchange.publish message, persistent:true
  end

  def disconnect
    self.conn.close
  end


end