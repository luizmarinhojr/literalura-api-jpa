package com.luizmarinho.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "book_authors",
            joinColumns = @JoinColumn(name="book_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name="author_id", nullable = false))
    private Set<Author> authors = new HashSet<>();

    private List<String> subjects;

    @Column(nullable = false)
    private List<String> languages;

    private boolean copyright;

    @Column(name = "download_count")
    @JsonAlias("download_count")
    private long downloadCount;

    public Book() {}

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

    @Override
    public String toString() {
        String hasCopyright = isCopyright() ? "Sim" : "Nao";
        return "Titulo: " + getTitle() + '\'' + "\n" +
                "Autor: " + getAuthors() + "\n" +
                "Assuntos: " + getSubjects() + "\n" +
                "Idioma(s): " + getLanguages() + "\n" +
                "Possui copyright: " + hasCopyright + "\n" +
                "Quantidade de downloads: " + getDownloadCount() + "\n";
    }
}
