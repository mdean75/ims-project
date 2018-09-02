package me.bedaring.imsproject.controllers;

import me.bedaring.imsproject.EmailService;
import me.bedaring.imsproject.Mail;
import me.bedaring.imsproject.models.User;
import me.bedaring.imsproject.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Controller
@RequestMapping(value = "profile")
public class ProfileController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private EmailService emailService;

    // date format to use for display timestamp
    private SimpleDateFormat format = new SimpleDateFormat("EEEE MMMM d, y - hh:mm:ss aa");

    @RequestMapping(value = "activate", method = RequestMethod.GET)
    public String displayActivation(@RequestParam String token, Model model){
        model.addAttribute("title", "IMS - Complete new account setup");
        model.addAttribute("subtitle", "Account setup");
        model.addAttribute("date", format.format(new Date()));
        model.addAttribute("user", new User());
        model.addAttribute("token", token);

        boolean isValidToken = false;
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Optional<User> tokenUser = userDao.findUserByToken(token);

        // check if user found for the given token, if true get the expiration timestamp
        if (tokenUser.isPresent()) {
            // expiration timestamp from database
            Timestamp userTimestamp = new Timestamp(tokenUser.get().getTokenExpiration().getTime());
            model.addAttribute("id", tokenUser.get().getId());

            // check if token is not expired and no more than 3 attempts have been made to set the password with this token
            if ( (timestamp.getTime() < userTimestamp.getTime()) && (tokenUser.get().getTokenAttempts() < 3) ) {
                // all validation passed, set the flag to true
                isValidToken = true;
            }
        }

        model.addAttribute("isValidToken", isValidToken);
        return "profile/activate";
    }

    @RequestMapping(value = "activate", method = RequestMethod.POST)
    public String activateUser(@ModelAttribute User user, @RequestParam String token, RedirectAttributes message) {
        // todo check if username and email match the user of the given token, if true verify passwords match and are valid
        // on failed attempt increment the token attempt counter in the database, on success return counter to 0
        // and save the new password and set the timestamp and token to null
        String invalidEntry = "Either the username or email did not match our records, the new password is less than 8 " +
                "characters, or the passwords did not match";

        Optional<User> checkUser = userDao.findUserByToken(token);
        if (user.getUsername().equals(checkUser.get().getUsername()) && user.getEmail().equals(checkUser.get().getEmail())) {
            if (user.getNewPassword().equals(user.getVerifyPassword()) && user.getNewPassword().length() >= 8) {
                // all validation passed, set the new password and null the token, token expiration and token attempts fields
                checkUser.get().setPassword(bCryptPasswordEncoder.encode(user.getNewPassword()));
                checkUser.get().setToken(null);
                checkUser.get().setTokenExpiration(null);
                checkUser.get().setTokenAttempts(0);
                userDao.save(checkUser.get());
                message.addFlashAttribute("message", "User saved successfully");
                return "redirect:/";

            } else {
                // passwords did not match, increment the token attempts counter
                checkUser.get().setTokenAttempts(checkUser.get().getTokenAttempts() + 1);
                userDao.save(checkUser.get());
                message.addFlashAttribute("message", invalidEntry);
                return "redirect:/profile/activate?token="+token;
            }
        } else {
            // username and/or email did not match details for the given token
            checkUser.get().setTokenAttempts(checkUser.get().getTokenAttempts() + 1);
            userDao.save(checkUser.get());
            message.addFlashAttribute("message", invalidEntry);
            return "redirect:/profile/activate?token="+token;
        }

    }

    @RequestMapping(value = "/reset-password", method = RequestMethod.POST)
    public String resetPassword(@RequestParam String username, @RequestParam String email, RedirectAttributes message,
                                HttpServletRequest request) {
        // check to ensure username and email are not empty then attempt to get user record by the entered username
        if (username.length() > 0 && email.length() > 0) {
            Optional<User> user = userDao.findByUsername(username);

            // check if result was found
            if (user.isPresent()) {
                // check if the user entered email matches the database
                if (user.get().getEmail().equals(email)) {
                    // validation passed, create a timestamp and build the new token
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    String token = User.createRandomPassword(16) + timestamp.getTime();

                    // set the expiration timestamp
                    timestamp.setTime(timestamp.getTime() + ( (24 * 60 * 60 ) * 1000) );


                    // set a new hashed random password
                    user.get().setPassword(bCryptPasswordEncoder.encode(User.createRandomPassword(16)));

                    //set the token attempts to 0, expiration to the current timestamp + 24 hours, and the created token
                    user.get().setTokenAttempts(0);
                    user.get().setTokenExpiration(timestamp);
                    user.get().setToken(token);

                    try {
                        // save the updates to the user record
                        userDao.save(user.get());

                        // create the email
                        Mail mail = new Mail();
                        mail.setFrom("bedaring.me@gmail.com");
                        mail.setTo(user.get().getEmail());
                        mail.setSubject("IMS password reset");
                        //Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                        String msg = "The password for the user associated with this email address in the IMS system " +
                                "has been reset.  Click on this link or copy and paste it into your browser to choose " +
                                "your new password.  Remember that passwords must be at least 8 characters in length.  " +
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
                }
            }
        }

        message.addFlashAttribute("message", "If a matching user is found, the password will be reset and an " +
                "email with instructions will be sent to the account holder");
        return "redirect:/";
    }

}
