package com.epam.javacourse.hotelapp.controller;

import com.epam.javacourse.hotelapp.dto.UserDto;
import com.epam.javacourse.hotelapp.exception.AppException;
import com.epam.javacourse.hotelapp.model.User;
import com.epam.javacourse.hotelapp.service.impl.UserServiceImpl;
import com.epam.javacourse.hotelapp.utils.mappers.UserMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static com.epam.javacourse.hotelapp.utils.Constants.*;
import static java.util.Objects.nonNull;
//import org.apache.commons.lang3.StringUtils;

@Controller
public class LoginController {

    private static final Logger logger = LogManager.getLogger(LoginController.class);

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Controller
    public class LoginPageController {

        @GetMapping("/login")
        public String login(){
            return PAGE_LOGIN;
        }
    }

//    @Resource(name = "customerAccountService")
//    private CustomerAccountService customerAccountService;
//
//    @GetMapping
//    public String login(@RequestParam(value = "error", defaultValue = "false") boolean loginError,
//                        @RequestParam(value = "invalid-session", defaultValue = "false") boolean invalidSession,
//                        final Model model, HttpSession session){
//
//        String userName = getUserName(session);
//
//        if(loginError){
//            if(StringUtils.isNotEmpty(userName) && customerAccountService.loginDisabled(userName)){
//                model.addAttribute("accountLocked", Boolean.TRUE);
//                model.addAttribute("forgotPassword", new ResetPasswordData());
//                return "account/login";
//            }
//        }
//        if(invalidSession){
//            model.addAttribute("invalidSession", "You already have an active session. We do not allow multiple active sessions");
//        }
//        model.addAttribute("forgotPassword", new ResetPasswordData());
//        model.addAttribute("accountLocked", Boolean.FALSE);
//        return "account/login";
//    }
//
//
//    final String getUserName(HttpSession session){
//        final String username = (String) session.getAttribute(LAST_USERNAME_KEY);
//        if(StringUtils.isNotEmpty(username)){
//            session.removeAttribute(LAST_USERNAME_KEY); // we don't need it and removing it.
//        }
//        return username;
//    }


//    @GetMapping("/login")
//    public String getLogin() {
//        return PAGE_LOGIN;
//    }

//    @PostMapping("/login")
//    public RedirectView postLogin(HttpServletRequest request, Model model) throws AppException {
//        final String email = request.getParameter(PARAM_EMAIL);
//        final String password = request.getParameter(PARAM_PASSWORD);
//
//        final HttpSession session = request.getSession();
//
//        if (nonNull(session) &&
//                nonNull(session.getAttribute("email")) &&
//                nonNull(session.getAttribute("password"))) {
//
//            return new RedirectView(PAGE_HOME);
//        }
//
//        User user = userService.getUserByEmail(email);
//
//        UserDto userDto = new UserDto();
//        String inputPass = null;
//        if (user != null) {
//
//            try {
//                inputPass = passwordEncoder.encode(password);
//            } catch (Exception e) {
//                logger.error("Cannot encode input password ", e);
//            }
//
//            UserMapper userMapper = new UserMapper();
//            userDto = userMapper.mapToDto(user);
//        }
//        if (inputPass.equals(user.getPassword())) {
//            request.getSession().setAttribute("authorisedUser", true);
//            request.getSession().setAttribute("user", userDto);
//            request.getSession().setAttribute("email", userDto.getEmail());
//            request.getSession().setAttribute("password", userDto.getPassword());
//            request.getSession().setAttribute("role", userDto.getRole());
//            return new RedirectView(PAGE_HOME);
//        } else {
//            logger.info("User entered wrong email or password");
//            model.addAttribute("wrongInput", true);
//            return new RedirectView(PAGE_LOGIN);
//        }
//    }
}
