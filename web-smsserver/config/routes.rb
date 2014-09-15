WebSmsserver::Application.routes.draw do



  # The priority is based upon order of creation: first created -> highest priority.
  # See how all your routes lay out with "rake routes".

  # You can have the root of your site routed with "root"
  root to: redirect('/channels')

  devise_for :users, controllers: { omniauth_callbacks: "users/omniauth_callbacks",
                                    registrations: 'users/registrations',
                                    sessions: 'users/sessions',
                                    passwords: 'users/passwords',
                                    unlocks: 'users/unlocks'}

  get 'channels' => 'channel#index', as: :channels
  get "channels/:channel_id/messages", to: redirect('channels/%{channel_id}/messages/list/1'), as: :channels_messages_index
  get "channels/:channel_id/messages/list/(:page)" => 'message#index', as: :channels_messages_page
  get "channels/:channel_id/messages/:id/show" => 'message#show', as: :channels_messages_show
  get "channels/:channel_id/messages/new" => 'message#new', as: :channels_messages_new
  post "channels/:channel_id/messages/create" => 'message#create', as: :channels_messages_create

  namespace :api do
    namespace :v1  do
      namespace :send_sms  do
        post 'sendsms'
      end
      namespace :users, only: [] do
        get '', action: :search, as: "search"
        get ':user_id', action: :show, as: "show"
      end
    end
  end

  namespace :admin do
    get 'index' => 'main#index'

    namespace :users do
      get :list
      get :new
      post :create
      get ":user_id/edit", action: :edit, as: "edit"
      put ":user_id/update" , action: :update, as: "update"
      delete ":user_id/destroy" , action: :destroy, as: "destroy"
    end

    namespace :channels do
      get :list
      get :new
      post :create
      get ":channel_id/edit", action: :edit, as: "edit"
      put ":channel_id/update" , action: :update, as: "update"
      delete ":channel_id/destroy" , action: :destroy, as: "destroy"
      #members
      post ":channel_id/members/create", to: "members#create", as: "members_create"
      delete ":channel_id/members/:user_id/destroy", to: "members#destroy", as: "members_destroy"
      #channel connections
      post ":channel_id/connections/create", to: "connections#create", as: "connections_create"
      put ":channel_id/connections/:connect_id/update", to: "connections#update", as: "connections_update"
      delete ":channel_id/connections/:connect_id/destroy", to: "connections#destroy", as: "connections_destroy"
    end
  end


  # namespace :channels do
  #   get ":channel_id/messages" => 'message#index', as: :messages_index
  #   get ":channel_id/messages/:id/show" => 'message#show', as: :messages_show
  # end

  # Example of regular route:
  #   get 'products/:id' => 'catalog#view'

  # Example of named route that can be invoked with purchase_url(id: product.id)
  #   get 'products/:id/purchase' => 'catalog#purchase', as: :purchase

  # Example resource route (maps HTTP verbs to controller actions automatically):
  #   resources :products

  # Example resource route with options:
  #   resources :products do
  #     member do
  #       get 'short'
  #       post 'toggle'
  #     end
  #
  #     collection do
  #       get 'sold'
  #     end
  #   end

  # Example resource route with sub-resources:
  #   resources :products do
  #     resources :comments, :sales
  #     resource :seller
  #   end

  # Example resource route with more complex sub-resources:
  #   resources :products do
  #     resources :comments
  #     resources :sales do
  #       get 'recent', on: :collection
  #     end
  #   end

  # Example resource route with concerns:
  #   concern :toggleable do
  #     post 'toggle'
  #   end
  #   resources :posts, concerns: :toggleable
  #   resources :photos, concerns: :toggleable

  # Example resource route within a namespace:
  #   namespace :admin do
  #     # Directs /admin/products/* to Admin::ProductsController
  #     # (app/controllers/admin/products_controller.rb)
  #     resources :products
  #   end
end
