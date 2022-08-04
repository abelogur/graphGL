package com.example.demo.repository;

import com.example.demo.DaoIt;
import com.example.demo.entity.Author;
import com.example.demo.entity.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
public class AuthorRepositoryTest extends DaoIt {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void saveAuthor() {
        authorRepository.save(new Author("author1"));
        authorRepository.save(new Author("author2"));
        authorRepository.save(new Author("author3"));

        assertEquals(3, authorRepository.findAll().size());
    }

    @Test
    public void saveAuthorWithNullName__shouldThrowException() {
        assertThrows(Exception.class, () -> authorRepository.save(new Author()));
    }

    @Test
    public void saveAuthorsWithEqualNames__shouldThrowException() {
        var name = "author";
        assertThrows(Exception.class, () -> {
            authorRepository.save(new Author(name));
            authorRepository.save(new Author(name));
        });
    }

    @Test
    public void updateAuthor() {
        authorRepository.save(new Author("author"));
        var newAuthorName = "new-author-name";

        var author = authorRepository.findAll().get(0);
        author.setName(newAuthorName);
        authorRepository.save(author);

        var updatedAuthor = authorRepository.findById(author.getId()).get();
        assertEquals(newAuthorName, updatedAuthor.getName());
    }

    @Test
    public void deleteAuthor() {
        authorRepository.save(new Author("author1"));
        authorRepository.save(new Author("author2"));
        authorRepository.save(new Author("author3"));

        var deletedAuthor = authorRepository.findAll().stream().findAny().get();
        authorRepository.delete(deletedAuthor);

        assertEquals(2, authorRepository.findAll().size());
        assertFalse(authorRepository.findAll().stream().anyMatch(author -> author.getId().equals(deletedAuthor.getId())));
    }

    @Test
    public void saveAuthorWithBooks() {
        var books = addBooks();
        var author = new Author("author");
        author.setBooks(books);

        author = authorRepository.save(author);

        assertEquals(2, author.getBooks().size());
    }

    @Test
    public void deleteBookFromAuthor() {
        var books = addBooks();
        var author = new Author("author");
        author.setBooks(books);

        author = authorRepository.save(author);
        author.setBooks(author.getBooks().stream().limit(1).collect(Collectors.toList()));
        authorRepository.save(author);

        assertEquals(1, author.getBooks().size());
    }

    private List<Book> addBooks() {
        bookRepository.save(new Book("book1"));
        bookRepository.save(new Book("book2"));

        return bookRepository.findAll();
    }
}
