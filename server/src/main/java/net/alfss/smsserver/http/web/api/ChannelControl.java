package net.alfss.smsserver.http.web.api;

import net.alfss.smsserver.http.services.impl.ChannelControlImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

/**
 * User: alfss
 * Date: 19.09.14
 * Time: 15:40
 */
@Controller
public class ChannelControl {

    @Autowired
    private ChannelControlImpl channelControl;

    public ResponseEntity<String> start(@RequestBody String json, @RequestHeader Map<String,String> header) {
        if (channelControl.startChannel(json, header)) {
            return new ResponseEntity<>("OK START", HttpStatus.OK);
        }
        return new ResponseEntity<>("OK FORBIDDEN", HttpStatus.FORBIDDEN);
    }

    public ResponseEntity<String> stop(@RequestBody String json, @RequestHeader Map<String,String> header) {
        if (channelControl.stopChannel(json, header)) {
            return new ResponseEntity<>("OK START", HttpStatus.OK);
        }
        return new ResponseEntity<>("OK FORBIDDEN", HttpStatus.FORBIDDEN);
    }

    public ResponseEntity<String> restart(@RequestBody String json, @RequestHeader Map<String,String> header) {
//        if (channelControl.startChannel(json, header)) {
//            return new ResponseEntity<>("OK START", HttpStatus.OK);
//        }
        return new ResponseEntity<>("OK FORBIDDEN", HttpStatus.FORBIDDEN);
    }

    public ResponseEntity<String> status(@RequestBody String json, @RequestHeader Map<String,String> header) {
//        if (enableAccess(header)) {
//            return new ResponseEntity<>("OK STATUS", HttpStatus.OK);
//        }
        return new ResponseEntity<>("OK FORBIDDEN", HttpStatus.FORBIDDEN);
    }
}
