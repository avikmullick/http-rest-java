package com.sap.cc.library.book;

import com.sap.cc.library.BookRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class BookRepositoryTest {
  @Autowired
  private BookRepository repository;
  @BeforeEach
  public void clearDb(){
    repository.deleteAll();
  }

  @Test
  public void findAll(){
    List<Book> books = repository.findAll();
    Assertions.assertThat(books).isEmpty();
  }

  @Test
  public void bookMultipleAction(){

    Book createdBook= repository.save(BookFixtures.cleanCode());
    List<Book> books = repository.findAll();
    Assertions.assertThat(books).hasSize(1);
    Assertions.assertThat(createdBook.getTitle()).isEqualTo(BookFixtures.cleanCode().getTitle());
    Assertions.assertThat(createdBook.getAuthor().getName()).isEqualTo(BookFixtures.cleanCode().getAuthor().getName());
  }

  @Test
  public void findBookByTitle(){
    Book cleanCodeBook=repository.save(BookFixtures.cleanCode());
    Book designPatternsBook=repository.save(BookFixtures.designPatterns());
    Book cleanCodeBookReturned= repository.findByTitle(BookFixtures.cleanCode().getTitle());
    Assertions.assertThat(cleanCodeBookReturned.getTitle()).isEqualTo(BookFixtures.cleanCode().getTitle());
  }
}
