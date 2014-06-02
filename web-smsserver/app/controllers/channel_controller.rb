class ChannelController < ApplicationController

  def index

    @channels = Channel.all.order(is_enable: :desc)
    # @channels_disable = Channel.disable

  end
end
