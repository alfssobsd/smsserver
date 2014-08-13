class Api::V1::UsersController < Api::BaseController
  before_filter :authenticate_user

  def search
    email = params[:q]
    users = User.arel_table
    render json: User.where(users[:email].matches("%#{email}%")).limit(10), :only=> [:email, :id]
  end

  def show
    render json: User.find(params[:user_id]), :only=> [:email, :id]
  end
end

