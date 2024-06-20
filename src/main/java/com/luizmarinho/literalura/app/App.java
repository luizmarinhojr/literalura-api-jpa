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
                [2] - Listar os top 10 livros mais baixados
                [3] - Listar livros registrados
                [4] - Listar autores registrados
                [5] - Listar autores vivos em um determinado ano
                [6] - Listar livros em um determinado idioma
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
                    showTop10DownloadedBooks();
                    break;
                case 3:
                    showRegisteredBooks();
                    break;
                case 4:
                    showRegisteredAuthors();
                    break;
                case 5:
                    showRegisteredAuthorsAlive();
                    break;
                case 6:
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
                if (books != null) {
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
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                System.out.println("\nDigite apenas números inteiros válidos");
            }
            saveBook(books.get(choiceUniqueBook - 1));
        }
    }

    private void saveBook(Book book) {
        boolean doAgain = true;
        while(doAgain) {
            try {
                System.out.println("\nDeseja registrar esse livro? [1] Sim  [2] Não ");
                int choiceRegistered = Integer.parseInt(reader.nextLine());

                if (choiceRegistered == 1) {
                    Optional<Author> authorFromDbOptional;
                    Book bookNew = new Book(book.getTitle(), book.getSubjects(),
                            book.getLanguages(), book.isCopyright(), book.getDownloadCount());

                    for (Author authorFromRam : book.getAuthors()) {
                        authorFromDbOptional = authorRepository.findByName(authorFromRam.getName());

                        Author author;
                        if (authorFromDbOptional.isPresent()) { // Se o autor estiver presente no banco de dados
                            var authorTemp = authorFromDbOptional.get();
                            author = new Author(authorTemp.getName(), authorTemp.getBirthYear(),
                                    authorTemp.getDeathYear(), authorTemp.getBooks());
                            bookNew.addNewAuthor(author);
                        } else {
                            author = new Author(authorFromRam.getName(), authorFromRam.getBirthYear(),
                                    authorFromRam.getDeathYear(), authorFromRam.getBooks());
                            authorRepository.save(author);
                            bookNew.addNewAuthor(author);
                        }
                    }
                    bookRepository.save(bookNew);
                    System.out.println("\nSalvo com sucesso!");
                    doAgain = false;
                } else if (choiceRegistered == 2) {
                    System.out.println("\nOk, voltando para o menu principal");
                } else {
                    System.out.println("\nOk, voltando para o menu principal");
                }
            } catch (NumberFormatException e) {
                System.out.println("Digite apenas números inteiros válidos");
            }
            System.out.println("\nPressione Enter para voltar ao menu principal");
            reader.nextLine();
        }
    }

    private void showTop10DownloadedBooks() {
        var books = bookRepository.findTop10MostDownloadedBooks();
        if (!books.isEmpty()) {
            for (int i = 0; i < books.size(); i++) {
                if (i == 0) {
                    System.out.println("-----------------------------------\n" +
                            "[" + (i + 1) + "ª]" + " - Titulo: " + books.get(i).getTitle() + "\n" + "------" +
                            " Autor(es): " + books.get(i).getAuthors().stream()
                            .map(Author::getName).collect(Collectors.toSet()) + "\n" + "------" +
                            " Downloads: " + books.get(i).getDownloadCount() +
                            "\n-----------------------------------\n");
                } else {
                    System.out.println("[" + (i + 1) + "ª]" + " - Titulo: " + books.get(i).getTitle() + "\n" + "------" +
                            " Autor(es): " + books.get(i).getAuthors().stream()
                            .map(Author::getName).collect(Collectors.toSet()) + "\n" + "------" +
                            " Downloads: " + books.get(i).getDownloadCount() + "\n");
                }
            }
        } else {
            System.out.println("Não há nenhum livro cadastrado");
        }
        System.out.println("Pressione Enter para voltar ao menu principal");
        reader.nextLine();
    }

    private void showRegisteredBooks() {
        var registeredBooks = bookRepository.findAll();
        if (!registeredBooks.isEmpty()) {
            registeredBooks.forEach(System.out::println);
        } else {
            System.out.println("\nNão há livros registrados");
        }
        System.out.println("Pressione Enter para voltar ao menu principal");
        reader.nextLine();
    }

    private void showRegisteredAuthors() {
        var registeredAuthors = authorRepository.findAll();
        if (!registeredAuthors.isEmpty()) {
            registeredAuthors.forEach(System.out::println);
        } else {
            System.out.println("\nNão há autores registrados");
        }
        System.out.println("Pressione Enter para voltar ao menu principal");
        reader.nextLine();
    }

    private void showRegisteredAuthorsAlive() {
        try {
            System.out.println("\nDigite o ano: ");
            int choiceYear = Integer.parseInt(reader.nextLine());
            var authors = authorRepository.findAuthorAlive(choiceYear);

            if (!authors.isEmpty()) {
                authors.forEach(System.out::println);
            } else {
                System.out.println("Não há autores registrados no banco de dados vivos nesse ano");
            }
        } catch (NumberFormatException e) {
            System.out.println("\nDigite apenas números inteiros");
        }
        System.out.println("Pressione Enter para voltar ao menu principal");
        reader.nextLine();
    }

    private void showRegisteredBooksByLanguage() {
        System.out.println("""

                ---------- OPÇÕES ----------
                [es] - Espanhol
                [en] - Inglês
                [fr] - Francês
                [pt] - Português

                Digite o idioma (Ex: pt):\s""");
        String choiceLanguage = reader.nextLine();

        var registeredBooksByLanguage = bookRepository.findContainingLanguages(choiceLanguage);

        if (!registeredBooksByLanguage.isEmpty()) {
            registeredBooksByLanguage.forEach(System.out::println);
        } else {
            System.out.println("\nNão existem livros registrados nesse idioma");
        }
        System.out.println("Pressione Enter para voltar ao menu principal");
        reader.nextLine();
    }
}
