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
import java.util.ArrayList;
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
    @PostMapping("/accounter/{id}")
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
        return "redirect:/accounter/{id}";
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

    @GetMapping("/accounter/analystics/{id}")
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
        return "accounteranalystics";
    }

    @PostMapping("/accounter/analystics/{id}")
    public String createReport(@PathVariable(value = "id") Long id, @RequestParam String start, @RequestParam String end, Model model) {
        Optional<User> user = userRepository.findById(id);
        model.addAttribute("user", user.get());
        createRep(start, end);
        return "redirect:/accounter/analystics/{id}";
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
