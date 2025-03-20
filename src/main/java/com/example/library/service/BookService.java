package com.example.library.service;

import com.example.library.dto.BookDto;
import com.example.library.model.Book;
import com.example.library.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BookService {
    
    List<Book> findAllBooks();
    
    List<Book> findByUser(User user);
    
    void saveBook(BookDto bookDto);
    
    Book findBookById(Long id);

    void deleteBook(Long id);

    void editBook(Book book);

    Page<Book> findPaginated(Pageable pageable);
    
    
}
