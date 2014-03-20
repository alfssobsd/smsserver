class Channel < ActiveRecord::Base
  has_many :channel_connections
  has_many :messages
  belongs_to :user
end
