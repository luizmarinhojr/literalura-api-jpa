package com.luizmarinho.literalura.repository;

import com.luizmarinho.literalura.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {
    @Query(value = "SELECT * FROM books WHERE :language = ANY (languages);", nativeQuery = true)
    List<Book> findContainingLanguages(@Param("language") String language);

    @Query(value = "SELECT * FROM books ORDER BY download_count DESC LIMIT 10", nativeQuery = true)
    List<Book> findTop10MostDownloadedBooks();
}
