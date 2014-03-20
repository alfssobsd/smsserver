INSERT INTO public.channels (id, is_enable, is_fake, name, is_payload, queue, smpp_dest_npi, smpp_dest_ton,
smpp_enquire_link_interval, smpp_host, smpp_password, smpp_port, smpp_reconnect_timeout,
smpp_send_message_per_second, smpp_source_addr, smpp_source_npi, smpp_source_ton, smpp_system_type,
smpp_username, smpp_max_message) VALUES (1, true, false, 'demo', false, 'demo', 1, 1, 20, 'localhost',
'demopassword', 3700, 10, 20, 'srcaddr', 0, 5, 'SINGLE', 'demouser', 12);
