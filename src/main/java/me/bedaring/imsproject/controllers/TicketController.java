package me.bedaring.imsproject.controllers;

import me.bedaring.imsproject.models.*;
import me.bedaring.imsproject.models.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("ticket")
public class TicketController {

    @Autowired
    private ImsDao imsDao;

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private SeverityDao severityDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private StatusDao statusDao;


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

    @RequestMapping(value = "view/{id}", method = RequestMethod.GET)
    public String viewTicket(Model model, @PathVariable String id) {

        model.addAttribute("title", "IMS - View Ticket");
        //model.addAttribute("ticket", new Ticket());
       // model.addAttribute("id", id);
        model.addAttribute("incident", imsDao.findById(Integer.valueOf(id)));
        model.addAttribute("groups", groupDao.findAll());
        model.addAttribute("severities", severityDao.findAll());
        model.addAttribute("subcategory", categoryDao.findCategoryByCategoryTypeEquals("Sub"));
        model.addAttribute("maincategory", categoryDao.findCategoryByCategoryTypeEquals("Main"));
        model.addAttribute("detailcategory", categoryDao.findCategoryByCategoryTypeEquals("Detail"));
        model.addAttribute("statuses", statusDao.findAll());
        return "ticket/view";
    }

    @RequestMapping(value = "view/{id}", method = RequestMethod.POST)
    public String updateTicket(@ModelAttribute @Valid Ticket newTicket, Errors errors,
                               @RequestParam int assignedGroup, @RequestParam int severity,
                               @RequestParam int categoryMain, @RequestParam int status,
                               @RequestParam int id, Model model){

        if(errors.hasErrors()) {
            model.addAttribute("title", "IMS - update Ticket");
            model.addAttribute("groups", groupDao.findAll());
            model.addAttribute("severities", severityDao.findAll());
            model.addAttribute("categories", categoryDao.findAll());
            model.addAttribute("statuses", statusDao.findAll());
            return "ticket/view";
        }

        Optional<Ticket> ticket = imsDao.findById(id);
        Optional<AssignedGroup> group = groupDao.findById(assignedGroup);
        Optional<Severity> displaySeverity = severityDao.findById(severity);
        Optional<Category> category = categoryDao.findById(categoryMain);
        Optional<Status> statusD = statusDao.findById(status);

        newTicket.setId(id);
        newTicket.setAssignedGroup(group);
        newTicket.setSeverity(displaySeverity);
        newTicket.setCategoryMain(category);
        newTicket.setStatus(statusD);

        imsDao.save(newTicket);
        return "redirect:/ticket/main";
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String displayCreateTicket(Model model) {
        model.addAttribute("title", "IMS - Create Ticket");
        model.addAttribute("ticket", new Ticket());
        model.addAttribute("groups", groupDao.findAll());
        model.addAttribute("severities", severityDao.findAll());
        model.addAttribute("statuses", statusDao.findAll());
        model.addAttribute("subcategory", categoryDao.findCategoryByCategoryTypeEquals("Sub"));
        model.addAttribute("maincategory", categoryDao.findCategoryByCategoryTypeEquals("Main"));
        model.addAttribute("detailcategory", categoryDao.findCategoryByCategoryTypeEquals("Detail"));
        return "ticket/create";
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String processCreateTicket(@ModelAttribute @Valid Ticket newTicket, Errors errors,
                                      @RequestParam int assignedGroup, @RequestParam int severity,
                                      @RequestParam int categoryMain, @RequestParam int categorySub,
                                      @RequestParam int categoryDetail, @RequestParam int statusId, Model model) {

        if(errors.hasErrors()) {
            model.addAttribute("title", "IMS - Create Ticket");
            model.addAttribute("groups", groupDao.findAll());
            model.addAttribute("severities", severityDao.findAll());
            model.addAttribute("subcategory", categoryDao.findCategoryByCategoryTypeEquals("Sub"));
            model.addAttribute("maincategory", categoryDao.findCategoryByCategoryTypeEquals("Main"));
            model.addAttribute("detailcategory", categoryDao.findCategoryByCategoryTypeEquals("Detail"));
            model.addAttribute("statuses", statusDao.findAll());
            return "ticket/create";
        }

        Optional<AssignedGroup> group = groupDao.findById(assignedGroup);
        Optional<Severity> displaySeverity = severityDao.findById(severity);
        Optional<Category> mainCategory = categoryDao.findById(categoryMain);
        Optional<Category> subCategory = categoryDao.findById(categorySub);
        Optional<Category> detailCategory = categoryDao.findById(categoryDetail);
        Optional<Status> status = statusDao.findById(statusId);
        newTicket.setAssignedGroup(group);
        newTicket.setSeverity(displaySeverity);
        newTicket.setCategoryMain(mainCategory);
        newTicket.setCategorySub(subCategory);
        newTicket.setCategoryDetail(detailCategory);
        newTicket.setStatus(status);

        imsDao.save(newTicket);
        return "redirect:/ticket/main";
    }
}
