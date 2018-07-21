package me.bedaring.imsproject.controllers;

import me.bedaring.imsproject.models.AssignedGroup;
import me.bedaring.imsproject.models.Category;
import me.bedaring.imsproject.models.Severity;
import me.bedaring.imsproject.models.data.CategoryDao;
import me.bedaring.imsproject.models.data.GroupDao;
import me.bedaring.imsproject.models.data.SeverityDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("admin")
public class AdminController {

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private SeverityDao severityDao;

    SimpleDateFormat format = new SimpleDateFormat("EEEE MMMM d, y - hh:mm:ss aa");

    @RequestMapping(value="menu")
    public String index(Model model) {
        model.addAttribute("title", "IMS - Admin Menu");
        return "admin/menu";
    }

    @RequestMapping(value = "group/add", method = RequestMethod.GET)
    public String displayAddGroup(Model model) {
        model.addAttribute("title", "IMS - Add Group");
        model.addAttribute("subtitle", "Add Group");
        model.addAttribute("group", new AssignedGroup());
        model.addAttribute("date", format.format(new Date()));
        return "admin/group/add";
    }

    @RequestMapping(value = "group/add", method = RequestMethod.POST)
    public String processAddGroup(@Valid @ModelAttribute("group") AssignedGroup group, Errors errors, Model model,
                                  RedirectAttributes message) {
        if (errors.hasErrors()) {
            model.addAttribute("title", "IMS - Add Group post");
            model.addAttribute("subtitle", "Add Group");
            model.addAttribute("date", format.format(new Date()));
            return "admin/group/add";
        }
        groupDao.save(group);
        message.addFlashAttribute("message", "Successfully added new group");
        return "redirect:/admin/menu";
    }

    @RequestMapping(value = "severity/add", method = RequestMethod.GET)
    public String displayAddSeverity(Model model) {
        model.addAttribute("title", "IMS - Add Severity");
        model.addAttribute("subtitle", "Add Severity");
        model.addAttribute("severity", new Severity());
        model.addAttribute("date", format.format(new Date()));
        return "admin/severity/add";
    }

    @RequestMapping(value = "severity/add", method = RequestMethod.POST)
    public String processAddSeverity(@Valid @ModelAttribute("severity") Severity severity, Errors errors,  Model model,
                                     RedirectAttributes message) {
        if (errors.hasErrors()) {
            model.addAttribute("title", "IMS - Add Severity");

            model.addAttribute("date", format.format(new Date()));
            return "admin/severity/add";
        }
        severityDao.save(severity);
        message.addFlashAttribute("message", "New severity level added successfully");
        return "redirect:/admin/menu";
    }

    @RequestMapping(value = "category/add", method = RequestMethod.GET)
    public String displayAddCategory(Model model) {
        model.addAttribute("title", "IMS - Add Category");
        model.addAttribute("subtitle", "Add Category");
        model.addAttribute("date", format.format(new Date()));
        model.addAttribute("category", new Category());
        return "admin/category/add";
    }

    @RequestMapping(value = "category/add", method = RequestMethod.POST)
    public String processAddCategory(@Valid @ModelAttribute("category") Category category, Errors errors, Model model,
                                     RedirectAttributes message) {
        if (errors.hasErrors()) {
            model.addAttribute("title", "IMS - Add Category");
            model.addAttribute("subtitle", "Add Category");
            model.addAttribute("date", format.format(new Date()));
            return "admin/category/add";

        }
        categoryDao.save(category);
        message.addFlashAttribute("message", "New category successfully added");
        return "redirect:/admin/menu";
    }
}
