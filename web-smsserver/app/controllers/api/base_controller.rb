class Api::BaseController < ApplicationController

  rescue_from CanCan::AccessDenied do |exception|
    render :status => :forbidden, :text => "Forbidden"
  end

  protected

  def authenticate_user
    unless signed_in?
      raise ActionController::RoutingError.new('Forbidden')
    end
  end

  def authenticate_by_token
    token = nil
    token = params['auth_token'] if request.GET and params['auth_token']
    token = request.headers["X-AUTH-TOKEN"] if request.POST and request.headers["X-AUTH-TOKEN"]
    if token
      @current_user = User.get_user_by_token(token)
      return current_user
    end

    render :status => :forbidden, :text => "Forbidden" unless @current_user
  end

end