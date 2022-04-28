package com.example.webapp.controllers;

import com.example.webapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.webapp.models.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;


@Controller
public class MainController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String home(Model model) {
        return "home";
    }
    @PostMapping("/")
    public String login(@RequestParam String login, @RequestParam String password, Model model){
        Optional<User> user = userRepository.findUserByLoginAndPassword(login,password);
        if(user.isPresent()){
            Long id=user.get().getUserId();
            switch(user.get().getPost().getPost()){
                case "Директор": return "redirect:/admin/"+id.toString();
                case "Бухгалтер": return "redirect:/accounter/"+id.toString();
                default: return "redirect:/user/"+id.toString();
            }
        }else{
            return "redirect:/";
        }
    }
}
