class InboundMessage
  include ActiveModel::Validations
  include ActiveModel::Conversion
  extend ActiveModel::Naming

  attr_accessor :from, :to, :message_text, :expire_time, :channel

  validates :message_text, length: {minimum: 3, maximum: 670}, presence: true
  validates :from, length: {minimum: 3, maximum: 21}, presence: true
  validates :to, length: {minimum: 3, maximum: 21}, presence: true, numericality: true
  validates :channel, presence: true
  # validates :expire_time, allow_nil: true, allow_blank: true

  def initialize(attributes = {})
    attributes.each do |name, value|
      send("#{name}=", value)
    end
  end

  def persisted?
    false
  end
end