package me.bedaring.imsproject.controllers;

import me.bedaring.imsproject.EmailService;
import me.bedaring.imsproject.Mail;
import me.bedaring.imsproject.models.*;
import me.bedaring.imsproject.models.data.CarrierDao;
import me.bedaring.imsproject.models.data.GroupDao;
import me.bedaring.imsproject.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Michael DeAngelo
 * last update date: Aug 15, 2018
 * purpose: Controller class for the web root and other miscellaneous request mappings.  Mappings in this class include
 * document root, /profile, /password-change, /profile-update, /403 (for custom access denied page)
 */
@Controller
public class ImsController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private CarrierDao carrierDao;

    @Autowired
    private GroupDao groupDao;

    // date format to use for display timestamp
    private SimpleDateFormat format = new SimpleDateFormat("EEEE MMMM d, y - hh:mm:ss aa");

    @Autowired
    private EmailService emailService;

    /**
     * This method supplies the request mapping for the document root
     * @param model used to supply attributes to the view
     * @return view template
     */
    @RequestMapping(value="")
    public String index(Model model) {
        model.addAttribute("title", "IMS - Home");
        model.addAttribute("date", format.format(new Date()));
        return "ticket/index";
    }

    /**
     * This method supplies the request mapping for the GET request method for the profile page which displays 2 forms.
     * The first form allows users to change their password and the second allows users to update their mobile phone
     * number and carrier.
     * @param customUserDetails  used to get detail about the logged in user
     * @param model used to supply attributes to the view
     * @return profile view template
     */
    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String displayProfileUpdate(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {
        model.addAttribute("title", "IMS - Update Profile");
        model.addAttribute("subtitle", "Update Profile");
        model.addAttribute("date", format.format(new Date()));
        model.addAttribute("user", userDao.findById(customUserDetails.getId()));
        model.addAttribute("carriers", carrierDao.findAll());
        return "/profile/profile";
    }

    /**
     * This method supplies the POST request mapping to process the password change
     * @param user User object that is being updated
     * @param customUserDetails used to get detail about the logged in user
     * @param model used to supply attributes to the view
     * @param message RedirectAttributes used to add a flash message for successful password change
     * @return profile view template if error is present, otherwise redirect to /ticket/main
     */
    @RequestMapping(value = "/password-change", method = RequestMethod.POST)
    public String processUpdatePassword(@ModelAttribute User user,
                                        @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                        Model model, RedirectAttributes message) {

        // use BCrypt to encode the password
        BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder(11);

        // check to ensure the user entered all password fields
        if (user.getNewPassword().equals("") || user.getVerifyPassword().equals("") || user.getPassword().equals("")) {

            message.addFlashAttribute("message","All password fields are required");
            return "redirect:/profile";

        // check if the new password matches the verify field and the user entered the correct current password
        }else if ((!user.getNewPassword().equals(user.getVerifyPassword())) ||
                (!bCrypt.matches(user.getPassword(), customUserDetails.getPassword())) ) {

            message.addFlashAttribute("message", "Passwords do not match or invalid current password");
            return "redirect:/profile";

            // all checks passed, proceed with password change, update password in the database and set the success message
        }else {
            userDao.updatePasswordById(bCrypt.encode(user.getNewPassword()), customUserDetails.getId());
            message.addFlashAttribute("message", "Successfully changed password");

            return "redirect:/ticket/main";
        }
    }

    /**
     * This method supplies the POST request mapping to process the mobile phone and carrier update
     * @param user User object that is being updated
     * @param customUserDetails used to get detail about the logged in user
     * @param model used to supply attributes to the view
     * @param message RedirectAttributes used to add a flash message for successful password change
     * @return profile view template if error is present, otherwise redirect to /ticket/main
     */
    @RequestMapping(value = "/profile-update", method = RequestMethod.POST)
    public String processUpdateProfile(@Valid @ModelAttribute User user, Model model, RedirectAttributes message,
                                       @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        if (user.getPhone().isEmpty() || user.getPhone().length()!=10) {

            message.addFlashAttribute("message", "Phone number must be 10 digits and both carrier and phone are required");
            return "redirect:/profile";
        }

        // all required fields are present and pass validation, process the change and set the success message
        userDao.updatePhoneById(user.getPhone(), user.getCarrierId().getId(), customUserDetails.getId());
        message.addFlashAttribute("message", "Updated profile");

        return "redirect:/ticket/main";
    }

    /**
     * This method supplies the request mapping for the custom access denied error page
     * @return 403 view template
     */
    @RequestMapping("/403")
    public String accessDenied() {
        return "error/403";
    }


    /**
     * This method supplies the request mapping to display the sms messaging form
     * @param model used to supply attributes to the view
     * @return sms messaging form template view
     */
    @RequestMapping(value = "/sms", method = RequestMethod.GET)
    public String displayMessaging(Model model) {
        model.addAttribute("title", "IMS - Messaging");
        model.addAttribute("subtitle", "SMS Messaging");
        model.addAttribute("date", format.format(new Date()));
        model.addAttribute("message", new Message());
        model.addAttribute("groups", groupDao.findAll());
        model.addAttribute("users", userDao.findAllByOrderByLastName());
        return "sms";
    }

    /**
     * This method supplies the request mapping to process the sms messaging form and send the message
     * @param sms Message object used to build the sms message
     * @param message RedirectAttributes object used to set the flash message
     * @return String template view
     */
    @RequestMapping(value = "/sms", method = RequestMethod.POST)
    public String processMessaging(@ModelAttribute Message sms, RedirectAttributes message){
       // iterate over each of the selected groups and add each user to the selectedUsers set if user not already present
        for (AssignedGroup group : sms.getSelectedGroups()) {

            // add all users in the group to the set if not already present
            sms.getSelectedUsers().addAll(userDao.findAllByGroupId(group));
        }

        // iterate over the list of users and create the addresses to send the message to and add to the sendList set
        for (User user : sms.getSelectedUsers()) {
            // get the user's phone number
            String phone = user.getPhone();

            // check that carrier and phone number are not empty, then build the send address and add to the set
            if ((!(user.getCarrierId() == null)) && (!phone.equals(""))){

                // build the address and add to the set
                String address = phone + carrierDao.findCarrierById(user.getCarrierId().getId()).getCarrierDomain();
                sms.getSendList().add(address);
            }
        }

        // iterate over the set of users, add each as a recipient and send the email
        Mail email = new Mail();

        // copy the hash set to an array
        String[] toAddress = sms.getSendList().toArray(new String[0]);

        // build the email (sms message is being sent as an email) and send
        email.setToMultiple(toAddress);
        email.setContent(sms.getSmsMessage());

        emailService.sendSimpleMessage(email);

        // redirect and display message with number of users the message was sent to and conditionally display "user" or "users"
        if (sms.getSendList().size() == 1) {
            message.addFlashAttribute("success", "Message sent to " + sms.getSendList().size()  + " user");
        }else {
            message.addFlashAttribute("success", "Message sent to " + sms.getSendList().size() + " users");
        }

        return "redirect:/sms";
    }
}
