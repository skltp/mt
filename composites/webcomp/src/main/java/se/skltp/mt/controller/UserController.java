package se.skltp.mt.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import se.skltp.mt.controller.vo.UserForm;
import se.skltp.mt.core.entity.User;
import se.skltp.mt.core.service.UserService;
import se.skltp.mt.exception.CaseboxException;

@Controller
@RequestMapping("/admin/user")
public class UserController {
    
    @Autowired
    private UserService userService;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value="/create", method=RequestMethod.POST)
    public ModelAndView create(@ModelAttribute("form") @Valid UserForm form, 
            BindingResult result) {
        ModelAndView mav = new ModelAndView("admin/user");
        
        try {
            userService.createUser(form.getUsername(), form.getPassword(), form.getFirstname(), form.getLastname());
        } catch (CaseboxException e) {
            log.error(e.getMessage());
            mav.getModel().put("error", e.getMessageCode());
        }
        List<User> users = userService.findAllUsers();
        mav.addObject("users", users);
        return mav;
    }
    
    @RequestMapping(value="/delete/{username}", method=RequestMethod.GET)
    public ModelAndView delete(@PathVariable String username) {
        ModelAndView mav = new ModelAndView("admin/user");
        log.debug("Delete user {}", new Object[]{username});
        try {
            userService.deleteUser(username);
        } catch (CaseboxException e) {
            log.error(e.getMessage());
            mav.getModel().put("error", e.getMessageCode());
        }
        List<User> users = userService.findAllUsers();
        mav.addObject("users", users);
        mav.addObject("form", new UserForm());
        return mav;
    }
    
    @RequestMapping(method=RequestMethod.GET)
    public ModelAndView userOverview() {
        ModelAndView mav = new ModelAndView("admin/user");
        List<User> users = userService.findAllUsers();
        mav.addObject("users", users);
        mav.addObject("form", new UserForm());
        return mav;
    }
}
