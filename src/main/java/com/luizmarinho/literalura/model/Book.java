package com.luizmarinho.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name="books")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private UUID id;

    @Column(nullable = false)
    private String title;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name = "book_authors",
            joinColumns = @JoinColumn(name="book_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name="author_id", nullable = false))
    private Set<Author> authors = new HashSet<>();

    private List<String> subjects;

    @Column(nullable = false)
    private List<String> languages;

    private boolean copyright;

    @Column(name = "download_count", nullable = false)
    @JsonAlias("download_count")
    private long downloadCount;

    public Book() {}

    public Book(String title, List<String> subjects, List<String> languages, boolean copyright, long downloadCount) {
        this.title = title;
        this.subjects = subjects;
        this.languages = languages;
        this.copyright = copyright;
        this.downloadCount = downloadCount;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public List<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<String> subjects) {
        this.subjects = subjects;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public boolean isCopyright() {
        return copyright;
    }

    public void setCopyright(boolean copyright) {
        this.copyright = copyright;
    }

    public long getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(long downloadCount) {
        this.downloadCount = downloadCount;
    }

    public String showForAuthors() {
        return getTitle();
    }

    public void addNewAuthor(Author author) {
        this.authors.add(author);
    }

    @Override
    public String toString() {
        String hasCopyright = isCopyright() ? "Sim" : "Nao";
        return "----------------------- LIVRO -----------------------\n" +
                "Titulo: " + title + '\'' + "\n" +
                "Autor(es): " + authors.stream().map(Author::getName).collect(Collectors.toSet()) + "\n" +
                "Assuntos: " + subjects + "\n" +
                "Idioma(s): " + languages + "\n" +
                "Possui copyright: " + hasCopyright + "\n" +
                "Quantidade de downloads: " + downloadCount + "\n" +
                "-----------------------------------------------------\n";
    }
}
