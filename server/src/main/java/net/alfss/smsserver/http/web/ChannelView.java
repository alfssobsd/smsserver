package net.alfss.smsserver.http.web;

import net.alfss.smsserver.http.services.ChannelConnectionServices;
import net.alfss.smsserver.http.services.ChannelServices;
import net.alfss.smsserver.http.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

/**
 * User: alfss
 * Date: 23.10.13
 * Time: 18:28
 */
@Controller
public class ChannelView {
    @Autowired
    private ChannelServices channelServices;
    @Autowired
    private ChannelConnectionServices channelConnectionServices;
    @Autowired
    private UserServices userServices;


//    @RequestMapping("/channel/{channel_name}/queue/{page}")
//    public String queue(Model model, @PathVariable("channel_name") String channelName, @PathVariable("page") Long page)
//    {
//        model.addAttribute("channel", channelServices.get(channelName));
////        model.addAttribute("channelMessageList",
////                channelMessageServices.getMessageListFromChannel(channelServices.getChannel(channelName), page));
//        return "channel/queue";
//    }
//
//    @RequestMapping("/channel/{channel_name}/queue")
//    public String queue(Model model, @PathVariable("channel_name") String channelName)
//    {
//        model.addAttribute("channel", channelServices.getChannel(channelName));
////        model.addAttribute("channelMessageList",
////                channelMessageServices.getMessageListFromChannel(channelServices.getChannel(channelName), 0));
//        return "channel/queue";
//    }
//    @RequestMapping("/channel/{channel_id}/show")
//    public String queue(Model model, @PathVariable("channel_id") Integer channelId) {
//        model.addAttribute("channel", channelServices.getChannelById(channelId));
//        model.addAttribute("channelConnectionList", channelConnectionServices.getListByChannel(channelId));
//        return "channel/show";
//    }

//    @RequestMapping("/channels")
    public String list(Model model)
    {
        model.addAttribute("channelList", channelServices.getChannelList());
        return "channel/list";
    }

}
