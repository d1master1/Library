package com.example.library.impl;

import com.example.library.dto.AuthorDto;
import com.example.library.model.Author;
import com.example.library.repo.AuthorRepo;
import com.example.library.repo.BookRepo;
import com.example.library.service.AuthorService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepo authorRepo;
    private final BookRepo bookRepo;

    public AuthorServiceImpl(AuthorRepo authorRepo, BookRepo bookRepo) {
        this.authorRepo = authorRepo;
        this.bookRepo = bookRepo;
    }

    @Override
    public List<Author> findAllAuthors() {
        return authorRepo.findAll();
    }

    @Override
    public void saveAuthor(AuthorDto authorDto) {
        Author newAuthor = new Author();
        newAuthor.setName(authorDto.getName());
        newAuthor.setSurname(authorDto.getSurname());
        authorRepo.save(newAuthor);
    }

    @Override
    public Author findAuthorById(Long id) {
        return authorRepo.findById(id).orElseThrow(null);
    }

    @Override
    public void deleteAuthor(Long id) {
        this.authorRepo.deleteById(id);
    }

    @Override
    public void editAuthor(Author author) {
        Author editedAuthor = authorRepo.findById(author.getId()).orElseThrow(null);
        if (editedAuthor == null) {
            throw new IllegalArgumentException("Автор не найден");
        }
        editedAuthor.setName(author.getName());
        editedAuthor.setSurname(author.getSurname());
        authorRepo.save(editedAuthor);
    }

    @Override
    public boolean hasBooks(Long id) {
        return bookRepo.existsByAuthorId(id);
    }
}
