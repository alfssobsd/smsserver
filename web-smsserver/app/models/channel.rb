class Channel < ActiveRecord::Base
  after_create :create_channel_connection
  before_update :sms_server_chaneg_status

  has_many :channel_connections
  has_many :messages

  has_many :memberships
  has_many :members, through: :memberships, class_name: User

  validates :name,  presence: true,
                    :format => {:with => /\A[a-z0-9\-\_]+\z/i},
                    length: {minimum: 3, maximum: 254},
                    :uniqueness => {
                        :case_sensitive => false
                    }
  validates :queue_name,  presence: true,
                          :format => {:with => /\A[a-z0-9\-\_]+\z/i},
                          length: {minimum: 3, maximum: 254},
                          :uniqueness => {
                              :case_sensitive => false
                          }
  validates :smpp_port, presence: true, numericality: :true
  validates :smpp_username, length: {minimum: 1, maximum: 16}, presence: true
  validates :smpp_password, length: {minimum: 1, maximum: 9}, presence: true
  validates :smpp_source_addr, length: {minimum: 1, maximum: 255}, presence: true
  validates :smpp_source_ton, presence: true, numericality: true
  validates :smpp_source_npi, presence: true, numericality: true
  validates :smpp_dest_ton, presence: true, numericality: true
  validates :smpp_dest_npi, presence: true, numericality: true
  validates :smpp_max_split_message, presence: true, numericality: true
  validates :smpp_max_message_per_second, presence: true, numericality: true
  validates :smpp_reconnect_timeout, presence: true, numericality: true
  validates :smpp_enquire_link_interval, presence: true, numericality: true

  def self.enable
    where(is_enable: true).load
  end

  def self.disable
    where(is_enable: false).load
  end

  def self.search(search, page)
    if search
      where('name ILIKE ? or queue_name ILIKE ?', "%#{search}%", "%#{search}%").page page
    else
      page page
    end
  end

  private
  def sms_server_chaneg_status
    if self.attribute_changed?(:is_enable)
      sms_api = SmsServerWebApi.new
      sms_api.start(self) if is_enable
      sms_api.stop(self) unless is_enable
    end
    true
  end

  private

  def create_channel_connection
    ChannelConnection.create(channel: self, smpp_system_type: "")
  end

end
