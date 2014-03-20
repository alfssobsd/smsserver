class MessageStatus < ActiveRecord::Base
  has_many :messages
end
