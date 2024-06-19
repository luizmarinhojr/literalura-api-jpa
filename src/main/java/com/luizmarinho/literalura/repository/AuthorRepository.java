package com.luizmarinho.literalura.repository;

import com.luizmarinho.literalura.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuthorRepository extends JpaRepository<Author, UUID> {
}
