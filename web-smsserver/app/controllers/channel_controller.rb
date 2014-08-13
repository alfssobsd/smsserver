class ChannelController < ApplicationController
  before_filter :authenticate_user


  def index
    if is_admin?
      @channels = Channel.all.order(is_enable: :desc)
    else
      @channels = current_user.channels.all.order(is_enable: :desc)
    end
  end
end
