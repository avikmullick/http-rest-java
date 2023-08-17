package com.sap.cc.books;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

  private BookStorage  bookStorage ;

  @Autowired
  public BookController(BookStorage bookStorage) {
    this.bookStorage = bookStorage;
  }

  @GetMapping
  public List<Book> getAllBooks(){
    return bookStorage.getAll();
  }

  @PostMapping
  public ResponseEntity<Book> addBook(@RequestBody Book book) throws URISyntaxException {
    UriComponents uriComponents = UriComponentsBuilder
      .fromPath("/api/v1/books" + "/{id}")
      .buildAndExpand(book.getId());
    Book newBook=createdBook(book.getId(), book.getAuthor());
    URI locationHeaderUri = new URI(uriComponents.getPath());
    return ResponseEntity.created(locationHeaderUri).body(newBook);
  }

  public Book createdBook(Long id,String author){
    Book book=new Book();
    book.setId(id);
    book.setAuthor(author);
    bookStorage.save(book);
    return book;
  }

  @GetMapping(path = "/{id}")
  public ResponseEntity<Book> getSingle (@PathVariable("id") Long id) throws IllegalArgumentException{
    if(id<=0){
      throw new IllegalArgumentException("Id must not be less than 1");
    }

    if(bookStorage.get(id).isPresent()) {
      return ResponseEntity.ok(bookStorage.get(id).get());
    }
    throw new NotFoundException();
  }
}
