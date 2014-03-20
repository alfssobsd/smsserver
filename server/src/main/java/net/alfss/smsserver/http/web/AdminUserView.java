package net.alfss.smsserver.http.web;

import net.alfss.smsserver.database.entity.User;
import net.alfss.smsserver.http.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * User: alfss
 * Date: 27.02.14
 * Time: 0:42
 */

@Controller
public class AdminUserView {

    @Autowired
    private UserServices userServices;

    public String list(Model model)
    {
        model.addAttribute("userList", userServices.getUserList());
        return "admin/user/list";
    }

    public String add(Model model)
    {
        return "admin/user/add";
    }

    public String save(@ModelAttribute User user)
    {
        return "redirect:" + "/admin/user";
    }

    public String edit(Model model, @PathVariable("user_id") Integer userId, @ModelAttribute User user)
    {
        return "admin/user/edit";
    }

    public String update(Model model, @PathVariable("user_id") Integer userId, @ModelAttribute User user)
    {
        return "redirect:" + "/admin/user/" + userId + "/edit";
    }

    public String delete(Model model, @PathVariable("user_id") Integer userId, @ModelAttribute User user)
    {
        return "redirect:" + "/admin/user";
    }
}
