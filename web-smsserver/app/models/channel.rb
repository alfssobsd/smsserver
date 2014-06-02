class Channel < ActiveRecord::Base
  has_many :channel_connections
  has_many :messages
  has_and_belongs_to_many :users

  def self.enable
    where(is_enable: true).load
  end

  def self.disable
    where(is_enable: false).load
  end

end
