module ApplicationHelper
  def flash_class(level)
    case level
      when 'notice' then "alert alert-info alert-dismissable"
      when 'success' then "alert alert-success alert-dismissable"
      when 'error' then "alert alert-danger alert-dismissable"
      when 'alert' then "alert alert-warning alert-dismissable"
      else
        "alert alert-info"
    end
  end

  def is_active?(controller, action='', return_class='active')
    return_class if params[:controller].match(controller) and params[:action].match(action)
  end

  def title(page_title)
    content_for (:title) { page_title + " / SMSServer" }
  end

  def yield_or_default(section, default = "SMSServer")
    content_for?(section) ? content_for(section) : default
  end

  def user_avatar(user, options = {})
    image = get_gravatar(user.email, options)
    image_tag image, size: "#{options[:size]}x#{options[:size]}", :alt => "Avatar" if image.present?
  end

  def get_gravatar(email, options = {})
    require 'digest/md5' unless defined?(Digest::MD5)
    md5 = Digest::MD5.hexdigest(email.to_s.strip.downcase)
    options[:s] = options.delete(:size) || 32
    options[:d] = "retro"
    "#{request.ssl? ? 'https://secure' : 'http://www'}.gravatar.com/avatar/#{md5}?#{options.to_param}"
  end

  def yes_no_label_tag(value, yes_text="YES", no_text="NO")
    label_text = no_text
    label_sufix_class = "default"
    if value
      label_text = yes_text
      label_sufix_class = "success"
    end
    ("<span class='label label-" + label_sufix_class + "'>" + label_text + "</span>").html_safe
  end

  def message_status_label_tag(value)
    label_sufix_class = "info"
    label_sufix_class = "success" if value == 'SUCCESS'
    label_sufix_class = "danger" if value == 'FAIL'
    ("<span class='label label-" + label_sufix_class + "'>" + value + "</span>").html_safe
  end

end
