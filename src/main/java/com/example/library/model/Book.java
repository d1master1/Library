package com.example.library.model;


import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.Locale;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    private LocalDateTime addedAt;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    

    public String getFormattedCreatedAt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy", new Locale("ru"));
        return addedAt.format(formatter);
    }

    public Book(Long id, String title, Author author, LocalDateTime addedAt, User user) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.addedAt = addedAt;
        this.user = user;
    }

    public Book() {
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(LocalDateTime year) {
        this.addedAt = year;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Book{" +
                "user=" + user.getName() +
                '}';
    }
}
