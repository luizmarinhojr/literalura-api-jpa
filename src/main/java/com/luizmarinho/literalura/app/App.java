package com.luizmarinho.literalura.app;

import com.luizmarinho.literalura.model.Book;
import com.luizmarinho.literalura.repository.AuthorRepository;
import com.luizmarinho.literalura.repository.BookRepository;
import com.luizmarinho.literalura.service.ApiConsumption;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

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
                [1] - Buscar livro pelo título
                [2] - Listar livros registrados
                [3] - Listar autores registrados
                [4] - Listar autores vivos em um determinado ano
                [5] - Listar livros em um determinado idioma
                [0] - Sair
                
                Escolha o número da sua opção:
                """;
        int choiceMenu = -1;
        while (choiceMenu != 0) {
            System.out.println("\n******** BEM VINDO AO LITERALURA ********\n");

            System.out.print(menu);
            choiceMenu = reader.nextInt();

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
//        String title = "dom casmurro";
//
//        List<Book> books = api.searchBook(title);
//
//        books.forEach(System.out::println);
//
//        bookRepository.save(books.getFirst());
//
//        System.out.println("teste continuidade");

    }

    private void showRegisteredBooksByLanguage() {
        var registeredBooks = bookRepository.findAll();
        registeredBooks.forEach(System.out::println);
    }

    private void showRegisteredAuthorsByBirthYear() {
    }

    private void showRegisteredAuthors() {
    }

    private void showRegisteredBooks() {
        var registeredBooks = bookRepository.findAll();
        if (!registeredBooks.isEmpty()) {
            registeredBooks.forEach(System.out::println);
        } else {
            System.out.println("\nNão há livros registrados");
        }
    }

    private void searchBookByTitle() {
        System.out.println("\nDigite o título do livro: ");
        String bookTitle = reader.nextLine();
        choiceBooks(api.searchBook(bookTitle));
    }

    private void choiceBooks(List<Book> books) {
        int choiceUniqueBook = 1;
        try {
            if (books.size() > 1) {
                for (int i = 0; i < books.size(); i++) {
                    System.out.println("[" + (i + 1) + "]" + " - " + books.get(i).getTitle() + "\n" + "-----" +
                            " Autor: " + books.get(i).getAuthors() + "\n");
                }
                System.out.println("\nEscolha o número do livro: ");
                choiceUniqueBook = reader.nextInt();

                System.out.println("Você escolheu o livro: \n" + books.get(choiceUniqueBook -1));
            } else {
                books.forEach(System.out::println);
            }
            saveBook(books.get(choiceUniqueBook - 1));
        } catch(InputMismatchException e) {
            System.out.println("\nDigite apenas números");
        }
    }

    private void saveBook(Book book) throws InputMismatchException{
        System.out.println("\nDeseja registrar esse livro? [1] Sim  [2] Não ");
        int choiceRegistered = reader.nextInt();

        if (choiceRegistered == 1) {
            bookRepository.save(book);
            System.out.println("Salvo com sucesso!\nVoltando para o menu principal...\n");
        } else {
            System.out.println("Ok, voltando para o menu principal");
        }
    }
}
