package book.bookstore.dao;

import java.util.List;

import book.bookstore.entity.Book;

public interface BookRepository {
	
	 Boolean addBook(Book book);
	    List<Book> getAllBooks();
	    Book getBookById(int id);
	    List<Book> getBookByAuthor(String author);
	    Integer updateBook(Book book);
	    Integer deleteBook(Integer id);
}
