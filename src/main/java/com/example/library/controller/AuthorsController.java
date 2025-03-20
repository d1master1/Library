package com.example.library.controller;

import com.example.library.dto.AuthorDto;
import com.example.library.service.AuthorService;
import com.example.library.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthorsController {

    private final AuthorService authorService;
    private final UserService userService;

    public AuthorsController(AuthorService authorService, UserService userService) {
        this.authorService = authorService;
        this.userService = userService;
    }

    @ModelAttribute("author")
    private AuthorDto authorDto() {
        return new AuthorDto();
    }
    
    @GetMapping("/addAuthor")
    public String addAuthor(@AuthenticationPrincipal User user, Model model) {
        if (user != null){
            model.addAttribute("user", userService.findUser(user.getUsername()));
        }
        else {
            model.addAttribute("user", null);
        }
        model.addAttribute("authors", authorService.findAllAuthors());
        return "addAuthor";
    }

    @PostMapping("/saveAuthor")
    public String saveAuthor(@ModelAttribute("author") AuthorDto authorDto) {
        authorService.saveAuthor(authorDto);
        return "redirect:/profile";
    }

    @GetMapping("/authorsList")
    public String booksList(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", userService.findUser(user.getUsername()));
        model.addAttribute("authors", authorService.findAllAuthors());
        return "authorsList";
    }

}
