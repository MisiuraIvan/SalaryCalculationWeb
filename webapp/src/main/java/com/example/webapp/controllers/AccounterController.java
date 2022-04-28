package com.example.webapp.controllers;

import com.example.webapp.models.*;
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
public class AccounterController {
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
    @GetMapping("/accounter/{id}")
    public String accounter(@PathVariable(value="id") Long id, Model model) {
        Optional<User> user=userRepository.findById(id);
        model.addAttribute("user", user.get());
        return "accountermain";
    }
    @GetMapping("/accounter/salaries/{id}")
    public String salaries(@PathVariable(value="id") Long id,Model model) {
        Iterable<Salary> salaries = salaryRepository.findAll();
        Optional<User> user=userRepository.findById(id);
        model.addAttribute("salaries", salaries);
        model.addAttribute("user", user.get());
        return "accountersalaries";
    }

    @GetMapping("/accounter/employees/{id}")
    public String employees(@PathVariable(value="id") Long id,Model model) {
        Iterable<User> employees = userRepository.findAll();
        Optional<User> user = userRepository.findById(id);
        model.addAttribute("employees", employees);
        model.addAttribute("user", user.get());
        return "accounteremployees";
    }
    @GetMapping("/accounter/timesheets/{id}")
    public String timesheets(@PathVariable(value="id") Long id,Model model) {
        Iterable<TimeSheet> timesheets = timeSheetRepository.findAll();
        Optional<User> user=userRepository.findById(id);
        model.addAttribute("timesheets", timesheets);
        model.addAttribute("user", user.get());
        return "accountertimesheets";
    }
    @GetMapping("/accounter/salaryaccount/{id}")
    public String salaryaccount(@PathVariable(value="id") Long id,Model model) {
        Iterable<TimeSheet> timesheets = timeSheetRepository.findAll();
        Iterable<Date> dates = dateRepository.findAll();
        Iterable<User> users=userRepository.findAll();
        Optional<User> user=userRepository.findById(id);
        model.addAttribute("user", user.get());
        model.addAttribute("dates", dates);
        model.addAttribute("timesheets", timesheets);
        model.addAttribute("users", users);
        return "salaryaccount";
    }
    @PostMapping("/accounter/salaryaccount/{id}")
    public String addSalary(@PathVariable(value="id") Long id,@RequestParam double netsalary,@RequestParam double salary,@RequestParam double pension,@RequestParam double pensioninsurance,@RequestParam double socialinsurance,@RequestParam double tax,@RequestParam int award,@RequestParam Long timesheetid, Model model) {
        Optional<User> user=userRepository.findById(id);
        model.addAttribute("user", user.get());
        double result=Math.ceil((pensioninsurance+socialinsurance)*Math.pow(10,2))/Math.pow(10,2);
        Salary salary1=new Salary(timesheetid,award,salary,netsalary,pension,tax,result,new TimeSheet(timesheetid));
        salaryRepository.save(salary1);
        return "redirect:/accounter/salaryaccount/{id}";
    }

}
