package com.example.demo.rest.controller;

import com.example.demo.entity.Author;
import com.example.demo.entity.Book;
import com.example.demo.repository.AuthorRepository;
import com.example.demo.repository.BookRepository;
import com.example.demo.rest.dto.BookInput;
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
public class AuthorController {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    @QueryMapping
    public Author getAuthor(@Argument String name) {
        return authorRepository.getByName(name);
    }

    @MutationMapping
    public Author saveAuthor(@Argument("name") String name, @Argument("books") List<BookInput> books) {
        var newAuthor = authorRepository.save(new Author(name));
        var newBooks = books.stream()
                .map(bookInput -> {
                    var book = new Book();
                    book.setTitle(bookInput.getTitle());
                    book.getAuthors().add(newAuthor);
                    return book;
                }).collect(Collectors.toList());
        bookRepository.saveAll(newBooks);
        newAuthor.setBooks(newBooks);
        return newAuthor;
    }
}
