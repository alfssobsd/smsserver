class Channel < ActiveRecord::Base
  #TODO: references
  #      @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,  mappedBy = "channels")
  #private Set<User> users;

  #@OneToMany(fetch = FetchType.LAZY, mappedBy = "channel", cascade = CascadeType.ALL)
  #public List<Message> messages;

  ##@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,  mappedBy = "channel")
  #private Set<ChannelConnection> channelConnections;
end
