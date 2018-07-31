package me.bedaring.imsproject.controllers;

import me.bedaring.imsproject.EmailService;
import me.bedaring.imsproject.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ImsController {

    @Autowired
    private EmailService emailService;

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
}
