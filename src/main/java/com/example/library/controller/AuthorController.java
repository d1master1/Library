package com.example.library.controller;

import com.example.library.model.Author;
import com.example.library.service.AuthorService;
import com.example.library.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("authors/author/{authorId:\\d+}")
public class AuthorController {
    
    private final AuthorService authorService;
    private final UserService userService;

    public AuthorController(AuthorService authorService, UserService userService) {
        this.authorService = authorService;
        this.userService = userService;
    }

    @ModelAttribute("author")
    public Author author(@PathVariable("authorId") long authorId) {
        return this.authorService.findAuthorById(authorId);
    }

    @GetMapping
    public String getAuthor(@AuthenticationPrincipal User user, Model model){
        model.addAttribute("user", userService.findUser(user.getUsername()));
        return "/authors/author";
    }

    @PostMapping("/deleteAuthor")
    public String deleteAuthor(@ModelAttribute("author") Author author, RedirectAttributes redirectAttributes) {

        if (authorService.hasBooks(author.getId())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Невозможно удалить автора, так как у него есть связанные книги.");
            return "redirect:/authorsList";
        }

        authorService.deleteAuthor(author.getId());
        return "redirect:/authorsList";
    }

    @PostMapping("/editAuthor")
    public String editBook(@ModelAttribute("author") Author author){
        this.authorService.editAuthor(author);
        return "redirect:/authors/author/" + author.getId();
    }

    @GetMapping("/editAuthor")
    public String getEditAuthor(@ModelAttribute("author") Author author, @AuthenticationPrincipal User user, Model model){
        model.addAttribute("user", userService.findUser(user.getUsername()));
        model.addAttribute("author", authorService.findAuthorById(author.getId()));
        return "/editAuthor";
    }
}
