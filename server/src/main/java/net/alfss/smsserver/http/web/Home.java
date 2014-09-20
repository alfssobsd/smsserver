package net.alfss.smsserver.http.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

/**
 * User: alfss
 * Date: 19.09.14
 * Time: 15:36
 */
@Controller
public class Home {
    public ResponseEntity index() {
        return new ResponseEntity<>("OK INDEX", HttpStatus.OK);
    }
}
