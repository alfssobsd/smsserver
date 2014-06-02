SMS_SERVER_CONF = HashWithIndifferentAccess.new(YAML.load_file("#{Rails.root}/config/web-smsserver.yml"))
