package me.bedaring.imsproject.controllers;

import me.bedaring.imsproject.models.*;
import me.bedaring.imsproject.models.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

/**
 * @author Michael DeAngelo
 * last update date: Aug 14, 2018
 * purpose: Controller class for all incident ticket crud features
 * note:    The terms incident and ticket are used interchangeably and have the same meaning
 */
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

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    // date format to use for display timestamp
    private SimpleDateFormat format = new SimpleDateFormat("EEEE MMMM d, y - hh:mm:ss aa");

    /**
     * This method supplies the mapping for the ticket main landing page via GET
     * @param model used to supply attributes to the view
     * @return      view template
     */
    @RequestMapping(value = "main", method = RequestMethod.GET)
    public String main(Model model) {
        // field to store username of currently logged in user
        String username;

        /* obtain the name of the currently authenticated user (from spring-security docs
           https://docs.spring.io/spring-security/site/docs/4.0.2.RELEASE/reference/htmlsingle/#obtaining-information-about-the-current-user)
        */
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        } // end of obtaining username

        // add attributes to be sent to the view
        model.addAttribute("title", "IMS - Main");
        model.addAttribute("username", username);
        model.addAttribute("date", format.format(new Date()));
        return "ticket/main";
    }

    /**
     * This method supplies the mapping to process the ticket lookup form
     * @param id ticket number to view full incident
     * @return view template
     */
    @RequestMapping(value = "main", method = RequestMethod.POST)
    public String mainPost(@RequestParam int id) {
        return "redirect:/ticket/view/"+id;
    }

    /**
     * This method supplies the mapping for viewing the list of tickets. It contains an optional path variable id to
     * alter the results. Without the id variable the list contains tickets assigned to the current users' group,
     * id = 1 filters the list to only show tickets assigned to the current user, and id = 2 shows all tickets in the
     * system and is restricted by SecurityConfiguration to "SUPPORT" users.
     * @param id optional path variable used to limit results
     * @param customUserDetails used to get detail about the logged in user
     * @param model used to supply attributes to the view
     * @return view template
     */
    @RequestMapping(value = {"list/{id}", "list"})
    public String list(@PathVariable(required = false) Optional<Integer> id,
                       @AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {

        // add attributes to be sent to the view
        model.addAttribute("username", customUserDetails.getUsername());
        model.addAttribute("roles", customUserDetails.getRoles());

        model.addAttribute("title", "IMS - List Tickets");
        model.addAttribute("severities", severityDao.findAll());
        model.addAttribute("date", format.format(new Date()));

        // get the logged in user's group id to select records belonging to that group
        int group = customUserDetails.getGroupId().getId();

        // check if the optional id is present and if true process based on the value
        if (id.isPresent()) {

            // get tickets assigned to the logged in user only and add to the model
            if (id.get() == 1) {
                model.addAttribute("tickets", imsDao.findAllByAssignedPersonId(customUserDetails.getId()));

            // get all tickets in the system and add to the model
            }else if (id.get() == 2) {
                model.addAttribute("tickets", imsDao.findAll());
            }

        // default - get tickets assigned to the user's group and add to the model
        }else {
            model.addAttribute("tickets", imsDao.findAllByAssignedGroupId(group));
        }
        return "ticket/list";
    }

    /**
     * This method supplies the mapping for viewing a specific ticket and uses GET method.
     * @param customUserDetails used to get detail about the logged in user
     * @param model used to supply attributes to the view
     * @param id id number of the ticket
     * @return view template
     */
    @RequestMapping(value = "view/{id}", method = RequestMethod.GET)
    public String viewTicket(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model, @PathVariable String id) {
        Optional<Ticket> tickets = imsDao.findById(Integer.valueOf(id));
        model.addAttribute("title", "IMS - View Ticket");

        model.addAttribute("incident", imsDao.findById(Integer.valueOf(id)));
        model.addAttribute("groups", groupDao.findAll());
        model.addAttribute("severities", severityDao.findAll());
        model.addAttribute("subcategory", categoryDao.findCategoryByCategoryTypeEquals("Sub"));
        model.addAttribute("maincategory", categoryDao.findCategoryByCategoryTypeEquals("Main"));
        model.addAttribute("detailcategory", categoryDao.findCategoryByCategoryTypeEquals("Detail"));
        model.addAttribute("statuses", statusDao.findAll());
        model.addAttribute("date", format.format(new Date()));
        model.addAttribute("persons", userDao.findAll());

        // only if ticket is assigned to the user's group, or the user has role SUPPORT add an attribute to allow the update
        // button to be rendered
        if ((tickets.get().getAssignedGroup().getId() == customUserDetails.getGroupId().getId()) ||
                (AuthorityUtils.authorityListToSet(SecurityContextHolder.getContext().getAuthentication()
                        .getAuthorities()).contains("ROLE_SUPPORT"))){
            model.addAttribute("render", true);
        }

        return "ticket/view";
    }

    /**
     * This method supplies the mapping to process the POST request for updating an incident
     * @param newTicket Ticket object to get details of the ticket being updated
     * @param errors Errors of current ticket being updated
     * @param assignedGroup id of AssignedGroup of the ticket
     * @param severity id of Severity of the ticket
     * @param categoryMain id of main category of the ticket
     * @param status id of status of the ticket
     * @param id id of the ticket being updated
     * @param model used to supply attributes to the view
     * @return view template
     */
    @RequestMapping(value = "view/{id}", method = RequestMethod.POST)
    public String updateTicket(@ModelAttribute @Valid Ticket newTicket, Errors errors,
                               @RequestParam int assignedGroup, @RequestParam int severity,
                               @RequestParam int categoryMain, @RequestParam int status,
                               @RequestParam int id, Model model, RedirectAttributes message){

        // if user did not add update text, re-display the ticket and display a message to the user
        if (newTicket.getUpdate().isEmpty()) {
            message.addFlashAttribute("message", "Update text is required");
            return "redirect:/ticket/view/{id}";
        }

        // if errors are present add the required attributes
        if(errors.hasErrors()) {
            model.addAttribute("incident", imsDao.findById(id));
            model.addAttribute("title", "IMS - update Ticket");
            model.addAttribute("groups", groupDao.findAll());
            model.addAttribute("severities", severityDao.findAll());
            model.addAttribute("categories", categoryDao.findAllByOrderByCategoryTypeAscCategoryNameAsc());
            model.addAttribute("statuses", statusDao.findAll());
            model.addAttribute("date", format.format(new Date()));
            return "ticket/view";
        }

        // when a ticket is reassigned to a different group, clear the assignedPerson by setting it to a special "user"
        // that isn't a real user and has value 0. This will prevent a user from having access to the ticket after it
        // is no longer assigned to their group
        if (imsDao.findById(id).get().getAssignedGroup().getId() != newTicket.getAssignedGroup().getId()) {
            newTicket.setAssignedPerson(userDao.findUserById(0));
        }

        //Optional<Ticket> ticket = imsDao.findById(id);
        // get the group assigned to this ticket by using the value of the assignedGroup parameter
        Optional<AssignedGroup> group = groupDao.findById(assignedGroup);
        // get the severity assigned to this ticket by using the value of the severity parameter
        Optional<Severity> displaySeverity = severityDao.findById(severity);
        // get the main category assigned to this ticket by using the value of the categoryMain parameter
        Optional<Category> category = categoryDao.findById(categoryMain);
        // get the status assigned to this ticket by using the value of the status parameter
        Optional<Status> statusD = statusDao.findById(status);

        // set the id, assignedGroup, severity, categoryMain, and status of the ticket so it is updated correctly
        newTicket.setId(id);
        newTicket.setAssignedGroup(group);
        newTicket.setSeverity(displaySeverity);
        newTicket.setCategoryMain(category);
        newTicket.setStatus(statusD);

        // get the currently authenticated user and build the update log then save the ticket
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        newTicket.setLog(newTicket.getLog().append("\n").append(new Date()).append(" updated by: ").append(user)
                .append("\n").append(newTicket.getUpdate()));

        imsDao.save(newTicket);
        return "redirect:/ticket/main";
    }

    /**
     * This method supplies the mapping to create a ticket.  This is the GET request to display the create ticket form.
     * @param model used to supply attributes to the view
     * @return view template
     */
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
        model.addAttribute("users", userDao.findAll());
        return "ticket/create";
    }

    /**
     * This method supplies the mapping for processing the create ticket form.
     * @param newTicket Ticket object to get details of the ticket being created
     * @param errors Errors of current ticket being updated
     * @param model used to supply attributes to the view
     * @return view template
     */
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String processCreateTicket(@ModelAttribute @Valid Ticket newTicket, Errors errors,
                                      Model model, RedirectAttributes message) {

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

        // save new ticket and assign to variable so we can get the ID to display in the success message
        Ticket insertedTicket = imsDao.save(newTicket);

        message.addFlashAttribute("message", "New ticket number " + insertedTicket.getId() + " successfully created");
        return "redirect:/ticket/main";
    }
}
