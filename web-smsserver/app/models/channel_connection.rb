class ChannelConnection < ActiveRecord::Base
  belongs_to :channel

  before_destroy :count_channel_connection

  protected
  def count_channel_connection
    return false if self.channel.channel_connections.length <= 1
  end

end
