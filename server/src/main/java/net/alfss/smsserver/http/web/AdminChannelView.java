package net.alfss.smsserver.http.web;

import net.alfss.smsserver.http.services.ChannelConnectionServices;
import net.alfss.smsserver.http.services.ChannelServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * User: alfss
 * Date: 27.02.14
 * Time: 1:54
 */
@Controller
public class AdminChannelView {

    @Autowired
    private ChannelServices channelServices;
    @Autowired
    private ChannelConnectionServices channelConnectionServices;


    public String list(Model model)
    {
        model.addAttribute("channelList", channelServices.getChannelList());
        return "admin/channel/list";
    }

    public String add(Model model) {
        return "admin/channel/add";
    }

    public String save(Model model) {
        return "redirect:" + "/admin/channel";
    }

    public String edit(Model model, @PathVariable("channel_id") Integer channelId) {
        model.addAttribute("channel", channelServices.getChannelById(channelId));
        model.addAttribute("channelConnectionList", channelConnectionServices.getListByChannel(channelId));
        return "admin/channel/edit";
    }

    public String update(Model model, @PathVariable("channel_id") Integer channelId) {
        return "redirect:" + "/admin/channel/" + channelId + "/edit";
    }

    public String delete(Model model, @PathVariable("channel_id") Integer channelId) {
        return "redirect:" + "/admin/channel";
    }

    public String add_connect(Model model, @PathVariable("channel_id") Integer channelId) {
        return "redirect:" + "/admin/channel/" + channelId + "/edit";
    }
}
