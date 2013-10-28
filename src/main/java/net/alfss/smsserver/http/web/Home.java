package net.alfss.smsserver.http.web;

/**
 * User: alfss
 * Date: 07.10.13
 * Time: 13:11
 */

import net.alfss.smsserver.http.services.ChannelServices;
import net.alfss.smsserver.http.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Home {
    @Autowired
    private ChannelServices channelServices;
    @Autowired
    private UserServices userServices;


    @RequestMapping("/")
    public String home(Model model)
    {
        return "index";
    }
}