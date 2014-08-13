class MessageController < ApplicationController
  before_filter :authenticate_user
  before_filter :find_message, only:[:show]
  before_filter :find_channel

  def index
    authorize! :read, @channel
    @messages = Message.where(channel_id: params[:channel_id] ).order(id: :desc).page(params[:page]).per(20)
  end

  def show
    authorize! :read, @channel

  end

  def new
    authorize! :read, @channel
    @inbound_message = InboundMessage.new
  end

  def create
    authorize! :read, @channel
    @inbound_message = InboundMessage.new(inbound_message_params)
    @inbound_message.channel = @channel
    if @inbound_message.valid?
      rmq = RabbitSms.new
      rmq.create_sms(@inbound_message)
      flash[:success] = "#{t('messages.notify.send.success')}"
      redirect_to channels_messages_new_path(@channel)
    else
      render 'new'
    end
  end

  protected

  def find_message
    @message = Message.find(params[:id])
  end

  def find_channel
    @channel = Channel.find(params[:channel_id])
  end

  def inbound_message_params
    params.require(:inbound_message).permit(:from, :to, :message_text)
  end
end
