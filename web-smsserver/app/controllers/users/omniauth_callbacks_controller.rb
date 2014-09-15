class Users::OmniauthCallbacksController < Devise::OmniauthCallbacksController

  def ldap
    username = request.env["omniauth.auth"][:extra][:raw_info][Settings.ldap.uid][0]
    @user = User.find_by_username(username)
    if @user
      sign_in_and_redirect(@user)
    else
      flash[:alert] = "Access denied for your LDAP account."
      redirect_to new_user_session_path
    end
  end
end