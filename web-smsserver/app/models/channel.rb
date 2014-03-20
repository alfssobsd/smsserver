class Channel < ActiveRecord::Base
  has_many :channel_connections
  has_many :messages
  has_and_belongs_to_many :users

end
