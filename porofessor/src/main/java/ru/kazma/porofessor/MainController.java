package ru.kazma.porofessor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/")
public class MainController {
    @GetMapping
    public String hello(){
        return "main";
    }
    @PostMapping("check")
    public String check(@RequestParam String summonnerName, Model model){
        RestTemplate restTemplate = new RestTemplate();
        model.addAttribute("summonnerName", summonnerName);
        return "info";
    }
}
