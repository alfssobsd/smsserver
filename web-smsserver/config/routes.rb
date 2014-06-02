WebSmsserver::Application.routes.draw do
  devise_for :users
  # The priority is based upon order of creation: first created -> highest priority.
  # See how all your routes lay out with "rake routes".

  # You can have the root of your site routed with "root"
  root to: redirect('/channels')

  get 'channels' => 'channel#index', as: :channels
  get "channels/:channel_id/messages", to: redirect('channels/%{channel_id}/messages/list/1'), as: :channels_messages_index
  get "channels/:channel_id/messages/list/(:page)" => 'message#index', as: :channels_messages_page
  get "channels/:channel_id/messages/:id/show" => 'message#show', as: :channels_messages_show
  get "channels/:channel_id/messages/new" => 'message#new', as: :channels_messages_new
  post "channels/:channel_id/messages/create" => 'message#create', as: :channels_messages_create


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
