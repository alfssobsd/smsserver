class Message < ActiveRecord::Base
  belongs_to :channel
  belongs_to :message_status

end
