package com.luizmarinho.literalura.repository;

import com.luizmarinho.literalura.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthorRepository extends JpaRepository<Author, String> {

    Optional<Author> findByName(String name);


}
