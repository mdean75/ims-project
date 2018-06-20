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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("ticket")
public class ImsController {

    @Autowired
    private ImsDao imsDao;

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private SeverityDao severityDao;

    @Autowired
    private CategoryDao categoryDao;


    @RequestMapping(value="")
    public String index(Model model) {
        model.addAttribute("title", "IMS - Home");
        return "ticket/index";
    }

    @RequestMapping(value = "main", method = RequestMethod.GET)
    public String main(Model model) {
        model.addAttribute("title", "IMS - Main");
        return "ticket/main";
    }

    @RequestMapping(value = "main", method = RequestMethod.POST)
    public String mainPost(Model model, @RequestParam int id) {
        model.addAttribute("title", "IMS - Main");
        return "redirect:/ticket/view/"+id;
    }

    @RequestMapping(value = "list")
    public String list(Model model) {
        model.addAttribute("tickets", imsDao.findAll());
        model.addAttribute("title", "IMS - List Tickets");
        model.addAttribute("severities", severityDao.findAll());
        return "ticket/list";
    }

    @RequestMapping(value = "view/{id}")
    public String viewTicket(Model model, @PathVariable String id) {

        model.addAttribute("title", "IMS - View Ticket");
        model.addAttribute("id", id);
        model.addAttribute("incident", imsDao.findById(Integer.valueOf(id)));
        model.addAttribute("groups", groupDao.findAll());
        model.addAttribute("severities", severityDao.findAll());
        model.addAttribute("categories", categoryDao.findAll());
        return "ticket/view";
    }

    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String displayCreateTicket(Model model) {
        model.addAttribute("title", "IMS - Create Ticket");
        model.addAttribute("ticket", new Ticket());
        model.addAttribute("groups", groupDao.findAll());
        model.addAttribute("severities", severityDao.findAll());
        model.addAttribute("categories", categoryDao.findAll());
        return "ticket/create";
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String processCreateTicket(@ModelAttribute @Valid Ticket newTicket, Errors errors,
                                      @RequestParam int assignedGroup, @RequestParam int severity,
                                      @RequestParam int categoryMain, Model model) {

        if(errors.hasErrors()) {
            model.addAttribute("title", "IMS - Create Ticket");
            model.addAttribute("groups", groupDao.findAll());
            model.addAttribute("severities", severityDao.findAll());
            model.addAttribute("categories", categoryDao.findAll());
            return "ticket/create";
        }

        Optional<AssignedGroup> group = groupDao.findById(assignedGroup);
        Optional<Severity> displaySeverity = severityDao.findById(severity);
        Optional<Category> category = categoryDao.findById(categoryMain);
        newTicket.setAssignedGroup(group);
        newTicket.setSeverity(displaySeverity);
        newTicket.setCategoryMain(category);
        imsDao.save(newTicket);
        return "redirect:/ticket/main";
    }
}
