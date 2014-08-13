class Api::V1::SendSmsController < Api::BaseController
  skip_before_filter :verify_authenticity_token
  before_filter :authenticate_by_token , only: [:sendsms]

  # {"message_text":"text", "from": "unicom", "channel":"unicom24t", "to":"79062223344"}
  def sendsms
    begin
      @inbound_message = InboundMessage.new(inbound_message_params)
      @inbound_message.channel = Channel.find_by_name(@inbound_message.channel)
    rescue JSON::ParserError
      return render status: :bad_request, json: "Error JSON"
    end

    authorize! :read, @inbound_message.channel
    if @inbound_message.valid?
      rmq = RabbitSms.new
      rmq.create_sms(@inbound_message)
      render json: "OK"
    else
      render status: :bad_request, json: "Error Message"
    end
  end

  protected

  def inbound_message_params
    request.body.rewind #fix JSON::ParserError (A JSON text must at least contain two octets!)
    json_params = ActionController::Parameters.new( JSON.parse(request.body.read) )
    json_params.permit(:from, :to, :message_text, :expire_time, :channel)
  end
end
