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
public class AdminController {

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
    @GetMapping("/admin/{id}")
    public String admin(@PathVariable(value="id") Long id,Model model) {
        Optional<User> user=userRepository.findById(id);
        model.addAttribute("user", user.get());
        return "mainadmin";
    }
    @GetMapping("/admin/analystics/{id}")
    public String analistics(@PathVariable(value="id") Long id,Model model) {
        Optional<User> user=userRepository.findById(id);
        Iterable<Date> dates=dateRepository.findAll();
        model.addAttribute("user", user.get());
        model.addAttribute("dates", dates);
        return "analystics";
    }
    @GetMapping("/admin/posts/{id}")
    public String posts(@PathVariable(value="id") Long id,Model model) {
        Iterable<Post> posts = postRepository.findAll();
        Optional<User> user=userRepository.findById(id);
        model.addAttribute("posts", posts);
        model.addAttribute("user", user.get());
        return "posts";
    }
    @GetMapping("/admin/salaries/{id}")
    public String salaries(@PathVariable(value="id") Long id,Model model) {
        Iterable<Salary> salaries = salaryRepository.findAll();
        Optional<User> user=userRepository.findById(id);
        model.addAttribute("salaries", salaries);
        model.addAttribute("user", user.get());
        return "salaries";
    }
    @GetMapping("/admin/employees/{id}")
    public String employees(@PathVariable(value="id") Long id,Model model) {
        Iterable<User> employees = userRepository.findAll();
        Optional<User> user = userRepository.findById(id);
        model.addAttribute("employees", employees);
        model.addAttribute("user", user.get());
        return "employees";
    }
    @GetMapping("/admin/timesheets/{id}")
    public String timesheets(@PathVariable(value="id") Long id,Model model) {
        Iterable<TimeSheet> timesheets = timeSheetRepository.findAll();
        Optional<User> user=userRepository.findById(id);
        model.addAttribute("timesheets", timesheets);
        model.addAttribute("user", user.get());
        return "timesheets";
    }
    @GetMapping("/admin/dates/{id}")
    public String dates(@PathVariable(value="id") Long id,Model model) {
        Iterable<Date> dates = dateRepository.findAll();
        Optional<User> user=userRepository.findById(id);
        model.addAttribute("dates", dates);
        model.addAttribute("user", user.get());
        return "dates";
    }
    @PostMapping("/admin/posts/{id}")
    public String addPost(@PathVariable(value="id") Long id, @RequestParam Long idd, @RequestParam String post, @RequestParam int oklad, Model model) {
        Optional<User> user=userRepository.findById(id);
        model.addAttribute("user", user.get());
        Post post1=new Post(idd,post,oklad);
        postRepository.save(post1);
        return "redirect:/admin/posts/{id}";
    }
    @PostMapping("/admin/dates/{id}")
    public String addDate(@PathVariable(value="id") Long id, @RequestParam Long idd, @RequestParam String month, @RequestParam int year, @RequestParam int hours, Model model) {
        Optional<User> user=userRepository.findById(id);
        model.addAttribute("user", user.get());
        Date date1=new Date(idd,month,year,hours);
        dateRepository.save(date1);
        return "redirect:/admin/dates/{id}";
    }
    @PostMapping("/admin/timesheets/{id}")
    public String addtimesheet(@PathVariable(value="id") Long id,@RequestParam Long idd,@RequestParam String emp,@RequestParam String month,@RequestParam int year,@RequestParam int worktime,@RequestParam int holiday,@RequestParam int sickleave,@RequestParam int overtime, Model model) {
        Optional<User> user = userRepository.findById(id);
        model.addAttribute("user", user.get());
        Optional<Date> date1=dateRepository.findByMonthAndYear(month,year);
        String[] FIO=emp.split(" ");
        Optional<User> user1=userRepository.findByFIO(FIO[0],FIO[1],FIO[2]);
        if(user1.isPresent() && date1.isPresent()) {
            TimeSheet timesheet1 = new TimeSheet(idd, worktime, sickleave, holiday, overtime, new User(user1.get().getUserId()), new Date(date1.get().getDateId()));
            timeSheetRepository.save(timesheet1);
        }
        return "redirect:/admin/timesheets/{id}";
    }
    @PostMapping("/admin/employees/{id}")
    public String addEmployees(@PathVariable(value="id") Long id,@RequestParam Long idd,@RequestParam String firstname,@RequestParam String lastname,@RequestParam String middlename,@RequestParam String post,@RequestParam String login,@RequestParam String password, Model model) {
        Optional<User> user = userRepository.findById(id);
        model.addAttribute("user", user.get());
        Optional<Post> post1=postRepository.findByPost(post);
        if(post1.isPresent()) {
            User user1 = new User(idd, firstname, lastname, middlename, login, password, new Post(post1.get().getPostId()));
            userRepository.save(user1);
        }
        return "redirect:/admin/employees/{id}";
    }
    @PostMapping("/post/delete/{id}")
    public String deletepost(@PathVariable(value="id") Long id,@RequestParam Long hidden,Model model){
        Optional<User> user=userRepository.findById(id);
        model.addAttribute("user", user.get());
        Post post1=postRepository.findById(hidden).orElseThrow();
        postRepository.delete(post1);
        return "redirect:/admin/posts/{id}";
    }
    @PostMapping("/date/delete/{id}")
    public String deletedate(@PathVariable(value="id") Long id,@RequestParam Long hidden,Model model){
        Optional<User> user=userRepository.findById(id);
        model.addAttribute("user", user.get());
        Date date1=dateRepository.findById(hidden).orElseThrow();
        dateRepository.delete(date1);
        return "redirect:/admin/dates/{id}";
    }
    @PostMapping("/timesheet/delete/{id}")
    public String deletetimesheet(@PathVariable(value="id") Long id,@RequestParam Long hidden,Model model){
        Optional<User> user=userRepository.findById(id);
        model.addAttribute("user", user.get());
        TimeSheet timesheet1=timeSheetRepository.findById(hidden).orElseThrow();
        timeSheetRepository.delete(timesheet1);
        return "redirect:/admin/timesheets/{id}";
    }
    @PostMapping("/user/delete/{id}")
    public String deleteuser(@PathVariable(value="id") Long id,@RequestParam Long hidden,Model model){
        Optional<User> user=userRepository.findById(id);
        model.addAttribute("user", user.get());
        User user1=userRepository.findById(hidden).orElseThrow();
        userRepository.delete(user1);
        return "redirect:/admin/employees/{id}";
    }
}
