package net.alfss.smsserver.http.web;

import net.alfss.smsserver.http.services.ChannelServices;
import net.alfss.smsserver.http.services.MessageServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * User: alfss
 * Date: 19.02.14
 * Time: 13:02
 */
@Controller
public class MessageView {

    @Autowired
    private MessageServices messageServices;

    @Autowired
    private ChannelServices channelServices;

    public String list(Model model, @PathVariable("channel_id") Integer channelId, @PathVariable("page_number") Integer pageNumber)
    {
        int offset = pageNumber * 50;
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("channel", channelServices.getChannelById(channelId));
        model.addAttribute("messageList", messageServices.getMessageListByChannel(channelId, 50, offset));
        return "message/list";
    }

    public String show(Model model, @PathVariable("channel_id") Integer channelId, @PathVariable("message_id") Integer messageId)
    {
        model.addAttribute("channel", channelServices.getChannelById(channelId));
        model.addAttribute("message", messageServices.getMessage(messageId));
        return "message/show";
    }
}
