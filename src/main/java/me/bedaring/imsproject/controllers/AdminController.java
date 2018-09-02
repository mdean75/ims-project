package me.bedaring.imsproject.controllers;

import me.bedaring.imsproject.EmailService;
import me.bedaring.imsproject.Mail;
import me.bedaring.imsproject.models.*;
import me.bedaring.imsproject.models.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Michael DeAngelo
 * last update date: Aug 20, 2018
 * purpose: Controller class for the admin features.
 */
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

    @Autowired
    private UserDao userDao;

    @Autowired
    private CarrierDao carrierDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private EmailService emailService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    // date format to use for display timestamp
    private SimpleDateFormat format = new SimpleDateFormat("EEEE MMMM d, y - hh:mm:ss aa");

    /**
     * This method supplies the request mapping to display the admin menu
     * @param model used to supply attributes to the view
     * @return template view
     */
    @RequestMapping(value="menu")
    public String index(Model model) {
        model.addAttribute("title", "IMS - Admin Menu");
        model.addAttribute("date", format.format(new Date()));
        return "admin/menu";
    }

    /**
     * This method supplies the GET request mapping to display the add group form
     * @param model used to supply attributes to the view
     * @return template view
     */
    @RequestMapping(value = "group/add", method = RequestMethod.GET)
    public String displayAddGroup(Model model) {
        model.addAttribute("title", "IMS - Add Group");
        model.addAttribute("subtitle", "Add Group");
        model.addAttribute("group", new AssignedGroup());
        model.addAttribute("date", format.format(new Date()));
        return "admin/group/add";
    }

    /**
     * This method supplies the POST request mapping to process adding a group
     * @param group AssignedGroup object to get details of group being created
     * @param errors Errors of current group being updated
     * @param model used to supply attributes to the view
     * @param message RedirectAttributes used to add a flash message for successful adding a group
     * @return template view
     */
    @RequestMapping(value = "group/add", method = RequestMethod.POST)
    public String processAddGroup(@Valid @ModelAttribute("group") AssignedGroup group, Errors errors, Model model,
                                  RedirectAttributes message) {
        // if errors are present add the required attributes
        if (errors.hasErrors()) {
            model.addAttribute("title", "IMS - Add Group");
            model.addAttribute("subtitle", "Add Group");
            model.addAttribute("date", format.format(new Date()));
            return "admin/group/add";
        }

        // validation passed, save(add) the new group, then redirect to the admin menu and display a success flash message
        groupDao.save(group);
        message.addFlashAttribute("message", "Successfully added new group");
        return "redirect:/admin/menu";
    }

    /**
     * This method supplies the GET request mapping to display the add severity form
     * @param model used to supply attributes to the view
     * @return template view
     */
    @RequestMapping(value = "severity/add", method = RequestMethod.GET)
    public String displayAddSeverity(Model model) {
        model.addAttribute("title", "IMS - Add Severity");
        model.addAttribute("subtitle", "Add Severity");
        model.addAttribute("severity", new Severity());
        model.addAttribute("date", format.format(new Date()));
        return "admin/severity/add";
    }

    /**
     * This method supplies the POST request mapping to process adding a severity
     * @param severity Severity object used to create new severity level
     * @param errors Errors on validating user input
     * @param model used to supply attributes to the view
     * @param message RedirectAttributes used to add a flash message for successful adding severity
     * @return template view
     */
    @RequestMapping(value = "severity/add", method = RequestMethod.POST)
    public String processAddSeverity(@Valid @ModelAttribute("severity") Severity severity, Errors errors,  Model model,
                                     RedirectAttributes message) {
        // if errors are present add the required attributes and redisplay
        if (errors.hasErrors()) {
            model.addAttribute("title", "IMS - Add Severity");
            model.addAttribute("subtitle", "Add Severity");
            model.addAttribute("date", format.format(new Date()));
            return "admin/severity/add";
        }
        // validation passed, save(add) the new severity, then redirect to the admin menu and display a success flash message
        severityDao.save(severity);
        message.addFlashAttribute("message", "New severity level added successfully");
        return "redirect:/admin/menu";
    }

    /**
     * This method supplies the GET request mapping to display the add category form
     * @param model used to supply attributes to the view
     * @return template view
     */
    @RequestMapping(value = "category/add", method = RequestMethod.GET)
    public String displayAddCategory(Model model) {
        model.addAttribute("title", "IMS - Add Category");
        model.addAttribute("subtitle", "Add Category");
        model.addAttribute("date", format.format(new Date()));
        model.addAttribute("category", new Category());
        return "admin/category/add";
    }

    /**
     * This method supplies the POST request mapping to process adding a category
     * @param category Category object used to create new category
     * @param errors Errors on validating user input
     * @param model used to supply attributes to the view
     * @param message RedirectAttributes used to add a flash message for successful adding category
     * @return template view
     */
    @RequestMapping(value = "category/add", method = RequestMethod.POST)
    public String processAddCategory(@Valid @ModelAttribute("category") Category category, Errors errors, Model model,
                                     RedirectAttributes message) {
        // if errors are present add the required attributes and redisplay
        if (errors.hasErrors()) {
            model.addAttribute("title", "IMS - Add Category");
            model.addAttribute("subtitle", "Add Category");
            model.addAttribute("date", format.format(new Date()));
            return "admin/category/add";

        }
        // validation passed, save(add) the new category, then redirect to the admin menu and display a success flash message
        categoryDao.save(category);
        message.addFlashAttribute("message", "New category successfully added");
        return "redirect:/admin/menu";
    }

    /**
     * This method supplies the GET request mapping to display the group update form
     * @param assignedGroup AssignedGroup object used to get details about the assigned groups in the system
     * @param model used to supply attributes to the view
     * @return template view
     */
    @RequestMapping(value = "group/update", method = RequestMethod.GET)
    public String listUpdateGroups(@ModelAttribute AssignedGroup assignedGroup, Model model) {
        model.addAttribute("title", "IMS - Update Group");
        model.addAttribute("subtitle", "Update Group");
        model.addAttribute("date", format.format(new Date()));
        model.addAttribute("groups", groupDao.findAllByOrderByGroupName());
        return "admin/group/update";

    }

    /**
     * This method supplies the POST request mapping to process updating a group
     * @param assignedGroup AssignedGroup object used to get details about the assigned groups in the system
     * @param errors Errors on validating user input
     * @param message RedirectAttributes used to add a flash message for successful updating group
     * @return template view
     */
    @RequestMapping(value = "group/update", method = RequestMethod.POST)
    public String processUpdateGroup(@Valid @ModelAttribute("assignedGroup") AssignedGroup assignedGroup,
                                     Errors errors, RedirectAttributes message) {
        // if errors are present redisplay the group update page with a flash message
        if (errors.hasErrors()) {
            message.addFlashAttribute("message", "Group Name cannot be empty");
            return "redirect:/admin/group/update";
        }
        // validation passed, save(update) the group, then redirect to the admin menu and display a success flash message
        groupDao.save(assignedGroup);
        message.addFlashAttribute("message", "Group Successfully Updated!");
        return "redirect:/admin/menu";
    }

    /**
     * This method supplies the GET request mapping to display the severity update form
     * @param severity Severity object used to get details about the severity levels in the system
     * @param model used to supply attributes to the view
     * @return template view
     */
    @RequestMapping(value = "severity/update", method = RequestMethod.GET)
    public String listUpdateSeverities(@ModelAttribute Severity severity, Model model) {
        model.addAttribute("title", "IMS - Update Severity");
        model.addAttribute("subtitle", "Update Severity");
        model.addAttribute("date", format.format(new Date()));
        model.addAttribute("severities", severityDao.findAll());
        return "admin/severity/update";
    }

    /**
     * This method supplies the POST request mapping to process updating a severity
     * @param severity Severity object used to get details about the severity levels in the system
     * @param errors Errors on validating user input
     * @param message RedirectAttributes used to add a flash message for successful updating severity
     * @return template view
     */
    @RequestMapping(value = "severity/update", method = RequestMethod.POST)
    public String processUpdateSeverity(@Valid @ModelAttribute("severity") Severity severity, Errors errors,
                                        RedirectAttributes message) {
        // if errors are present redisplay the update severity page with a flash message
        if (errors.hasErrors()) {
            message.addFlashAttribute("message", "Severity Name cannot be empty");
            return "redirect:/admin/severity/update";
        }
        // validation passed, save(update) the severity, then redirect to the admin menu and display a success flash message
        severityDao.save(severity);
        message.addFlashAttribute("message", "Severity Successfully Updated!");
        return "redirect:/admin/menu";
    }

    /**
     * This method supplies the GET request mapping to display the category update form
     * @param category Category object used to get details about the categories in the system
     * @param model used to supply attributes to the view
     * @return template view
     */
    @RequestMapping(value = "category/update", method = RequestMethod.GET)
    public String listUpdateCategories(@ModelAttribute Category category, Model model) {
        model.addAttribute("title", "IMS - Update Category");
        model.addAttribute("subtitle", "Update Category");
        model.addAttribute("date", format.format(new Date()));
        model.addAttribute("categories", categoryDao.findAllByOrderByCategoryTypeAscCategoryNameAsc());
        return "admin/category/update";
    }

    /**
     * This method supplies the POST request mapping to process updating a category
     * @param category Category object used to update the given category
     * @param errors Errors on validating user input
     * @param message RedirectAttributes used to add a flash message for successful updating category
     * @return template view
     */
    @RequestMapping(value = "category/update", method = RequestMethod.POST)
    public String processUpdateCategory(@Valid @ModelAttribute Category category, Errors errors,
                                        RedirectAttributes message) {
        // if errors are present redisplay the update category page with a flash message
        if (errors.hasErrors()) {
            message.addFlashAttribute("message", "Name and Type are both required");
            return "redirect:/admin/category/update";
        }
        // validation passed, save(update) the category, then redirect to the admin menu and display a success flash message
        categoryDao.save(category);
        message.addFlashAttribute("message", "Category Successfully Updated!");
        return "redirect:/admin/menu";
    }

    /**
     * This method supplies the GET request mapping to display the list of groups and for each group a form is
     * displayed to allow them to be deleted.
     * @param assignedGroup AssignedGroup object used to get details about all groups in the system
     * @param model used to supply attributes to the view
     * @return template view
     */
    @RequestMapping(value = "group/delete", method = RequestMethod.GET)
    public String listDeleteGroups(@ModelAttribute AssignedGroup assignedGroup, Model model) {
        model.addAttribute("title", "IMS - Delete Group");
        model.addAttribute("subtitle", "Delete Group");
        model.addAttribute("date", format.format(new Date()));
        model.addAttribute("groups", groupDao.findAll());
        return "admin/group/delete";
    }

    /**
     * This method supplies the POST request mapping to process deleting a group
     * @param assignedGroup AssignedGroup object used to get the group to be deleted
     * @param message RedirectAttributes used to add a flash message for successful deleting group
     * @param model used to supply attributes to the view
     * @return template view
     */
    @RequestMapping(value = "group/delete", method = RequestMethod.POST)
    public String processDeleteGroup(@ModelAttribute AssignedGroup assignedGroup,
                                     RedirectAttributes message, Model model) {
        // first check if any parent records exist for the group to be deleted,
        // if any are present disallow delete and display message
        int count = imsDao.countTicketsByAssignedGroupEquals(assignedGroup) + userDao.countUsersByGroupIdEquals(assignedGroup);

        // group is present in at least 1 ticket or user record
        if (count > 0) {
            model.addAttribute(message.addFlashAttribute("message", "Selected group is in use and cannot be deleted"));
            return "redirect:/admin/group/delete";
        }

        // this group is not currently being used and can be deleted, delete then redirect with success message
        groupDao.deleteById(assignedGroup.getId());
        message.addFlashAttribute("message", "Group Successfully Deleted!");
        return "redirect:/admin/menu";
    }

    /**
     * This method supplies the GET request mapping to display the list of categories and for each category a form is
     * displayed to allow them to be deleted.
     * @param category Category object to get details of all categories in the system
     * @param model used to supply attributes to the view
     * @return template view
     */
    @RequestMapping(value = "category/delete", method = RequestMethod.GET)
    public String listDeleteCategories(@ModelAttribute Category category, Model model) {
        model.addAttribute("title", "IMS - Delete Category");
        model.addAttribute("subtitle", "Delete Category");
        model.addAttribute("date", format.format(new Date()));
        model.addAttribute("categories", categoryDao.findAllByOrderByCategoryTypeAscCategoryNameAsc());
        return "admin/category/delete";
    }

    /**
     * This method supplies the POST request mapping to process deleting a category
     * @param category Category object used to get details of category being deleted
     * @param message RedirectAttributes used to add a flash message for successful deleting category
     * @return template view
     */
    @RequestMapping(value = "category/delete", method = RequestMethod.POST)
    public String processDeleteCategory(@ModelAttribute Category category, RedirectAttributes message) {

        // check if any parent records exist for the category to be deleted, if any are present disallow delete and display message
        int count = imsDao.countTicketByCategory(category);
        if (count > 0) {
            message.addFlashAttribute("message", "Selected category is in use and cannot be deleted");
            return "redirect:/admin/category/delete";
        }
        // this category is not currently being used and can be deleted, delete then redirect with success message
        categoryDao.deleteById(category.getId());
        message.addFlashAttribute("message", "Category Successfully Deleted!");
        return "redirect:/admin/menu";
    }

    /**
     * This method supplies the GET request mapping to display the list of severities and for each severity a form is
     * displayed to allow them to be deleted.
     * @param severity Severity object to get details of all severities in the system
     * @param model used to supply attributes to the view
     * @return template view
     */
    @RequestMapping(value = "severity/delete", method = RequestMethod.GET)
    public String listDeleteSeverities(@ModelAttribute Severity severity, Model model) {
        model.addAttribute("title", "IMS - Delete Severity");
        model.addAttribute("subtitle", "Delete Severity");
        model.addAttribute("date", format.format(new Date()));
        model.addAttribute("severities", severityDao.findAll());
        return "admin/severity/delete";
    }

    /**
     * This method supplies the POST request mapping to process deleting a severity
     * @param severity Severity object used to get details of severity being deleted
     * @param message RedirectAttributes used to add a flash message for successful deleting severity
     * @return template view
     */
    @RequestMapping(value = "severity/delete", method = RequestMethod.POST)
    public String processDeleteSeverity(@ModelAttribute Severity severity, RedirectAttributes message) {
        // check if any parent records exist for the severity to be deleted, if any are present disallow delete and display message
        int count = imsDao.countTicketBySeverity(severity);
        if (count > 0) {
            message.addFlashAttribute("message", "Selected severity is in use and cannot be deleted");
            return "redirect:/admin/severity/delete";
        }
        // this severity is not currently being used and can be deleted, delete then redirect with success message
        severityDao.deleteById(severity.getId());
        message.addFlashAttribute("message", "Severity Successfully Deleted!");
        return "redirect:/admin/menu";
    }

    /**
     * This method supplies the GET request mapping to display the add user form
     * @param model used to supply attributes to the view
     * @return template view
     */
    @RequestMapping(value = "user/add", method = RequestMethod.GET)
    public String displayAddUser(Model model) {
        model.addAttribute("title", "IMS - Add User");
        model.addAttribute("subtitle", "Add User");
        model.addAttribute("user", new User());
        model.addAttribute("carriers", carrierDao.findAll());
        model.addAttribute("groups", groupDao.findAll());
        model.addAttribute("roles", roleDao.findAll());
        model.addAttribute("date", format.format(new Date()));
        return "admin/user/add";
    }

    /**
     * This method supplies the POST request mapping to process adding a user
     * @param user User object to get details of user being added
     * @param errors Errors on validating user input
     * @param message RedirectAttributes used to add a flash message for successful adding user
     * @param model used to supply attributes to the view
     * @return template view
     */
    @RequestMapping(value = "user/add", method = RequestMethod.POST)
    public String processAddUser(@Valid @ModelAttribute("user") User user, Errors errors,
                                 RedirectAttributes message, Model model, HttpServletRequest request) {
        // if errors are present add the required attributes and redisplay
        if (errors.hasErrors()) {
            model.addAttribute("title", "IMS - Add User");
            model.addAttribute("subtitle", "Add User");
            model.addAttribute("carriers", carrierDao.findAll());
            model.addAttribute("groups", groupDao.findAll());
            model.addAttribute("roles", roleDao.findAll());
            model.addAttribute("date", format.format(new Date()));
            return "admin/user/add";
        }
        // first check to ensure there are no existing users with the supplied username or email, if so redisplay with error message
        if (userDao.countUsersByUsernameOrEmail(user.getUsername(), user.getEmail()) > 0) {
            message.addFlashAttribute("message", "A user already exists with that username and/or email");
            return "redirect:/admin/user/add";
        }

        // create random plain text password then encrypt before saving using BCrypt
        String plainPassword = User.createRandomPassword(24);

        user.setPassword(bCryptPasswordEncoder.encode(plainPassword));

        // create timestamp to use for token and token expiration
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        // create a new token from random string plus current timestamp and set to user timestamp field
        String token = User.createRandomPassword(16) + timestamp.getTime();
        user.setToken(token);

        // set the token expiration for timestamp plus 24 hours then set the user token expiration
        timestamp.setTime(timestamp.getTime() + ( (24 * 60 * 60 ) * 1000) );
        user.setTokenExpiration(timestamp);

        // save (create) the new user and send email to the user with their credentials, also set flash message on success
        try {
            userDao.save(user);

            // create the email
            Mail mail = new Mail();
            mail.setFrom("bedaring.me@gmail.com");
            mail.setTo(user.getEmail());
            mail.setSubject("New account setup");
            //Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String msg = "A new user account has been created for you with the below listed username. \n\n\t" +
                    "username:  " + user.getUsername() + "\n\n Click on this link or copy and paste it into your " +
                    "browser to activate your account and choose a password.  Passwords must be at least 8 characters " +
                    "in length, contain at least 1 uppercase character, 1 lowercase character and 1 number.  " +
                    "\n\n This is a one time use link and will expire in 24 hours.  https://" + request.getServerName() +
                    "/profile/activate?token=" + token;

            mail.setContent(msg);
            // send the email
            emailService.sendSimpleMessage(mail);

            message.addFlashAttribute("message", "Successfully Added New User");
        } catch (ConstraintViolationException e) {
            System.out.println("there was a constraint error: " + e.getConstraintViolations());
            message.addFlashAttribute("message", "Error creating new user");
        }

        return "redirect:/admin/menu";
    }

    /**
     * This method supplies the GET request mapping to display the add carrier form
     * @param model used to supply attributes to the view
     * @return template view
     */
    @RequestMapping(value = "carrier/add", method = RequestMethod.GET)
    public String displayAddCarrier(Model model) {
        model.addAttribute("title", "IMS - Add Carrier");
        model.addAttribute("subtitle", "Add Carrier");
        model.addAttribute("carrier", new Carrier());
        model.addAttribute("date", format.format(new Date()));
        return "admin/carrier/add";
    }

    /**
     * This method supplies the POST request mapping to process adding a carrier
     * @param carrier Carrier object to user to create a new carrier
     * @param errors Errors on validating user input
     * @param model used to supply attributes to the view
     * @param message RedirectAttributes used to add a flash message for successful adding carrier
     * @return template view
     */
    @RequestMapping(value = "carrier/add", method = RequestMethod.POST)
    public String processAddCarrier(@Valid @ModelAttribute("carrier") Carrier carrier, Errors errors,  Model model,
                                     RedirectAttributes message) {
        // if errors are present add the required attributes
        if (errors.hasErrors()) {
            model.addAttribute("title", "IMS - Add Carrier");
            model.addAttribute("subtitle", "Add Carrier");
            model.addAttribute("date", format.format(new Date()));
            return "admin/carrier/add";
        }
        // validation passed, save(add) the new carrier, then redirect to the admin menu and display a success flash message
        carrierDao.save(carrier);
        message.addFlashAttribute("message", "New carrier added successfully");
        return "redirect:/admin/menu";
    }

    /**
     * This method supplies the GET request mapping to display the carrier update form
     * @param carrier Carrier object to get details about each carrier in the system
     * @param model used to supply attributes to the view
     * @return template view
     */
    @RequestMapping(value = "carrier/update", method = RequestMethod.GET)
    public String listUpdateCarrier(@ModelAttribute Carrier carrier, Model model) {
        model.addAttribute("title", "IMS - Update Carrier");
        model.addAttribute("subtitle", "Update Carrier");
        model.addAttribute("date", format.format(new Date()));
        model.addAttribute("carriers", carrierDao.findAll());
        return "admin/carrier/update";

    }

    /**
     * This method supplies the POST request mapping to process updating a carrier
     * @param carrier Carrier object to get details about each carrier in the system
     * @param errors Errors on validating user input
     * @param message RedirectAttributes used to add a flash message for successful updating carrier
     * @return template view
     */
    @RequestMapping(value = "carrier/update", method = RequestMethod.POST)
    public String processUpdateCarrier(@Valid @ModelAttribute Carrier carrier,
                                     Errors errors, RedirectAttributes message) {
        // if errors are present add the required attributes
        if (errors.hasErrors()) {
            message.addFlashAttribute("message", "Carrier Name and domain are both required");
            return "redirect:/admin/carrier/update";
        }
        // validation passed, save(update) the carrier, then redirect to the admin menu and display a success flash message
        carrierDao.save(carrier);
        message.addFlashAttribute("message", "Carrier Successfully Updated!");
        return "redirect:/admin/menu";
    }

    /**
     * This method supplies the GET request mapping to display the list of carriers and for each carrier a form is
     * displayed to allow them to be deleted.
     * @param carrier Carrier object to get details about each carrier in the system
     * @param model used to supply attributes to the view
     * @return template view
     */
    @RequestMapping(value = "carrier/delete", method = RequestMethod.GET)
    public String listDeleteCarriers(@ModelAttribute Carrier carrier, Model model) {
        model.addAttribute("title", "IMS - Delete Carrier");
        model.addAttribute("subtitle", "Delete Carrier");
        model.addAttribute("date", format.format(new Date()));
        model.addAttribute("carriers", carrierDao.findAll());
        return "admin/carrier/delete";
    }

    /**
     * This method supplies the POST request mapping to process deleting a carrier
     * @param carrier Carrier object to get details about each carrier in the system
     * @param message RedirectAttributes used to add a flash message for successful deleting carrier
     * @return template view
     */
    @RequestMapping(value = "carrier/delete", method = RequestMethod.POST)
    public String processDeleteCarriers(@ModelAttribute Carrier carrier,
                                     RedirectAttributes message) {
        // check if any parent records exist for the group to be deleted, if any are present disallow delete and display message
        int count = userDao.countUserByCarrierId(carrier);
        if (count > 0) {
            message.addFlashAttribute("message", "That carrier is in use and cannot be deleted");
            return "redirect:/admin/carrier/delete";
        }
        // this carrier is not currently being used and can be deleted, delete then redirect with success message
        carrierDao.deleteById(carrier.getId());
        message.addFlashAttribute("message", "Carrier Successfully Deleted!");
        return "redirect:/admin/menu";
    }

    /**
     * This method supplies the GET request mapping to display the list of users to select a user to update
     * @param user User object to get details about each user in the system
     * @param model used to supply attributes to the view
     * @return template view
     */
    @RequestMapping(value = "user/list")
    public String listAllUsers(@ModelAttribute User user, Model model) {
        model.addAttribute("title", "IMS - List Users");
        model.addAttribute("subtitle", "User List");
        model.addAttribute("date", format.format(new Date()));
        model.addAttribute("users", userDao.findAll());

        return "admin/user/list";
    }

    /**
     * This method displays the GET request mapping to display the user record to be updated
     * @param user User object to get details about the user being updated
     * @param model used to supply attributes to the view
     * @param id user id of user to get details of
     * @return template view
     */
    @RequestMapping(value = "user/update/{id}", method = RequestMethod.GET)
    public String displayUpdateUser(@ModelAttribute User user, Model model, @PathVariable int id) {
        model.addAttribute("user", userDao.findById(id));
        model.addAttribute("title", "IMS - Update User");
        model.addAttribute("subtitle", "Update User");
        model.addAttribute("date", format.format(new Date()));
        model.addAttribute("groups", groupDao.findAll());
        model.addAttribute("carriers", carrierDao.findAll());
        model.addAttribute("roles", roleDao.findAll());

        return "admin/user/update";
    }

    /**
     * This method supplies the POST request mapping to process updating a user.
     * @param user User object to get details about the user being updated
     * @param model used to supply attributes to the view
     * @param id user id to be updated
     * @param errors Errors on validating user input
     * @param message RedirectAttributes used to add a flash message for successful updating user
     * @return template view
     */
    @RequestMapping(value = "user/update/{id}", method = RequestMethod.POST)
    public String processUpdateUser(@Valid @ModelAttribute User user, Errors errors, Model model,
                                    @PathVariable int id, RedirectAttributes message) {

        // if errors are present add the required attributes
        if (errors.hasErrors()) {
            model.addAttribute("title", "IMS - Update User");
            model.addAttribute("subtitle", "Update User");
            model.addAttribute("date", format.format(new Date()));
            model.addAttribute("groups", groupDao.findAll());
            model.addAttribute("carriers", carrierDao.findAll());
            model.addAttribute("roles", roleDao.findAll());

            return "admin/user/update";
        }

        // check if username or email was changed, if either were changed then check to ensure uniqueness among the other users
        // if trying to change to an existing username or email, disallow update and redirect with error message
        if(!user.getUsername().equals(userDao.findUserById(id).getUsername()) ||
                !user.getEmail().equals(userDao.findUserById(id).getEmail())) {

            // there can be at most 1 record with the entered credentials
            if (userDao.countUsersByUsernameOrEmail(user.getUsername(), user.getEmail()) > 1) {
                message.addFlashAttribute("message", "Username or email matches an existing user");
                return "redirect:/admin/menu";
            }
        }

        // add existing password to the model before saving
        User updateUser = userDao.findUserById(id);
        user.setPassword(updateUser.getPassword());

        // save (update) the user then redirect with success message
        userDao.save(user);
        message.addFlashAttribute("message", "User successfully updated");
        return "redirect:/admin/menu";
    }

    /**
     * This method supplies the GET request mapping to display the list of users and for each user a form is displayed to
     * allow them to be deleted.
     * @param user User object to get details about each user in the system
     * @param model used to supply attributes to the view
     * @return template view
     */
    @RequestMapping(value = "user/delete", method = RequestMethod.GET)
    public String displayUserDelete(@ModelAttribute User user, Model model) {
        model.addAttribute("title", "IMS - Delete Users");
        model.addAttribute("subtitle", "User Delete List");
        model.addAttribute("date", format.format(new Date()));
        model.addAttribute("users", userDao.findAll());

        return "admin/user/delete";
    }

    /**
     * This method supplies the POST request mapping to process deleting a user
     * @param id user id to be deleted
     * @param message RedirectAttributes used to add a flash message for successful deleting user
     * @return template view
     */
    @RequestMapping(value = "user/delete", method = RequestMethod.POST)
    public String processUserDelete(@RequestParam int id, RedirectAttributes message) {
        // first check if user is assigned to tickets and disallow delete if any records exist and redirect with error message
        if (imsDao.countTicketByAssignedPerson(userDao.findUserById(id)) > 0) {
            message.addFlashAttribute("message", "User is assigned to tickets and cannot be deleted");
        }else {
            // user is ok to delete since not assigned to any tickets, delete then redirect with success message
            userDao.deleteById(id);
            message.addFlashAttribute("message", "User successfully deleted");
        }

        return "redirect:/admin/menu";
    }
}
