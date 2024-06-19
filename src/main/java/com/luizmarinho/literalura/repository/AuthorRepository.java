package com.luizmarinho.literalura.repository;

import com.luizmarinho.literalura.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, String> {

    Optional<Author> findByName(String name);

    @Query(value = "SELECT * FROM authors WHERE :year BETWEEN birth_year AND death_year", nativeQuery = true)
    List<Author> findAuthorAlive(@Param("year") int year);


}
