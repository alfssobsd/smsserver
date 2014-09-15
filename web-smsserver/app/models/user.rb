class User < ActiveRecord::Base
  # Include default devise modules. Others available are:
  # :confirmable, :lockable, :timeoutable and :omniauthable
  devise :database_authenticatable, :recoverable, :rememberable, :trackable, :validatable, :omniauthable, :omniauth_providers => [:ldap]

  has_many :memberships, foreign_key: :member_id
  has_many :channels, :through =>  :memberships, :class_name => Channel, :source => :channel

  attr_accessor :login

  before_create :generate_token

  validates :username,  presence: true,
            :format => {:with => /\A[a-z0-9\-\_]+\z/i},
            length: {minimum: 3, maximum: 254},
            :uniqueness => {
                :case_sensitive => false
            }

  scope :user_search, -> { order(is_admin: :desc) }

  class << self
    def get_user_by_token(token)
      return nil if token.nil?
      begin
        User.find_by_token(token)
      rescue ActiveRecord::RecordNotFound
        return nil
      end
    end

    def find_for_database_authentication(warden_conditions)
      conditions = warden_conditions.dup
      if login = conditions.delete(:login)
        where(conditions).where(["lower(username) = :value OR lower(email) = :value", { :value => login.downcase }]).first
      else
        where(conditions).first
      end
    end

    def search(search, page)
      if search
        user_search.where('username ILIKE ? or email ILIKE ?', "%#{search}%", "%#{search}%").page page
      else
        user_search.page page
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

  def login=(login)
    @login = login
  end

  def login
    @login || self.username || self.email
  end

end
