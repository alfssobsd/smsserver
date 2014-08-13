class Admin::BaseController < ApplicationController
  before_filter :only_admin_access


  rescue_from CanCan::AccessDenied do |exception|
    authenticate_user
    redirect_to root_path, :alert => exception.message if signed_in?
  end

  protected
  def only_admin_access
    authorize! :manage, current_user
  end
end
