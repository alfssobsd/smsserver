class Admin::UsersController < Admin::BaseController
  before_filter :find_user, only: [:edit, :update, :destroy]

  def list
    @users = User.all
  end

  def show

  end

  def new
    @user = User.new
  end

  def create
    @user = User.new(user_params(false))

    if @user.save
      create_successful
    else
      create_failed
    end
  end

  def edit
  end

  def update
    respond_to do |format|
      if @user.update(user_params(true))
        format.html { redirect_to admin_users_edit_path(@user), flash: {success: 'success update user'} }
      else
        format.html { render action: 'edit' }
      end
    end
  end

  def destroy
    if @user.destroy
      redirect_to admin_users_list_path, flash: {alert: "user: " + @user.email + " is deleted " }
    else
      redirect_to admin_users_edit_path(@user), flash: {error: 'user not deleted'}
    end
  end

  protected

  def user_params(is_update)
    tmp_params = params.require(:user).permit(:email, :password, :password_confirmation, :is_admin)
    if is_update and tmp_params[:password].blank?
      tmp_params.delete(:password)
      tmp_params.delete(:password_confirmation) if tmp_params[:password_confirmation].blank?
    end
    tmp_params
  end

  def create_successful
    flash[:success] = "success create user"
    redirect_to admin_users_edit_path(@user)
  end

  def create_failed
    render :action => "new"
  end

  def find_user
    @user = User.find(params[:user_id])
  end
end
