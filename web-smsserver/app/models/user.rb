class User < ActiveRecord::Base
  # Include default devise modules. Others available are:
  # :confirmable, :lockable, :timeoutable and :omniauthable
  devise :database_authenticatable, :recoverable, :rememberable, :trackable, :validatable

  before_create :generate_token

  has_many :memberships, foreign_key: :member_id
  has_many :channels, :through =>  :memberships, :class_name => Channel, :source => :channel

  class << self
    def get_user_by_token(token)
      return nil if token.nil?
      begin
        User.find_by_token(token)
      rescue ActiveRecord::RecordNotFound
        return nil
      end
    end
  end

  def generate_token
    self.token = Digest::MD5.hexdigest((0...32).map { ('a'..'z').to_a[rand(26)] }.join + self.email)
  end

  def get_permission_by_channel(channel, permission_type_name=nil)
    if permission_type_name
      return memberships.where(channel:channel, channel_permission_types: {name: permission_type_name}).first
    end
    memberships.where(channel: channel).first
  end

end
