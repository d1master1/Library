package com.example.library.controller;

import com.example.library.model.Book;
import com.example.library.model.User;
import com.example.library.repo.BookRepo;
import com.example.library.service.BookService;
import com.example.library.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private BookService bookService;

    @Mock
    private BookRepo bookRepo;

    @InjectMocks
    private UserController userController;

    @Mock
    private Model model;

    private User testUser;
    private UserDetails testSpringUser;
    private Book testBook;

    @Before
    public void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        testSpringUser = org.springframework.security.core.userdetails.User.withUsername("testuser")
                .password("password")
                .roles("USER")
                .build();

        testBook = new Book();
        testBook.setId(10L);
        testBook.setTitle("Test Book");
        testBook.setUser(testUser);
    }

    @Test
    public void testGetUserModelAttribute() {
        when(userService.findById(1L)).thenReturn(testUser);

        User account = userController.getUser(1L);

        assertEquals(testUser, account);
        verify(userService, times(1)).findById(1L);
    }

    @Test
    public void testGetUser() {
        when(userService.findUser("testuser")).thenReturn(testUser);
        when(userService.findById(1L)).thenReturn(testUser);

        List<Book> books = new ArrayList<>();
        books.add(testBook);
        when(bookService.findByUser(testUser)).thenReturn(books);

        String viewName = userController.getUser(testSpringUser, model, 1L);

        assertEquals("/users/user", viewName);
        verify(model, times(1)).addAttribute("user", testUser);
        verify(model, times(1)).addAttribute("books", books);
        verify(model, times(1)).addAttribute("account", testUser);
    }

    @Test
    public void testDeleteUser() {
        List<Book> books = new ArrayList<>();
        books.add(testBook);

        when(userService.findById(1L)).thenReturn(testUser);
        when(bookService.findByUser(testUser)).thenReturn(books);

        String redirectUrl = userController.deleteUser(testUser, 1L);

        assertEquals("redirect:/usersList", redirectUrl);
        verify(userService, times(1)).findById(1L);
        verify(bookService, times(1)).findByUser(testUser);
        verify(bookRepo, times(1)).save(testBook);
        verify(userService, times(1)).deleteUser(1L);
    }

    @Test
    public void testEditUser_Post() {
        String redirectUrl = userController.editUser(testUser);

        assertEquals("redirect:/usersList", redirectUrl);
        verify(userService, times(1)).editUser(testUser);
    }

    //не рабочий
    @Test
    public void testEditUser_Get() {
        when(userService.findUser("testuser")).thenReturn(testUser);
        when(userService.findById(1L)).thenReturn(testUser);

        String viewName = userController.editUser(testUser, testSpringUser, model);

        assertEquals("/editUser", viewName);
        verify(model, times(1)).addAttribute("user", testUser);
        verify(model, times(1)).addAttribute("account", testUser);
    }
}