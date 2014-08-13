class Admin::Channels::ConnectionsController < Admin::BaseController
  before_filter :find_channel, only: [:create, :destroy, :update]
  before_filter :find_connect, only: [:update, :destroy]

  def create
    @connection = ChannelConnection.new(channel:@channel)
    if @connection.save
      create_successful
    else
      create_failed
    end
  end

  def destroy
    if @connection.destroy
      destroy_successful
    else
      destroy_failed
    end
  end

  def update
    if @connection.update(connection_params)
      update_successful
    else
      update_failed
    end
  end

  protected

  def find_channel
    @channel = Channel.find(params[:channel_id])
  end

  def find_connect
    @connection = ChannelConnection.find(params[:connect_id])
  end

  def connection_params
    params.require(:channel_connections).permit(:smpp_system_type)
  end

  def create_successful
    flash[:success] = "connection is created"
    redirect_to admin_channels_edit_path(@channel)
  end

  def create_failed
    flash[:error] = "connection is not create"
    redirect_to admin_channels_edit_path(@channel)
  end

  def update_successful
    flash[:success] = "connection is updated"
    redirect_to admin_channels_edit_path(@channel)
  end

  def update_failed
    flash[:error] = "update connection is problem"
    redirect_to admin_channels_edit_path(@channel)
  end

  def destroy_successful
    flash[:alert] = "connection is deleted"
    redirect_to admin_channels_edit_path(@channel)
  end

  def destroy_failed
    flash[:error] = "connection not deleted"
    redirect_to admin_channels_edit_path(@channel)
  end
end
