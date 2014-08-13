class Admin::Channels::MembersController < Admin::BaseController
  before_filter :find_channel, only: [:create, :destroy]
  before_filter :find_member, only: [:destroy]

  def create
    @membership = Membership.new(membership_params)

    if @membership.save
      create_successful
    else
      create_failed
    end
  end

  def destroy
    @membership = Membership.find_by(channel: @channel, member: @member)
    @membership.destroy if @membership
    destroy_successful
  end


  protected

  def find_channel
    @channel = Channel.find(params[:channel_id])
  end

  def find_member
    @member = User.find(params[:user_id])
  end

  def create_successful
    flash[:success] = "member #{@membership.member.email} is created"
    redirect_to admin_channels_edit_path(@channel)
  end

  def create_failed
    flash[:alert] = "member #{@membership.member.email} not created"
    redirect_to admin_channels_edit_path(@channel)
  end

  def destroy_successful
    flash[:error] = "member #{@member.email} is deleted"
    redirect_to admin_channels_edit_path(@channel)
  end

  def membership_params
    params.require(:memberships).permit(:member_id, :channel_permission_type_id, :channel_id)
  end
end
