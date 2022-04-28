package com.example.webapp.controllers;

import com.example.webapp.models.Date;
import com.example.webapp.models.Salary;
import com.example.webapp.models.TimeSheet;
import com.example.webapp.models.User;
import com.example.webapp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private SalaryRepository salaryRepository;
    @Autowired
    private TimeSheetRepository timeSheetRepository;
    @Autowired
    private DateRepository dateRepository;

    @GetMapping("/user/{id}")
    public String accounter(@PathVariable(value = "id") Long id, Model model) {
        Optional<User> user = userRepository.findById(id);
        model.addAttribute("user", user.get());
        return "usermain";
    }

    @PostMapping("/user/{id}")
    public String changeData(@PathVariable(value = "id") Long id, @RequestParam String firstname, @RequestParam String lastname, @RequestParam String middlename, @RequestParam String login, @RequestParam String password, @RequestParam String newpassword, Model model) {
        Optional<User> user = userRepository.findById(id);
        model.addAttribute("user", user.get());
        if (password == "" || newpassword == "") {
            User user1 = new User(id, firstname, lastname, middlename, login, user.get().getPassword(), user.get().getPost());
            userRepository.save(user1);
        } else {
            if (user.get().getPassword().equals(password)) {
                User user1 = new User(id, firstname, lastname, middlename, login, newpassword, user.get().getPost());
                userRepository.save(user1);
            }
        }
        return "redirect:/user/{id}";
    }
    @GetMapping("/user/userinformation/{id}")
    public String timesheets(@PathVariable(value="id") Long id,Model model) {
        Iterable<TimeSheet> timesheets = timeSheetRepository.findByUserId(id);
        Iterable<Salary> salaries = salaryRepository.findByUserId(id);
        Optional<User> user=userRepository.findById(id);
        Iterable<Date> dates = dateRepository.findAll();
        int i=0;
        Integer[][] usersum = new Integer[1][((int) dates.spliterator().getExactSizeIfKnown())];
            for (Date el : dates) {
                usersum[0][i] = salaryRepository.SumByDateIdAndUserId(el.getDateId(), user.get().getUserId());
                i++;
            }
        model.addAttribute("usersum", usersum);
        model.addAttribute("dates", dates);
        model.addAttribute("salaries", salaries);
        model.addAttribute("timesheets", timesheets);
        model.addAttribute("user", user.get());
        return "userinformation";
    }
}