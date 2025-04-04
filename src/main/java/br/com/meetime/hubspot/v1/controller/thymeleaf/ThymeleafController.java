package br.com.meetime.hubspot.v1.controller.thymeleaf;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

@Controller
@RequestMapping()
public class ThymeleafController {

    @GetMapping("/")
    public String showCode(@RequestParam(name = "code", required = false) String code, Model model) {
        model.addAttribute("code", code);
        return "codePage";
    }
}

