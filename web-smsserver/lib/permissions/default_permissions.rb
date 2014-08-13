module DefaultPermissions
  extend ActiveSupport::Concern
  included do
    unless method_defined?(:can_read_user?)
      def can_read_user?(user_item)
        true
      end
    end

    unless method_defined?(:can_edit_user?)
      def can_edit_user?(user_item)
        user_item == self || self.is_admin?
      end
    end

    unless method_defined?(:can_manage_user?)
      def can_manage_user?(user_item)
        self.is_admin?
      end
    end

    # unless method_defined?(:can_read_channel?)
    #   def can_read_channel?(channel)
    #     (self.get_permission_by_channel(channel) != nil) || self.is_admin?
    #   end
    # end
    #
    # unless method_defined?(:can_manage_channel?)
    #   def can_manage_channel?(channel)
    #     (self.get_permission_by_channel(channel, 'manage') != nil) || self.is_admin?
    #   end
    # end
  end
end