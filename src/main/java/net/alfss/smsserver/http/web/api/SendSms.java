package net.alfss.smsserver.http.web.api;

import net.alfss.smsserver.http.domain.ChannelUser;
import net.alfss.smsserver.http.services.ChannelMessageServices;
import net.alfss.smsserver.http.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;

/**
 * User: alfss
 * Date: 17.10.13
 * Time: 18:46
 */

@Controller
public class SendSms {
    @Autowired
    private UserServices userServices;
    @Autowired
    private ChannelMessageServices channelMessageServices;

    @RequestMapping(value = "/api/kannel/sendsms", method = {RequestMethod.GET})
    public ResponseEntity<String> sendsms(@RequestParam("username") String userName,
                                          @RequestParam("password") String password,
                                          @RequestParam("text") String messageText,
                                          @RequestParam("to") String phone) {

        if(!userServices.checkUserPassword(userName, password)) {
            return new ResponseEntity<String>("Invalid username or password", HttpStatus.FORBIDDEN);
        }

        ChannelUser channelUser = userServices.getChannelUser(userName);
        try {
            channelMessageServices.pushMessageToChannel(phone,
                    new String(messageText.getBytes(), "UTF-16BE"),
                    channelUser.getChannel().getChannelName());
            return new ResponseEntity<String>("OK", HttpStatus.OK);
        } catch (UnsupportedEncodingException e) {
            return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
        }
    }

}
