package com.epam.javacourse.hotelapp.controller;

import com.epam.javacourse.hotelapp.dto.UserDto;
import com.epam.javacourse.hotelapp.exception.UserAlreadyExistsException;
import com.epam.javacourse.hotelapp.service.interfaces.IUserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static com.epam.javacourse.hotelapp.utils.Constants.PAGE_REGISTRATION;
import static com.epam.javacourse.hotelapp.utils.Constants.PARAM_USER;

@Controller
@RequestMapping("registration")
public class RegistrationController {

    private static final Logger logger = LogManager.getLogger(RegistrationController.class);

    @Autowired
    IUserService userService;


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping(value = "/new")
    public String getRegisterPage(HttpSession session, Model model) {
        return PAGE_REGISTRATION;
    }

    @PostMapping(value = "/new")
    public String registerPost(HttpServletRequest request, HttpSession session, @Valid UserDto userDto, Model model) throws UserAlreadyExistsException {

        String password = userDto.getPassword();
        String passHash = passwordEncoder.encode(userDto.getPassword());
        userDto.setPassword(passHash);

        authWithAuthManager(request, userDto.getEmail(), password);
        session.setAttribute(PARAM_USER, userDto);
        logger.info("Create user with id = {}", userDto.getId());
        return "redirect:/client/info";
    }

    public void authWithAuthManager(HttpServletRequest request, String username, String password) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
        authToken.setDetails(new WebAuthenticationDetails(request));

        Authentication authentication = authenticationManager.authenticate(authToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

//    @GetMapping("/new")
//    public String showRegistrationForm(HttpServletRequest request, Model model) {
//        UserDto userDto = new UserDto();
//        model.addAttribute("user", userDto);
//        return PAGE_REGISTRATION;
//    }

//    /**
//     * The controller will redirect to the registration form if there are any errors set at validation time.
//     * @param userDto
//     * @param request
//     * @param errors
//     * @return ModelAndView object which is the convenient class for sending model data (user) tied to the view.
//     */
//    @PostMapping("/new")
//    public ModelAndView registerUser(@ModelAttribute("user") @Valid UserDto userDto,
//            HttpServletRequest request, Errors errors) {
//        ModelAndView mav = null;
//        try {
//            userService.createUser(userDto);
//        } catch (UserAlreadyExistsException uaeEx) {
//            mav.addObject("message", "An account for that username/email already exists.");
//            return mav;
//        }
//        return new ModelAndView("successRegister", "user", userDto);
//
//    }

}
