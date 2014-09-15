class Settings < Settingslogic
  source ENV.fetch('WEB_SMSSERVER_CONFIG') { "#{Rails.root}/config/web-smsserver.yml" }
  namespace Rails.env
end

Settings['ldap'] ||= Settingslogic.new({})
Settings.ldap['enabled'] = false if Settings.ldap['enabled'].nil?

# SMS_SERVER_CONF = HashWithIndifferentAccess.new(YAML.load_file("#{Rails.root}/config/web-smsserver.yml"))
