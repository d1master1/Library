package com.example.library.controller;


import com.example.library.model.Book;
import com.example.library.service.AuthorService;
import com.example.library.service.UserService;
import org.springframework.security.core.userdetails.User;
import com.example.library.service.BookService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("books/book/{bookId:\\d+}")
public class BookController {
    
    private final UserService userService;
    private final BookService bookService;
    private final AuthorService authorService;

    public BookController(UserService userService, BookService bookService, AuthorService authorService) {
        this.userService = userService;
        this.bookService = bookService;
        this.authorService = authorService;
    }
    
    @ModelAttribute("book")
    private Book book(@PathVariable long bookId) {
        return this.bookService.findBookById(bookId);
    }
    
    @GetMapping
    public String getBook(@AuthenticationPrincipal User user, Model model){
        model.addAttribute("user", userService.findUser(user.getUsername()));
        return "/books/book";
    }
    
    @PostMapping("/deleteBook")
    public String deleteBook(Book book, @AuthenticationPrincipal User user){
        this.bookService.deleteBook(book.getId());
        if (user.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))){
            return "redirect:/booksList";
        }
        else if (user.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER"))){
            return "redirect:/profile";
        }
        else {
            return "redirect:/";
        }
    }
    
    @PostMapping("/editBook")
    public String editBook(Book book){
        this.bookService.editBook(book);
        return "redirect:/books/book/" + book.getId();
    }
    
    @GetMapping("/editBook")
    public String getEditBook(@ModelAttribute("book") Book book, @AuthenticationPrincipal User user, Model model){
        model.addAttribute("user", userService.findUser(user.getUsername()));
        model.addAttribute("book", bookService.findBookById(book.getId()));
        model.addAttribute("authors", authorService.findAllAuthors());
        return "/editBook";
    }
    
}
