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
public class BookRepositoryTest extends DaoIt {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    public void saveBooks() {
        bookRepository.save(new Book("book1"));
        bookRepository.save(new Book("book2"));
        bookRepository.save(new Book("book3"));

        assertEquals(3, bookRepository.findAll().size());
    }

    @Test
    public void saveBooksWithNullTitle__shouldThrowException() {
        assertThrows(Exception.class, () -> bookRepository.save(new Book()));
    }

    @Test
    public void updateBook() {
        bookRepository.save(new Book("book"));
        var newBookTitle = "new-book-title";

        var book = bookRepository.findAll().get(0);
        book.setTitle(newBookTitle);
        bookRepository.save(book);

        var updatedBook = bookRepository.findById(book.getId()).get();
        assertEquals(newBookTitle, book.getTitle());
    }

    @Test
    public void deleteBook() {
        bookRepository.save(new Book("book1"));
        bookRepository.save(new Book("book2"));
        bookRepository.save(new Book("book3"));

        var deletedBook = bookRepository.findAll().stream().findAny().get();
        bookRepository.delete(deletedBook);

        assertEquals(2, bookRepository.findAll().size());
        assertFalse(bookRepository.findAll().stream().anyMatch(book -> book.getId().equals(deletedBook.getId())));
    }

    @Test
    public void saveBookWithAuthor() {
        var authors = addAuthors();
        var book = new Book("book");
        book.setAuthors(authors);

        book = bookRepository.save(book);

        assertEquals(2, book.getAuthors().size());
    }

    @Test
    public void deleteAuthorFromBook() {
        var authors = addAuthors();
        var book = new Book("book");
        book.setAuthors(authors);

        book = bookRepository.save(book);
        book.setAuthors(book.getAuthors().stream().limit(1).collect(Collectors.toList()));
        bookRepository.save(book);

        assertEquals(1, book.getAuthors().size());
    }

    private List<Author> addAuthors() {
        authorRepository.save(new Author("author1"));
        authorRepository.save(new Author("author2"));

        return authorRepository.findAll();
    }
}
