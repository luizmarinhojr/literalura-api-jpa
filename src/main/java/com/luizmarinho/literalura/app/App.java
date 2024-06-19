package com.luizmarinho.literalura.app;

import com.luizmarinho.literalura.model.Author;
import com.luizmarinho.literalura.model.Book;
import com.luizmarinho.literalura.repository.AuthorRepository;
import com.luizmarinho.literalura.repository.BookRepository;
import com.luizmarinho.literalura.service.ApiConsumption;

import java.util.*;
import java.util.stream.Collectors;

public class App {
    private final ApiConsumption api = new ApiConsumption();
    private final Scanner reader = new Scanner(System.in);
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public App(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    public void execute() {
        String menu = """
                ****************** MENU *****************
                [1] - Buscar livro pelo título ou autor
                [2] - Listar livros registrados
                [3] - Listar autores registrados
                [4] - Listar autores vivos em um determinado ano
                [5] - Listar livros em um determinado idioma
                [0] - Sair
                
                Escolha o número da sua opção:
                """;
        int choiceMenu = -1;
        while (choiceMenu != 0) {
            try {
                System.out.println("\n******** BEM VINDO AO LITERALURA ********\n");
                System.out.print(menu);
                choiceMenu = Integer.parseInt(reader.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Digite apenas números inteiros");
            }

            switch (choiceMenu) {
                case 1:
                    searchBookByTitle();
                    break;
                case 2:
                    showRegisteredBooks();
                    break;
                case 3:
                    showRegisteredAuthors();
                    break;
                case 4:
                    showRegisteredAuthorsByBirthYear();
                    break;
                case 5:
                    showRegisteredBooksByLanguage();
                    break;
                case 0:
                    System.out.println("Ok, até logo! :)");
                    break;
                default:
                    System.out.println("Opção inválida, tente novamente");
            }
        }
    }

    private void searchBookByTitle() {
        System.out.println("\nDigite o título do livro: ");
        String bookTitle = reader.nextLine();
        choiceBooks(api.searchBook(bookTitle));
    }

    private void choiceBooks(List<Book> books) {
        int choiceUniqueBook = 1;
        boolean doAgain = true;
        while(doAgain) {
            try {
                if (!books.isEmpty()) {
                    if (books.size() > 1) {
                        for (int i = 0; i < books.size(); i++) {
                            System.out.println("[" + (i + 1) + "]" + " - " + books.get(i).getTitle() + "\n" + "------" +
                                    " Autor: " + books.get(i).getAuthors().stream()
                                    .map(Author::getName).collect(Collectors.toSet()) + "\n");
                        }
                        System.out.println("\nEscolha o número do livro: ");
                        choiceUniqueBook = Integer.parseInt(reader.nextLine());

                        System.out.println("\nVocê escolheu o livro: \n\n" + books.get(choiceUniqueBook - 1));
                    } else {
                        books.forEach(System.out::println);
                    }
                } else {
                    break;
                }
                doAgain = false;
                saveBook(books.get(choiceUniqueBook - 1));
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                doAgain = true;
                System.out.println("\nDigite apenas números inteiros válidos");
            }
        }
    }

    private void saveBook(Book book) {
        System.out.println("Deseja registrar esse livro? [1] Sim  [2] Não ");
        int choiceRegistered = Integer.parseInt(reader.nextLine());

        if (choiceRegistered == 1) {
            Optional<Author> authorOptional;
            Book bookNew = new Book(book.getTitle(), book.getSubjects(),
                    book.getLanguages(), book.isCopyright(), book.getDownloadCount());

            for (Author authorFromRam : book.getAuthors()) {
                authorOptional = authorRepository.findByName(authorFromRam.getName());
                System.out.println("********** Author na memoria **********\n" + authorFromRam.getName());

                Author author;
                if (authorOptional.isPresent()) {
                    var authorTemp = authorOptional.get();
                    System.out.println("********** Author no BD **********\n" + authorTemp);
                    author = new Author(authorTemp.getName(), authorTemp.getBirthYear(),
                            authorTemp.getDeathYear(), authorTemp.getBooks());
                    bookNew.addNewAuthor(author);
                    System.out.println("Saindo do if isPresent");
                } else {
                    System.out.println("***** Entrando no Else isPresent -> book *****\n");
                    author = new Author(authorFromRam.getName(), authorFromRam.getBirthYear(),
                            authorFromRam.getDeathYear(), authorFromRam.getBooks());
                    authorRepository.save(author);
                    bookNew.addNewAuthor(author);
                    System.out.println("Saindo do else isPresent");
                }
            }
            bookRepository.save(bookNew);
            System.out.println("\nSalvo com sucesso!\nVoltando para o menu principal...\n");
        } else if (choiceRegistered == 2){
            System.out.println("Ok, voltando para o menu principal");
        } else {
            System.out.println("Ok, voltando para o menu principal");
        }
    }

    private void showRegisteredBooks() {
        var registeredBooks = bookRepository.findAll();
        if (!registeredBooks.isEmpty()) {
            registeredBooks.forEach(System.out::println);
        } else {
            System.out.println("\nNão há livros registrados");
        }
    }

    private void showRegisteredAuthors() {
        var registeredAuthors = authorRepository.findAll();
        if (!registeredAuthors.isEmpty()) {
            registeredAuthors.forEach(System.out::println);
        } else {
            System.out.println("\nNão há autores registrados");
        }

    }

    private void showRegisteredAuthorsByBirthYear() {
//        try {
//            System.out.println("\nDigite o ano: ");
//            int choiceYear = Integer.parseInt(reader.nextLine());
//            authorRepository.findAuthorAlive(choiceYear);
//        } catch (NumberFormatException e) {
//            System.out.println("\nDigite apenas números inteiros");
//        }
    }

    private void showRegisteredBooksByLanguage() {
//        System.out.println("es\nen\nfr\npt\nDigite o idioma (Ex: pt): ");
//        String choiceLanguage = reader.nextLine();
//
//        var registeredBooksByLanguage = bookRepository.findBookContainingLanguages(choiceLanguage);
//        registeredBooksByLanguage.ifPresentOrElse(books -> books.forEach(System.out::println),
//                () -> System.out.println("Não existem livros registrados nesse idioma"));
    }
}
