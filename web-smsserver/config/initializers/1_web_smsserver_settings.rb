class Settings < Settingslogic
  if File.exist?("#{Rails.root}/config/web-smsserver-local.yml")
    source ENV.fetch('WEB_SMSSERVER_CONFIG') { "#{Rails.root}/config/web-smsserver-local.yml" }
  else
    source ENV.fetch('WEB_SMSSERVER_CONFIG') { "#{Rails.root}/config/web-smsserver.yml" }
  end
  namespace Rails.env
end

Settings['ldap'] ||= Settingslogic.new({})
Settings.ldap['enabled'] = false if Settings.ldap['enabled'].nil?

# SMS_SERVER_CONF = HashWithIndifferentAccess.new(YAML.load_file("#{Rails.root}/config/web-smsserver.yml"))
