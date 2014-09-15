module OmniAuth
  module Strategies
    class LDAP
      def request_phase
        redirect('/')
      end
    end
  end
end