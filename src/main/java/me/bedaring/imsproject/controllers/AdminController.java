package me.bedaring.imsproject.controllers;

import me.bedaring.imsproject.models.AssignedGroup;
import me.bedaring.imsproject.models.Category;
import me.bedaring.imsproject.models.Severity;
import me.bedaring.imsproject.models.Ticket;
import me.bedaring.imsproject.models.data.CategoryDao;
import me.bedaring.imsproject.models.data.GroupDao;
import me.bedaring.imsproject.models.data.ImsDao;
import me.bedaring.imsproject.models.data.SeverityDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("admin")
public class AdminController {

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private SeverityDao severityDao;

    @Autowired
    private ImsDao imsDao;

    SimpleDateFormat format = new SimpleDateFormat("EEEE MMMM d, y - hh:mm:ss aa");

    @RequestMapping(value="menu")
    public String index(Model model) {
        model.addAttribute("title", "IMS - Admin Menu");
        model.addAttribute("date", format.format(new Date()));
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

    @RequestMapping(value = "group/update", method = RequestMethod.GET)
    public String listUpdateGroups(@ModelAttribute AssignedGroup assignedGroup, Model model) {
        model.addAttribute("title", "IMS - Update Group");
        model.addAttribute("subtitle", "Update Group");
        model.addAttribute("date", format.format(new Date()));
        model.addAttribute("groups", groupDao.findAll());
        return "admin/group/update";

    }

    @RequestMapping(value = "group/update", method = RequestMethod.POST)
    public String processUpdateGroup(@Valid @ModelAttribute("assignedGroup") AssignedGroup assignedGroup,
                                     Errors errors, RedirectAttributes message, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("title", "IMS - Update Group");
            model.addAttribute("subtitle", "Update Group");
            model.addAttribute("date", format.format(new Date()));
            model.addAttribute("groups", groupDao.findAll());
            model.addAttribute(message.addFlashAttribute("message", "Field cannot be empty"));
            return "redirect:/admin/group/update";
        }

        groupDao.save(assignedGroup);
        message.addFlashAttribute("message", "Group Successfully Updated!");
        return "redirect:/admin/menu";
    }

    @RequestMapping(value = "severity/update", method = RequestMethod.GET)
    public String listUpdateSeverities(@ModelAttribute Severity severity, Model model) {
        model.addAttribute("title", "IMS - Update Severity");
        model.addAttribute("subtitle", "Update Severity");
        model.addAttribute("date", format.format(new Date()));
        model.addAttribute("severities", severityDao.findAll());
        return "admin/severity/update";
    }

    @RequestMapping(value = "severity/update", method = RequestMethod.POST)
    public String processUpdateSeverity(@Valid @ModelAttribute("severity") Severity severity,
                                        @RequestParam String severityName, @RequestParam int id, Errors errors,
                                        RedirectAttributes message, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("title", "IMS - Update Severity");
            model.addAttribute("subtitle", "Update Severity");
            model.addAttribute("date", format.format(new Date()));
            model.addAttribute("severities", severityDao.findAll());
            model.addAttribute(message.addFlashAttribute("message", "Field cannot be empty"));
            return "redirect:/admin/severity/update";
        }

        severityDao.update(severityName, id);

        message.addFlashAttribute("message", "Severity Successfully Updated!");
        return "redirect:/admin/menu";
    }

    @RequestMapping(value = "category/update", method = RequestMethod.GET)
    public String listUpdateCategories(@ModelAttribute Category category, Model model) {
        model.addAttribute("title", "IMS - Update Category");
        model.addAttribute("subtitle", "Update Category");
        model.addAttribute("date", format.format(new Date()));

        model.addAttribute("categories", categoryDao.findAll());
        return "admin/category/update";
    }

    @RequestMapping(value = "category/update", method = RequestMethod.POST)
    public String processUpdateCategory(@Valid @ModelAttribute Category category, Errors errors,
                                        RedirectAttributes message, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("title", "IMS - Update Category");
            model.addAttribute("subtitle", "Update Category");
            model.addAttribute("date", format.format(new Date()));
            model.addAttribute("categories", categoryDao.findAll());
            model.addAttribute(message.addFlashAttribute("message", "Fields cannot be empty"));
            return "redirect:/admin/category/update";
        }

        categoryDao.save(category);
        message.addFlashAttribute("message", "Category Successfully Updated!");
        return "redirect:/admin/menu";
    }

    @RequestMapping(value = "group/delete", method = RequestMethod.GET)
    public String listDeleteGroups(@ModelAttribute AssignedGroup assignedGroup, Model model) {
        model.addAttribute("title", "IMS - Delete Group");
        model.addAttribute("subtitle", "Delete Group");
        model.addAttribute("date", format.format(new Date()));
        model.addAttribute("groups", groupDao.findAll());
        return "admin/group/delete";
    }

    @RequestMapping(value = "group/delete", method = RequestMethod.POST)
    public String processDeleteGroup(@ModelAttribute AssignedGroup assignedGroup,
                                     RedirectAttributes message, Model model) {
        // check if any parent records exist for the group to be deleted, if any are present disallow delete and display message
        int count = imsDao.countTicketByAssignedGroup(assignedGroup);
        if (count > 0) {
            model.addAttribute("title", "IMS - Delete Group");
            model.addAttribute("subtitle", "Delete Group");
            model.addAttribute("date", format.format(new Date()));
            model.addAttribute("groups", groupDao.findAll());
            model.addAttribute(message.addFlashAttribute("message", "Field is in use and cannot be deleted"));
            return "redirect:/admin/group/delete";
        }
        groupDao.deleteById(assignedGroup.getId());
        message.addFlashAttribute("message", "Group Successfully Deleted!");
        return "redirect:/admin/menu";
    }

    @RequestMapping(value = "category/delete", method = RequestMethod.GET)
    public String listDeleteCategories(@ModelAttribute Category category, Model model) {
        model.addAttribute("title", "IMS - Delete Category");
        model.addAttribute("subtitle", "Delete Category");
        model.addAttribute("date", format.format(new Date()));
        model.addAttribute("categories", categoryDao.findAll());
        return "admin/category/delete";
    }

    @RequestMapping(value = "category/delete", method = RequestMethod.POST)
    public String processDeleteCategory(@ModelAttribute Category category, RedirectAttributes message, Model model) {
        // check if any parent records exist for the category to be deleted, if any are present disallow delete and display message

        int count = imsDao.countTicketByCategory(category);
        if (count > 0) {
            model.addAttribute("title", "IMS - Delete Category");
            model.addAttribute("subtitle", "Delete Category");
            model.addAttribute("date", format.format(new Date()));
            model.addAttribute("categories", categoryDao.findAll());
            model.addAttribute(message.addFlashAttribute("message", "Field is in use and cannot be deleted"));
            return "redirect:/admin/category/delete";
        }
        categoryDao.deleteById(category.getId());
        message.addFlashAttribute("message", "Category Successfully Deleted!");
        return "redirect:/admin/menu";
    }

    @RequestMapping(value = "severity/delete", method = RequestMethod.GET)
    public String listDeleteSeverities(@ModelAttribute Severity severity, Model model) {
        model.addAttribute("title", "IMS - Delete Severity");
        model.addAttribute("subtitle", "Delete Severity");
        model.addAttribute("date", format.format(new Date()));
        model.addAttribute("severities", severityDao.findAll());
        return "admin/severity/delete";
    }

    @RequestMapping(value = "severity/delete", method = RequestMethod.POST)
    public String processDeleteSeverity(@ModelAttribute Severity severity, RedirectAttributes message, Model model) {
        // check if any parent records exist for the severity to be deleted, if any are present disallow delete and display message
        int count = imsDao.countTicketBySeverity(severity);
        if (count > 0) {
            model.addAttribute("title", "IMS - Delete Severity");
            model.addAttribute("subtitle", "Delete Severity");
            model.addAttribute("date", format.format(new Date()));
            model.addAttribute("severities", severityDao.findAll());
            model.addAttribute(message.addFlashAttribute("message", "Field is in use and cannot be deleted"));
            return "redirect:/admin/severity/delete";
        }
        severityDao.deleteById(severity.getId());
        message.addFlashAttribute("message", "Severity Successfully Deleted!");
        return "redirect:/admin/menu";
    }
}
