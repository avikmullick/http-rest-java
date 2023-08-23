package com.sap.cc.library.book;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class DataDurabilityTest {
  @Autowired
  private BookRepository repository;

  @Test
  public void populateDb(){
    repository.save(BookFixtures.cleanCode());
    repository.save(BookFixtures.designPatterns());
    repository.save(BookFixtures.modernOperatingSystems());
    List<Book> books = repository.findAll();
    Assertions.assertThat(books).hasSizeGreaterThanOrEqualTo(3);
  }

  @Test
  public void isDbPopulated(){
    List<Book> books = repository.findAll();
    Assertions.assertThat(books).hasSizeGreaterThanOrEqualTo(3);
  }
}
