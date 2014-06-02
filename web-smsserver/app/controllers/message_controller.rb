class MessageController < ApplicationController
  before_filter :find_message, only:[:show]
  before_filter :find_channel

  def index
    @messages = Message.where(channel_id: params[:channel_id] ).order(id: :desc).page(params[:page]).per(20)
  end

  def show



  end

  def new

  end

  def create
    rmq = RabbitSms.new
    rmq.create_sms(1,1)
  end

  protected

  def find_message
    @message = Message.find(params[:id])
  end

  def find_channel
    @channel = Channel.find(params[:channel_id])
  end
end
