package com.example.library.controller;

import com.example.library.model.Author;
import com.example.library.service.AuthorService;
import com.example.library.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AuthorControllerTest {

    @Mock
    private AuthorService authorService;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthorController authorController;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    private Author testAuthor;
    private User testUser;

    @Before
    public void setUp() {
        testAuthor = new Author();
        testAuthor.setId(1L);
        testAuthor.setName("Test Author");

        testUser = new User("username", "password", new ArrayList<>());
    }

    @Test
    public void testAuthorModelAttribute() {
        when(authorService.findAuthorById(1L)).thenReturn(testAuthor);

        Author author = authorController.author(1L);

        assertEquals(testAuthor, author);
        verify(authorService, times(1)).findAuthorById(1L);
    }

    @Test
    public void testGetAuthor() {
        when(userService.findUser("username")).thenReturn(new com.example.library.model.User()); // Mock User object

        String viewName = authorController.getAuthor(testUser, model);

        assertEquals("/authors/author", viewName);
        verify(model, times(1)).addAttribute("user", userService.findUser("username"));
    }

    @Test
    public void testDeleteAuthor_Success() {
        when(authorService.hasBooks(testAuthor.getId())).thenReturn(false);

        String redirectUrl = authorController.deleteAuthor(testAuthor, redirectAttributes);

        assertEquals("redirect:/authorsList", redirectUrl);
        verify(authorService, times(1)).hasBooks(testAuthor.getId());
        verify(authorService, times(1)).deleteAuthor(testAuthor.getId());
    }

    @Test
    public void testDeleteAuthor_Failure_HasBooks() {
        when(authorService.hasBooks(testAuthor.getId())).thenReturn(true);

        String redirectUrl = authorController.deleteAuthor(testAuthor, redirectAttributes);

        assertEquals("redirect:/authorsList", redirectUrl);
        verify(authorService, times(1)).hasBooks(testAuthor.getId());
        verify(redirectAttributes, times(1)).addFlashAttribute("errorMessage", "Невозможно удалить автора, так как у него есть связанные книги.");
        verify(authorService, never()).deleteAuthor(testAuthor.getId());
    }

    @Test
    public void testEditBook() {
        String redirectUrl = authorController.editBook(testAuthor);

        assertEquals("redirect:/authors/author/1", redirectUrl);
        verify(authorService, times(1)).editAuthor(testAuthor);
    }

    @Test
    public void testGetEditAuthor() {
        when(userService.findUser("username")).thenReturn(new com.example.library.model.User());
        when(authorService.findAuthorById(testAuthor.getId())).thenReturn(testAuthor);

        String viewName = authorController.getEditAuthor(testAuthor, testUser, model);

        assertEquals("/editAuthor", viewName);
        verify(model, times(1)).addAttribute("user", userService.findUser("username"));
        verify(model, times(1)).addAttribute("author", authorService.findAuthorById(testAuthor.getId()));

    }

}