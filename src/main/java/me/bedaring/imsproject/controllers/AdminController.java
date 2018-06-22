package me.bedaring.imsproject.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("admin")
public class AdminController {

    @RequestMapping(value="menu")
    public String index(Model model) {
        model.addAttribute("title", "IMS - Admin Menu");
        return "admin/menu";
    }
}
