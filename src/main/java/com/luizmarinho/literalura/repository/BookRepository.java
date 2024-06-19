package com.luizmarinho.literalura.repository;

import com.luizmarinho.literalura.model.Book;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {
//    Optional<List<Book>> findBookContainingLanguages(String language);
}
