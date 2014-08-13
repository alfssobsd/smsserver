class Ability
  include CanCan::Ability
  def initialize(user)
    user ||= User.new

    User.class_eval do
      include DefaultPermissions
      include ChannelPermissions
    end

    can :manage, User do |user_item|
      user.can_manage_user?(user_item)
    end

    can :edit, User do |user_item|
      user.can_edit_user?(user_item)
    end

    can :read, User do |user_item|
      user.can_read_user?(user_item)
    end

    can :manage, Channel do |channel|
      user.can_manage_channel?(channel)
    end

    can :read, Channel do |channel|
      user.can_read_channel?(channel)
    end

    #
    # #Posts
    # can :read, Posts::Post do |post|
    #   user.can_read_post?(post)
    # end
    #
    # can :edit, Posts::Post do |post|
    #   user.can_edit_post?(post)
    # end
    #
    # can :manage, Posts::Post do |post|
    #   user.can_manage_post?
    # end
  end
end