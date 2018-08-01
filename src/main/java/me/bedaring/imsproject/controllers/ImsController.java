package me.bedaring.imsproject.controllers;

import me.bedaring.imsproject.EmailService;
import me.bedaring.imsproject.Mail;
import me.bedaring.imsproject.models.CustomUserDetails;
import me.bedaring.imsproject.models.User;
import me.bedaring.imsproject.models.data.CarrierDao;
import me.bedaring.imsproject.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class ImsController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private CarrierDao carrierDao;

    SimpleDateFormat format = new SimpleDateFormat("EEEE MMMM d, y - hh:mm:ss aa");

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model) {
        model.addAttribute("title", "IMS Login");
        return "login";
    }

    @RequestMapping(value = "/email")
    public String email(RedirectAttributes message, Model model) {

        Mail mail = new Mail();
        mail.setFrom("test@bedaring.me");
        mail.setTo("deangelomp@gmail.com");
        mail.setSubject("test email");
        mail.setContent("this is a test email to see if this works");

        emailService.sendSimpleMessage(mail);

        message.addFlashAttribute("message", "Email sent");

        return "redirect:/admin/menu";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String displayProfileUpdate(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {
        model.addAttribute("title", "IMS - Update Profile");
        model.addAttribute("subtitle", "Update Profile");
        model.addAttribute("date", format.format(new Date()));
        //model.addAttribute("user", new User());
        model.addAttribute("user", userDao.findById(customUserDetails.getId()));
        model.addAttribute("carriers", carrierDao.findAll());
        return "/profile";
    }

    @RequestMapping(value = "/password-change", method = RequestMethod.POST)
    public String processUpdatePassword(@Valid @ModelAttribute User user,
                                        @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                        Model model, RedirectAttributes message) {

        BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder(11);

        // check to ensure the user entered all password fields
        if (user.getNewPassword().equals("") || user.getVerifyPassword().equals("") || user.getPassword().equals("")) {
            model.addAttribute("title", "IMS - Update Profile");
            model.addAttribute("subtitle", "Update Profile");
            model.addAttribute("date", format.format(new Date()));
            model.addAttribute("message", "All password fields are required");
            //model.addAttribute("user", new User());
            System.out.println("something was empty");

            return "/profile";
        // check if the new password matches the verify field
        }else if (!user.getNewPassword().equals(user.getVerifyPassword())) {
            model.addAttribute("title", "IMS - Update Profile");
            model.addAttribute("subtitle", "Update Profile");
            model.addAttribute("date", format.format(new Date()));
            model.addAttribute("message", "Passwords do not match or invalid current password");
            //model.addAttribute("user", new User());
            System.out.println("passwords don't match");
            return "/profile";
        // check to ensure the user entered the correct current password
        }else if(!bCrypt.matches(user.getPassword(), customUserDetails.getPassword())){
            model.addAttribute("title", "IMS - Update Profile");
            model.addAttribute("subtitle", "Update Profile");
            model.addAttribute("date", format.format(new Date()));
            model.addAttribute("message", "Passwords do not match or invalid current password");

            return "/profile";
        // all verifications passed, process the password change
        }else {
            userDao.updatePasswordById(bCrypt.encode(user.getNewPassword()), customUserDetails.getId());
            message.addFlashAttribute("message", "Successfully changed password");

            return "redirect:/ticket/main";
        }

    }

    @RequestMapping(value = "/profile-update", method = RequestMethod.POST)
    public String processUpdateProfile(@Valid @ModelAttribute User user, Errors errors,
                                       @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                       Model model, RedirectAttributes message) {
        if (errors.hasErrors()) {
            model.addAttribute("title", "IMS - Update Profile");
            model.addAttribute("subtitle", "Update Profile");
            model.addAttribute("date", format.format(new Date()));
            model.addAttribute("carriers", carrierDao.findAll());
            //model.addAttribute("user", new User());
           // model.addAttribute("user", userDao.findById(customUserDetails.getId()));
            return "/profile";
        }
        userDao.updatePhoneById(user.getPhone(), user.getCarrierId().getId(), customUserDetails.getId());
        message.addFlashAttribute("message", "Updated profile");

        return "redirect:/ticket/main";
    }


}
