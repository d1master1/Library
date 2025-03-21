package com.example.library.controller;


import com.example.library.dto.BookDto;
import com.example.library.model.Author;
import com.example.library.model.Book;
import com.example.library.service.AuthorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.User;
import com.example.library.service.BookService;
import com.example.library.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class BooksController {

    private final BookService bookService;
    private final UserService userService;
    private final AuthorService authorService;

    public BooksController(BookService bookService, UserService userService, AuthorService authorService) {
        this.bookService = bookService;
        this.userService = userService;
        this.authorService = authorService;
    }
    
    @GetMapping("/addBook")
    String addBook(@AuthenticationPrincipal User user, Model model) {
        if (user != null){
            model.addAttribute("user", userService.findUser(user.getUsername()));
        }
        else {
            model.addAttribute("user", null);
        }
        model.addAttribute("books", bookService.findAllBooks());
        model.addAttribute("book", new BookDto());
        model.addAttribute("authors", authorService.findAllAuthors());
        return "addBook";
    }
    
    @PostMapping("/saveBook")
    String saveBook(@ModelAttribute("book") BookDto bookDto, @AuthenticationPrincipal User user, BindingResult bindingResult) {
        
        bookDto.setUser(userService.findUser(user.getUsername()));
        
        if (bindingResult.hasErrors()) {
            return "addBook";
        }
        
        bookService.saveBook(bookDto);
        
        return "redirect:/profile";
    }
    //Пагинация
    @GetMapping("/booksList")
    String booksList(@AuthenticationPrincipal User user, Model model, @RequestParam("page") Optional<Integer> page, 
                     @RequestParam("size") Optional<Integer> size) {
        
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(10);

        Page<Book> bookPage = bookService.findPaginated(PageRequest.of(currentPage - 1, pageSize));
        
        model.addAttribute("bookPage", bookPage);
        
        int totalPages = bookPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        
        
        model.addAttribute("user", userService.findUser(user.getUsername()));
        model.addAttribute("books", bookService.findAllBooks());
        return "booksList";
    }

}
