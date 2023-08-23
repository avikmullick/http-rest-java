package com.sap.cc.library;

import com.sap.cc.library.book.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

  Book findByTitle(String title);
}
