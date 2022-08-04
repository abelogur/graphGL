package com.example.demo.rest.controller;

import com.example.demo.entity.Author;
import com.example.demo.entity.Book;
import com.example.demo.repository.AuthorRepository;
import com.example.demo.repository.BookRepository;
import com.example.demo.rest.dto.AuthorInput;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@Transactional
@RequiredArgsConstructor
public class BookController {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @QueryMapping
    public List<Book> getBooksByAuthor(@Argument Long authorId) {
        return bookRepository.findBooksByAuthor(authorId);
    }

    @QueryMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @MutationMapping
    public Book saveBook(@Argument("title") String title, @Argument("authors") List<AuthorInput> authors) {
        var newBook = bookRepository.save(new Book(title));
        var newAuthors = authors.stream().map(authorInput -> {
            var author = new Author();
            author.setName(authorInput.getName());
            author.getBooks().add(newBook);
            return author;
        }).collect(Collectors.toList());
        authorRepository.saveAll(newAuthors);
        newBook.setAuthors(newAuthors);
        return newBook;
    }
}
