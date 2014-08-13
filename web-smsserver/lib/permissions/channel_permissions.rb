module ChannelPermissions
  extend ActiveSupport::Concern
  included do
    unless method_defined?(:can_read_channel?)
      def can_read_channel?(channel)
        (self.get_permission_by_channel(channel) != nil) || self.is_admin?
      end
    end

    unless method_defined?(:can_manage_channel?)
      def can_manage_channel?(channel)
         (self.get_permission_by_channel(channel, 'manager') != nil) || self.is_admin?
      end
    end

  end
end
