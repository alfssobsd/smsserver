class Users::PasswordsController < Devise::SessionsController
  layout "devise"

  def update
    super
  end

  protected

  def after_resetting_password_path_for(resource)
    root_path
  end

  def resource_params
    params.fetch(resource_name, {})
  end
end
