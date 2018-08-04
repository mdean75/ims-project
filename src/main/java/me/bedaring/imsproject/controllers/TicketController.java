package me.bedaring.imsproject.controllers;

import me.bedaring.imsproject.models.*;
import me.bedaring.imsproject.models.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    SimpleDateFormat format = new SimpleDateFormat("EEEE MMMM d, y - hh:mm:ss aa");


    @PreAuthorize("permitAll()")
    @RequestMapping(value="")
    public String index(Model model) {
        model.addAttribute("title", "IMS - Home");
        model.addAttribute("date", format.format(new Date()));
        return "ticket/index";
    }

    @RequestMapping(value = "main", method = RequestMethod.GET)
    public String main(Model model) {
        String username;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        model.addAttribute("title", "IMS - Main");
        model.addAttribute("username", username);
        model.addAttribute("date", format.format(new Date()));
        return "ticket/main";
    }

    @RequestMapping(value = "main", method = RequestMethod.POST)
    public String mainPost(Model model, @RequestParam int id) {
        model.addAttribute("title", "IMS - Main");
        model.addAttribute("date", format.format(new Date()));
        return "redirect:/ticket/view/"+id;
    }

    @RequestMapping(value = {"list/{id}", "list"})
    public String list(@PathVariable(required = false) Optional<Integer> id, @AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {
        model.addAttribute("username", customUserDetails.getUsername());
        model.addAttribute("roles", customUserDetails.getRoles());

        model.addAttribute("title", "IMS - List Tickets");
        model.addAttribute("severities", severityDao.findAll());
        model.addAttribute("date", format.format(new Date()));

        if (id.isPresent()) {
            if (id.get() == 1) {
                model.addAttribute("tickets", imsDao.findAllByAssignedPersonEquals(customUserDetails.getUsername()));
            }else if (id.get() == 2) {
                model.addAttribute("tickets", imsDao.findAll());
            }
        }else {
            model.addAttribute("tickets", imsDao.findAllByAssignedGroupEquals(customUserDetails.getGroupId()));
        }
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
        model.addAttribute("date", format.format(new Date()));
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
            model.addAttribute("date", format.format(new Date()));
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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        newTicket.setLog(newTicket.getLog().append("\n").append(new Date()).append(" updated by: ").append(user)
                .append("\n").append(newTicket.getUpdate()));

        imsDao.save(newTicket);
        return "redirect:/ticket/main";
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPPORT')")
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
        model.addAttribute("date", format.format(new Date()));
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
            model.addAttribute("date", format.format(new Date()));
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
