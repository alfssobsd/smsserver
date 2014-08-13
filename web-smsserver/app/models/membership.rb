class Membership < ActiveRecord::Base
  belongs_to :channel
  belongs_to :channel_permission_type
  belongs_to :member, class_name: User


  validates :channel, :presence => true, :uniqueness => {:scope => :member}
end
