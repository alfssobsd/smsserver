class Api::V1::UsersController < Api::BaseController
  before_filter :authenticate_user

  def search
    username = params[:q]
    users = User.arel_table
    render json: User.where(users[:username].matches("%#{username}%")).limit(10), :only=> [:email, :id, :username]
  end

  def show
    render json: User.find(params[:user_id]), :only=> [:email, :id, :username]
  end
end

