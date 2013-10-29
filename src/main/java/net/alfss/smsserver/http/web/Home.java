package net.alfss.smsserver.http.web;

/**
 * User: alfss
 * Date: 07.10.13
 * Time: 13:11
 */

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Home {
    @RequestMapping("/")
    public String home()
    {
        return "index";
    }
}