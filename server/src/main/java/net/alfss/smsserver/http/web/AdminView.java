package net.alfss.smsserver.http.web;

import org.springframework.stereotype.Controller;

/**
 * User: alfss
 * Date: 27.02.14
 * Time: 1:52
 */
@Controller
public class AdminView {

    public String show()
    {
        return "admin/index";
    }
}
