class Admin::ChannelsController < Admin::BaseController
  before_filter :find_channel, only: [:edit, :update, :destroy]
  before_filter :find_members, only: [:edit, :update]

  def list
    @channels = Channel.search(params[:search], params[:page])
  end

  def new
    @channel = Channel.new
  end

  def create
    @channel = Channel.new(channel_params)

    if @channel.save
      create_successful
    else
      create_failed
    end
  end

  def edit

  end

  def update
    respond_to do |format|
      if @channel.update(channel_params)
        format.html { redirect_to admin_channels_edit_path(@channel), flash: {success: 'success update channel'} }
      else
        format.html { render action: 'edit' }
      end
    end
  end

  def destroy
    if @channel.delete
      redirect_to admin_channels_list_path, flash: {alert: "channel: " + @channel.email + " is deleted " }
    else
      redirect_to admin_channels_edit_path(@channel), flash: {error: 'channel not deleted'}
    end
  end

  protected

  def find_members
    @memberships_channel = Membership.where(channel_id: @channel)
  end

  def find_channel
    @channel = Channel.find(params[:channel_id])
  end

  def channel_params
    params.require(:channel).permit!
  end

  def create_successful
    flash[:success] = "success create channel"
    redirect_to admin_channels_edit_path(@channel)
  end

  def create_failed
    render action: :new
  end

end
