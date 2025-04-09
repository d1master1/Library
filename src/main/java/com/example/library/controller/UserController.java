package com.example.library.controller;

import com.example.library.model.Book;
import com.example.library.model.User;
import com.example.library.repo.BookRepo;
import com.example.library.service.BookService;
import com.example.library.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("users/user/{userId:\\d+}")
public class UserController {
    
    private final UserService userService;
    private final BookService bookService;
    private final BookRepo bookRepo;

    public UserController(UserService userService, BookService bookService, BookRepo bookRepo) {
        this.userService = userService;
        this.bookService = bookService;
        this.bookRepo = bookRepo;
    }
    
    @ModelAttribute("account")
    public User getUser(@PathVariable("userId") long userId) {
        return this.userService.findById(userId);
    }
    
    @GetMapping
    public String getUser(@AuthenticationPrincipal UserDetails user, Model model, @PathVariable long userId) {
        model.addAttribute("user", userService.findUser(user.getUsername()));
        model.addAttribute("books", bookService.findByUser(userService.findById(userId)));
        model.addAttribute("account", userService.findById(userId));
        return "/users/user";
    }
    
    @PostMapping("/deleteUser")
    public String deleteUser(User user, @PathVariable("userId") long userId) {

        List<Book> books = bookService.findByUser(userService.findById(userId)); 
        
        for (Book book : books) {
            book.setUser(null);
            bookRepo.save(book);
        }
        
        this.userService.deleteUser(userId);
        
        return "redirect:/usersList";
    }
    
    @PostMapping("/editUser")
    public String editUser(@ModelAttribute("account") User user) {
        this.userService.editUser(user);
        return "redirect:/usersList";
    }
    
    @GetMapping("/editUser")
    public String editUser(@ModelAttribute("account") User account, @AuthenticationPrincipal UserDetails user, Model model) {
        model.addAttribute("user", userService.findUser(user.getUsername()));
        model.addAttribute("account", userService.findById(account.getId()));
        
        return "/editUser";
    }
}
