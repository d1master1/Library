package com.example.library.impl;

import com.example.library.dto.BookDto;
import com.example.library.model.Book;
import com.example.library.model.User;
import com.example.library.repo.BookRepo;
import com.example.library.repo.UserRepo;
import com.example.library.service.BookService;
import org.hibernate.type.ListType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    
    private final BookRepo bookRepo;

    public BookServiceImpl(BookRepo bookRepo) {
        this.bookRepo = bookRepo;
    }


    @Override
    public List<Book> findAllBooks() {
        return bookRepo.findAll();
    }

    @Override
    public List<Book> findByUser(User user) {
        return bookRepo.findByUser(user);
    }

    @Override
    public void saveBook(BookDto bookDto) {
        Book newBook = new Book();
        newBook.setTitle(bookDto.getTitle());
        newBook.setAuthor(bookDto.getAuthor());
        newBook.setAddedAt(LocalDateTime.now());
        newBook.setUser(bookDto.getUser());
        bookRepo.save(newBook);
    }

    @Override
    public Book findBookById(Long id) {
        return bookRepo.findById(id).orElse(null);
    }

    @Override
    public void deleteBook(Long id) {
        this.bookRepo.deleteById(id);
    }

    @Override
    public void editBook(Book book) {
        Book editedBook = bookRepo.findById(book.getId()).orElse(null);
        if (editedBook == null) {
            throw new IllegalArgumentException("Книга не найдена");
        }
        editedBook.setTitle(book.getTitle());
        editedBook.setAuthor(book.getAuthor());
        editedBook.setAddedAt(LocalDateTime.now());
        editedBook.setUser(book.getUser());
        bookRepo.save(editedBook);
    }
    
    @Override
    public Page<Book> findPaginated(Pageable pageable) {
        
        final List<Book> books = bookRepo.findAll();
        
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Book> list;
        
        if (books.size() < startItem) {
            list = Collections.emptyList();
        }
        else {
            int toIndex = Math.min(startItem + pageSize, books.size());
            list = books.subList(startItem, toIndex);
        }

        return new PageImpl<Book>(list, PageRequest.of(currentPage, pageSize), books.size());
        
    }


}
