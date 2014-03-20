class InitChannel < ActiveRecord::Migration
  def migrate(direction)
    super
    if direction == :up
      channel = Channel.create!(name: "demo",
                      queue_name: "demo",
                      smpp_host: "localhost",
                      smpp_port: 3700,
                      smpp_username: "demouser",
                      smpp_password: "demopass",
                      smpp_source_addr: "srcaddr",
                      smpp_source_ton: 5,
                      smpp_source_npi: 1,
                      smpp_dest_ton: 1,
                      smpp_dest_npi: 1,
                      smpp_max_split_message: 12,
                      smpp_max_message_per_second: 20,
                      smpp_reconnect_timeout: 10,
                      smpp_enquire_link_interval: 5,
                      is_payload: false,
                      is_fake: false,
                      is_enable: true)
      channel.channel_connections.create!(smpp_system_type: "SINGLE")

    end
  end

  def change
  end
end
