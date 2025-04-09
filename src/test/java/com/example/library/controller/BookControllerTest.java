package com.example.library.controller;

import com.example.library.model.Author;  //Import model instead of dto
import com.example.library.model.Book;
import com.example.library.model.User;
import com.example.library.service.AuthorService;
import com.example.library.service.BookService;
import com.example.library.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BookControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private BookService bookService;

    @Mock
    private AuthorService authorService;

    @InjectMocks
    private BookController bookController;

    @Mock
    private Model model;

    private Book testBook;
    private User testUser;
    private UserDetails testSpringUser;

    @Before
    public void setUp() {
        testBook = new Book();
        testBook.setId(1L);
        testBook.setTitle("Test Book");

        testUser = new User();
        testUser.setUsername("testuser");

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        testSpringUser = new org.springframework.security.core.userdetails.User("testuser", "password", authorities);
    }

    @Test
    public void testBookModelAttribute() {
        when(bookService.findBookById(1L)).thenReturn(testBook);

        Book book = bookController.book(1L);

        assertEquals(testBook, book);
        verify(bookService, times(1)).findBookById(1L);
    }

    @Test
    public void testGetBook() {
        when(userService.findUser("testuser")).thenReturn(testUser);

        String viewName = bookController.getBook((org.springframework.security.core.userdetails.User) testSpringUser, model);

        assertEquals("/books/book", viewName);
        verify(model, times(1)).addAttribute("user", testUser);
    }

    @Test
    public void testDeleteBook_AdminRole() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        org.springframework.security.core.userdetails.User adminUser = new org.springframework.security.core.userdetails.User("admin", "password", authorities);

        bookController.deleteBook(testBook, adminUser);
        verify(bookService, times(1)).deleteBook(testBook.getId());

        String redirect = bookController.deleteBook(testBook, adminUser);
        assertEquals("redirect:/booksList", redirect);

        verify(bookService, times(2)).deleteBook(testBook.getId());

    }

    @Test
    public void testDeleteBook_UserRole() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        org.springframework.security.core.userdetails.User userUser = new org.springframework.security.core.userdetails.User("user", "password", authorities);

        bookController.deleteBook(testBook, userUser);
        verify(bookService, times(1)).deleteBook(testBook.getId());

        String redirect = bookController.deleteBook(testBook, userUser);
        assertEquals("redirect:/profile", redirect);

        verify(bookService, times(2)).deleteBook(testBook.getId());
    }

    @Test
    public void testDeleteBook_NoRole() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        org.springframework.security.core.userdetails.User noRoleUser = new org.springframework.security.core.userdetails.User("noRole", "password", authorities);
        bookController.deleteBook(testBook, noRoleUser);
        verify(bookService, times(1)).deleteBook(testBook.getId());

        String redirect = bookController.deleteBook(testBook, noRoleUser);
        assertEquals("redirect:/", redirect);

        verify(bookService, times(2)).deleteBook(testBook.getId());
    }

    @Test
    public void testEditBook() {
        String redirectUrl = bookController.editBook(testBook);

        assertEquals("redirect:/books/book/1", redirectUrl);
        verify(bookService, times(1)).editBook(testBook);
    }

    @Test
    public void testGetEditBook() {
        when(userService.findUser("testuser")).thenReturn(testUser);
        when(bookService.findBookById(testBook.getId())).thenReturn(testBook);
        List<Author> authorList = new ArrayList<>();
        authorList.add(new Author());
        when(authorService.findAllAuthors()).thenReturn(authorList);

        String viewName = bookController.getEditBook(testBook, (org.springframework.security.core.userdetails.User) testSpringUser, model);

        assertEquals("/editBook", viewName);
        verify(model, times(1)).addAttribute("user", testUser);
        verify(model, times(1)).addAttribute("book", testBook);
        verify(model, times(1)).addAttribute("authors", authorList);
    }
}