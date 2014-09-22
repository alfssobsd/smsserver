package net.alfss.smsserver.http.web.api;

import net.alfss.smsserver.http.domain.Status;
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
        String status = channelControl.startChannel(json, header);

        if (status.equals(Status.OK)) {
            return new ResponseEntity<>(Status.OK, HttpStatus.OK);
        }

        if (status.equals(Status.ACCESS_ERROR)) {
            return new ResponseEntity<>(Status.ACCESS_ERROR, HttpStatus.FORBIDDEN);
        }

        if (status.equals(Status.DATABSE_ERROR)) {
            return new ResponseEntity<>(Status.DATABSE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(Status.INTERNAL_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> stop(@RequestBody String json, @RequestHeader Map<String,String> header) {
        String status = channelControl.stopChannel(json, header);

        if (status.equals(Status.OK)) {
            return new ResponseEntity<>(Status.OK, HttpStatus.OK);
        }

        if (status.equals(Status.ACCESS_ERROR)) {
            return new ResponseEntity<>(Status.ACCESS_ERROR, HttpStatus.FORBIDDEN);
        }

        if (status.equals(Status.DATABSE_ERROR)) {
            return new ResponseEntity<>(Status.DATABSE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(Status.INTERNAL_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> status(@RequestBody String json, @RequestHeader Map<String,String> header) {
//        if (enableAccess(header)) {
//            return new ResponseEntity<>("OK STATUS", HttpStatus.OK);
//        }
        return new ResponseEntity<>("NOT IMPLEMENTED", HttpStatus.NOT_IMPLEMENTED);
    }
}
