package net.alfss.smsserver.http.web.api;

import net.alfss.smsserver.http.domain.ChannelUser;
import net.alfss.smsserver.http.services.ChannelMessageServices;
import net.alfss.smsserver.http.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Map;

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

//    @RequestMapping(value = "/api/sendsms/simple", method = {RequestMethod.GET})
    public ResponseEntity<String> sendsmsSimple(@RequestParam("username") String userName,
                                          @RequestParam("password") String password,
                                          @RequestParam("text") String messageText,
                                          @RequestParam("to") String phone) {

        if(!userServices.checkUserPassword(userName, password)) {
            return new ResponseEntity<>("Invalid username or password", HttpStatus.FORBIDDEN);
        }

        ChannelUser channelUser = userServices.getChannelUser(userName);
        try {
            channelMessageServices.pushMessageToChannel(phone,
                    new String(messageText.getBytes(), "UTF-16BE"),
                    channelUser.getChannel().getQueue());
            return new ResponseEntity<>("OK", HttpStatus.OK);
        } catch (UnsupportedEncodingException e) {
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        }
    }

    /*
    {"phone":"79062783751","messagetext":"Привет это текст", "priority":"0", "channel":"smscru"}
     */

//    @RequestMapping(value = "/api/sendsms/json_post", method = {RequestMethod.POST})
    public ResponseEntity<String> sendsmsJsonPost(@RequestBody String json,
                                                  @RequestHeader Map<String,String> header) {

        String userName = header.get("X-AUTH-USER");
        String password = header.get("X-AUTH-PASSWORD");

        if(!userServices.checkUserPassword(userName, password)) {
            return new ResponseEntity<>("{\"status\":\"ERROR\", \"message_error\":\"Invalid username or password\"}", HttpStatus.FORBIDDEN);
        }

        ChannelUser channelUser = userServices.getChannelUser(userName);
        if (channelMessageServices.pushJsonMessageToChannel(json, channelUser)) {
            return new ResponseEntity<>("{\"status\":\"OK\"}", HttpStatus.OK);
        }

        return new ResponseEntity<>("{\"status\":\"ERROR\", \"message_error\":\"Error message format\"}", HttpStatus.FORBIDDEN);
    }

//    @RequestMapping(value = "/api/sendsms/post", method = {RequestMethod.POST})
    public ResponseEntity<String> sendsmsPost() {

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }


}
