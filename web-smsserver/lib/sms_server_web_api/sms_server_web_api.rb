class SmsServerWebApi
  require 'uri'
  require 'net/http'

  attr_accessor :base_url, :token

  def initialize
    self.base_url = Settings.smsserver_http_url
    self.token = Settings.smsserver_http_token
  end

  def start(channel)
    begin
      data = { "channel_id" => channel.id }.to_json
      uri = URI.parse(self.base_url + '/api/v1/channel/start')
      http = Net::HTTP.new(uri.host,uri.port)
      req = Net::HTTP::Post.new(uri.path, initheader = {'Content-Type' =>'application/json' })
      req['x-token'] = self.token
      req.body = data
      res = http.request(req)
      return true if res.code == 200
    rescue Errno::ECONNREFUSED
      # ignored
    end
    false
  end

  def stop(channel)
    begin
      data = { "channel_id" => channel.id }.to_json
      uri = URI.parse(self.base_url + '/api/v1/channel/stop')
      http = Net::HTTP.new(uri.host,uri.port)
      req = Net::HTTP::Post.new(uri.path, initheader = {'Content-Type' =>'application/json' })
      req['x-token'] = self.token
      req.body = data
      res = http.request(req)
      return true if res.code == 200
    rescue Errno::ECONNREFUSED
      # ignored
    end
    false
  end

  def status(channel)
    params = { 'channel_id' => channel.id }
    url = URI.parse(self.uri + '/api/v1/channel/stop')
    resp, data = Net::HTTP.get_response(url, params)
    puts resp.inspect
    puts data.inspect
    true
  end
end