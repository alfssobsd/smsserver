class Message < ActiveRecord::Base
  belongs_to :channel
  belongs_to :message_status

  def get_utf_message_data
    if self.esm_class == 64 and not self.is_payload
        [ self.message_data[0..5].unpack('H*')[0], Iconv.conv("UTF-8", "UTF-16BE",self.message_data[6..139])]
    else
        Iconv.conv("UTF-8", "UTF-16BE",self.message_data)
    end
  end

end
