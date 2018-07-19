package me.bedaring.imsproject.controllers;

import me.bedaring.imsproject.models.AssignedGroup;
import me.bedaring.imsproject.models.data.CategoryDao;
import me.bedaring.imsproject.models.data.GroupDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
@RequestMapping("admin")
public class AdminController {

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private GroupDao groupDao;

    @RequestMapping(value="menu")
    public String index(Model model) {
        model.addAttribute("title", "IMS - Admin Menu");
        return "admin/menu";
    }

    @RequestMapping(value = "group/add", method = RequestMethod.GET)
    public String displayAddGroup(Model model) {
        model.addAttribute("title", "IMS - Add Group");
        model.addAttribute("group", new AssignedGroup());
        return "admin/group/add";
    }

    @RequestMapping(value = "group/add", method = RequestMethod.POST)
    public String processAddGroup(@ModelAttribute @Valid AssignedGroup newGroup, Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("title", "IMS - Add Group");
            model.addAttribute("group", new AssignedGroup());
            return "admin/group/add";
        }
        groupDao.save(newGroup);
        return "redirect:/admin/menu";
    }
}
