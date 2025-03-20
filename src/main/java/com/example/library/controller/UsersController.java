package com.example.library.controller;

import com.example.library.dto.UserDto;
import com.example.library.impl.BookServiceImpl;
import com.example.library.service.BookService;
import com.example.library.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UsersController {

    private final UserService userService;
    private final BookService bookService;


    public UsersController(UserService userService, BookServiceImpl bookService) {
        this.userService = userService;
        this.bookService = bookService;
    }

    @ModelAttribute("user")
    public UserDto user() {
        return new UserDto();
    }

    @GetMapping("/registration")
    String registration(@ModelAttribute("user") UserDto userDto) {
        return "registration";
    }

    @PostMapping("/save")
    String save(@Valid @ModelAttribute("user") UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "registration";
        }

        if (userService.isUsernameAvailable(userDto.getUsername())) {
            System.out.println("Пользователь есть!");
            bindingResult.rejectValue("username","error.username", "Имя пользователя уже занято!");
            return "registration";
        }

        userService.save(userDto);
        return "redirect:/login";
    }
    
    @GetMapping("/")
    String index(@AuthenticationPrincipal User user, Model model) {
        if (user != null) {
            model.addAttribute("user", userService.findUser(user.getUsername()));
        } else {
            model.addAttribute("user", null);
        }
        return "index";
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    String profile(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", userService.findUser(user.getUsername()));
        model.addAttribute("books", bookService.findByUser(userService.findUser(user.getUsername())));
        model.addAttribute("title", "Электронная библиотека - профиль пользователя");
        return "profile";
    }
    
    @GetMapping("/usersList")
    String list(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", userService.findUser(user.getUsername()));
        model.addAttribute("users", userService.findAllUsers());
        return "usersList";
    }
}
