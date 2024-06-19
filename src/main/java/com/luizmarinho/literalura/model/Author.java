package com.luizmarinho.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name="authors")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Author {

    @Id
    @Column(nullable = false)
    private String name;

    @ManyToMany(mappedBy = "authors", cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private Set<Book> books = new HashSet<>();

    @Column(name = "birth_year", nullable = false)
    @JsonAlias("birth_year")
    private int birthYear;

    @Column(name = "death_year")
    @JsonAlias("death_year")
    private int deathYear;

    public Author() {}

    public Author(String name, int birthYear, int deathYear, Set<Book> books) {
        this.name = name;
        this.birthYear = birthYear;
        this.deathYear = deathYear;
        this.books = books;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }

    public int getDeathYear() {
        return deathYear;
    }

    public void setDeathYear(int deathYear) {
        this.deathYear = deathYear;
    }

    @Override
    public String toString() {

        return "Autor: " + name + "\n" +
                "Ano de nascimento: " + birthYear + "\n" +
                "Ano de falecimento: " + deathYear + "\n" +
                "Livros: " + books.stream().map(Book::showForAuthors).collect(Collectors.toSet()) + "\n";
    }
}
