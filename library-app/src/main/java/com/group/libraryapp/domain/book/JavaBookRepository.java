package com.group.libraryapp.domain.book;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JavaBookRepository extends JpaRepository<Book, Long> {

  Optional<Book> findByName(String bookName);

}
