class ApplicationController < ActionController::Base
  # Prevent CSRF attacks by raising an exception.
  # For APIs, you may want to use :null_session instead.
  protect_from_forgery with: :exception


  def authenticate_user
    unless signed_in?
      store_location
      redirect_to new_user_session_url, notice: "Please sign in."
    end
  end

  def store_location
    # store last url - this is needed for post-login redirect to whatever the user last visited.
    if  request.fullpath != "/users/sign_in" &&
        request.fullpath != "/users/sign_up" &&
        request.fullpath != "/users/password" &&
        request.fullpath != "/users/sign_out" &&
        !request.xhr? # don't store ajax calls
      session[:previous_url] = request.fullpath
    end
  end

  def is_admin?
    current_user && current_user.is_admin?
  end
end
