package com.example.webapp.controllers;

import com.example.webapp.models.*;
import com.example.webapp.repository.*;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
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
    public String admin(@PathVariable(value = "id") Long id, Model model) {
        Optional<User> user = userRepository.findById(id);
        model.addAttribute("user", user.get());
        return "mainadmin";
    }
    @PostMapping("/admin/{id}")
    public String changeData(@PathVariable(value = "id") Long id, @RequestParam String firstname, @RequestParam String lastname, @RequestParam String middlename, @RequestParam String login, @RequestParam String password, @RequestParam String newpassword, Model model) {
        Optional<User> user = userRepository.findById(id);
        model.addAttribute("user", user.get());
        if(password=="" || newpassword==""){
            User user1 = new User(id,firstname,lastname,middlename,login,user.get().getPassword(),user.get().getPost());
            userRepository.save(user1);
        }else{
            if(user.get().getPassword().equals(password)){
                User user1 = new User(id,firstname,lastname,middlename,login,newpassword,user.get().getPost());
                userRepository.save(user1);
            }
        }
        return "redirect:/admin/{id}";
    }

    @GetMapping("/admin/analystics/{id}")
    public String analistics(@PathVariable(value = "id") Long id, Model model) {
        Optional<User> user = userRepository.findById(id);
        Iterable<Date> dates = dateRepository.findAll();
        Iterable<User> users = userRepository.findAll();
        ArrayList<Integer> sum = new ArrayList<>();
        int i = 0;
        for (Date el : dates) {
            sum.add(i, salaryRepository.SumByDateId(el.getDateId()));
            i++;
        }
        Integer[][] usersum = new Integer[(int) users.spliterator().getExactSizeIfKnown()][((int) dates.spliterator().getExactSizeIfKnown())];
        int j = 0;
        for (User u : users) {
            i = 0;
            for (Date el : dates) {
                usersum[j][i] = salaryRepository.SumByDateIdAndUserId(el.getDateId(), u.getUserId());
                i++;
            }
            j++;
        }
        model.addAttribute("users", users);
        model.addAttribute("usersum", usersum);
        model.addAttribute("sum", sum);
        model.addAttribute("user", user.get());
        model.addAttribute("dates", dates);
        return "analystics";
    }

    @PostMapping("/admin/analystics/{id}")
    public String createReport(@PathVariable(value = "id") Long id, @RequestParam String start, @RequestParam String end, Model model) {
        Optional<User> user = userRepository.findById(id);
        model.addAttribute("user", user.get());
        createRep(start, end);
        return "redirect:/admin/analystics/{id}";
    }

    @GetMapping("/admin/posts/{id}")
    public String posts(@PathVariable(value = "id") Long id, Model model) {
        Iterable<Post> posts = postRepository.findAll();
        Optional<User> user = userRepository.findById(id);
        model.addAttribute("posts", posts);
        model.addAttribute("user", user.get());
        return "posts";
    }

    @GetMapping("/admin/salaries/{id}")
    public String salaries(@PathVariable(value = "id") Long id, Model model) {
        Iterable<Salary> salaries = salaryRepository.findAll();
        Optional<User> user = userRepository.findById(id);
        model.addAttribute("salaries", salaries);
        model.addAttribute("user", user.get());
        return "salaries";
    }

    @GetMapping("/admin/employees/{id}")
    public String employees(@PathVariable(value = "id") Long id, Model model) {
        Iterable<User> employees = userRepository.findAll();
        Optional<User> user = userRepository.findById(id);
        model.addAttribute("employees", employees);
        model.addAttribute("user", user.get());
        return "employees";
    }

    @GetMapping("/admin/timesheets/{id}")
    public String timesheets(@PathVariable(value = "id") Long id, Model model) {
        Iterable<TimeSheet> timesheets = timeSheetRepository.findAll();
        Optional<User> user = userRepository.findById(id);
        model.addAttribute("timesheets", timesheets);
        model.addAttribute("user", user.get());
        return "timesheets";
    }

    @GetMapping("/admin/dates/{id}")
    public String dates(@PathVariable(value = "id") Long id, Model model) {
        Iterable<Date> dates = dateRepository.findAll();
        Optional<User> user = userRepository.findById(id);
        model.addAttribute("dates", dates);
        model.addAttribute("user", user.get());
        return "dates";
    }

    @PostMapping("/admin/posts/{id}")
    public String addPost(@PathVariable(value = "id") Long id, @RequestParam Long idd, @RequestParam String post, @RequestParam int oklad, Model model) {
        Optional<User> user = userRepository.findById(id);
        model.addAttribute("user", user.get());
        Post post1 = new Post(idd, post, oklad);
        postRepository.save(post1);
        return "redirect:/admin/posts/{id}";
    }

    @PostMapping("/admin/dates/{id}")
    public String addDate(@PathVariable(value = "id") Long id, @RequestParam Long idd, @RequestParam String month, @RequestParam int year, @RequestParam int hours, Model model) {
        Optional<User> user = userRepository.findById(id);
        model.addAttribute("user", user.get());
        Date date1 = new Date(idd, month, year, hours);
        dateRepository.save(date1);
        return "redirect:/admin/dates/{id}";
    }

    @PostMapping("/admin/timesheets/{id}")
    public String addtimesheet(@PathVariable(value = "id") Long id, @RequestParam Long idd, @RequestParam String emp, @RequestParam String month, @RequestParam int year, @RequestParam int worktime, @RequestParam int holiday, @RequestParam int sickleave, @RequestParam int overtime, Model model) {
        Optional<User> user = userRepository.findById(id);
        model.addAttribute("user", user.get());
        Optional<Date> date1 = dateRepository.findByMonthAndYear(month, year);
        String[] FIO = emp.split(" ");
        Optional<User> user1 = userRepository.findByFIO(FIO[0], FIO[1], FIO[2]);
        if (user1.isPresent() && date1.isPresent()) {
            TimeSheet timesheet1 = new TimeSheet(idd, worktime, sickleave, holiday, overtime, new User(user1.get().getUserId()), new Date(date1.get().getDateId()));
            timeSheetRepository.save(timesheet1);
        }
        return "redirect:/admin/timesheets/{id}";
    }

    @PostMapping("/admin/employees/{id}")
    public String addEmployees(@PathVariable(value = "id") Long id, @RequestParam Long idd, @RequestParam String firstname, @RequestParam String lastname, @RequestParam String middlename, @RequestParam String post, @RequestParam String login, @RequestParam String password, Model model) {
        Optional<User> user = userRepository.findById(id);
        model.addAttribute("user", user.get());
        Optional<Post> post1 = postRepository.findByPost(post);
        if (post1.isPresent()) {
            User user1 = new User(idd, firstname, lastname, middlename, login, password, new Post(post1.get().getPostId()));
            userRepository.save(user1);
        }
        return "redirect:/admin/employees/{id}";
    }

    @PostMapping("/post/delete/{id}")
    public String deletepost(@PathVariable(value = "id") Long id, @RequestParam Long hidden, Model model) {
        Optional<User> user = userRepository.findById(id);
        model.addAttribute("user", user.get());
        Post post1 = postRepository.findById(hidden).orElseThrow();
        postRepository.delete(post1);
        return "redirect:/admin/posts/{id}";
    }

    @PostMapping("/date/delete/{id}")
    public String deletedate(@PathVariable(value = "id") Long id, @RequestParam Long hidden, Model model) {
        Optional<User> user = userRepository.findById(id);
        model.addAttribute("user", user.get());
        Date date1 = dateRepository.findById(hidden).orElseThrow();
        dateRepository.delete(date1);
        return "redirect:/admin/dates/{id}";
    }

    @PostMapping("/timesheet/delete/{id}")
    public String deletetimesheet(@PathVariable(value = "id") Long id, @RequestParam Long hidden, Model model) {
        Optional<User> user = userRepository.findById(id);
        model.addAttribute("user", user.get());
        TimeSheet timesheet1 = timeSheetRepository.findById(hidden).orElseThrow();
        timeSheetRepository.delete(timesheet1);
        return "redirect:/admin/timesheets/{id}";
    }

    @PostMapping("/user/delete/{id}")
    public String deleteuser(@PathVariable(value = "id") Long id, @RequestParam Long hidden, Model model) {
        Optional<User> user = userRepository.findById(id);
        model.addAttribute("user", user.get());
        User user1 = userRepository.findById(hidden).orElseThrow();
        userRepository.delete(user1);
        return "redirect:/admin/employees/{id}";
    }

    public void createRep(String start, String end) {
        XWPFDocument document = new XWPFDocument();
        try {
            FileOutputStream out = new FileOutputStream(new File(start + "-" + end + ".docx"));
            document.createParagraph().createRun().setText("   Отчет расчета заработной платы за период: " + start + " по " + end);
            document.createParagraph();
            XWPFTable table = document.createTable();
            XWPFTableRow tableRowOne = table.getRow(0);
            document.createParagraph();
            tableRowOne.getCell(0).setText("   Фамилия   ");
            tableRowOne.addNewTableCell().setText("   Имя   ");
            tableRowOne.addNewTableCell().setText("    Отчество    ");
            tableRowOne.addNewTableCell().setText("   Должность   ");
            tableRowOne.addNewTableCell().setText("   Дата   ");
            tableRowOne.addNewTableCell().setText("   ЗП   ");
            int k = 0;
            Optional<Date> startid = dateRepository.findByMonthAndYear(start.split(" ")[0], Integer.parseInt(start.split(" ")[1]));
            Optional<Date> endid = dateRepository.findByMonthAndYear(end.split(" ")[0], Integer.parseInt(end.split(" ")[1]));
            Iterable<Salary> salary = salaryRepository.findByDate(startid.get().getDateId(), endid.get().getDateId());
            for (Salary el : salary) {
                XWPFTableRow tableRowTwo = table.createRow();
                tableRowTwo.getCell(0).setText(el.getTimeSheet().getUser().getFirstName());
                tableRowTwo.getCell(1).setText(el.getTimeSheet().getUser().getLastName());
                tableRowTwo.getCell(2).setText(el.getTimeSheet().getUser().getMiddleName());
                tableRowTwo.getCell(3).setText(el.getTimeSheet().getUser().getPost().getPost());
                tableRowTwo.getCell(4).setText(el.getTimeSheet().getDate().getMonth() + " " + el.getTimeSheet().getDate().getYear());
                tableRowTwo.getCell(5).setText(String.valueOf(el.getSalary()));
            }

            document.write(out);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
