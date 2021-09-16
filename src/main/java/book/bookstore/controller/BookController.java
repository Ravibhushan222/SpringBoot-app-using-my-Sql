package book.bookstore.controller;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import book.bookstore.dao.BookRepository;
import book.bookstore.dao.BookRepositoryImpl;
import book.bookstore.entity.Book;

@RestController
@RequestMapping("/book")
public class BookController {

	
	 @Autowired
	    BookRepository bookRepository;

	    @GetMapping("/check")
	    public String check(){
	        return "Api is working";
	    }


	    
	    @GetMapping(value = "/{id}")
	    public ResponseEntity getBook_BY_Id(@PathVariable("id") Integer id){
	    	
	                      Book book = bookRepository.getBookById(id);
	                      
	        if(book == null){
	            return new ResponseEntity<String>("No book found with this "+ id, HttpStatus.NOT_FOUND);
	        }
	        
	        return new ResponseEntity<Book>(book,HttpStatus.OK);
	    }

	    
	  
	    
	    @GetMapping
	    public ResponseEntity getBook_By_Authorr(@RequestParam(required = false) String author){
	      
	    	if(author != null) {
	            List<Book> books = bookRepository.getBookByAuthor(author);
	            
	            if (books.isEmpty()) {
	                return new ResponseEntity<String>("No books found with this " + author, HttpStatus.NOT_FOUND);
	            }
	            
	            
	            return new ResponseEntity<List<Book>>(books, HttpStatus.OK);
	        }
	        return new ResponseEntity<List<Book>>(bookRepository.getAllBooks(),HttpStatus.OK);

	    }

	    @PostMapping
	    public ResponseEntity<String> addBook(@RequestBody Book book) throws SQLIntegrityConstraintViolationException {
	    	
	        if(bookRepository.getBookById(book.getId()) != null){
	            return new ResponseEntity<String>("book Already exist "+ book.getId(), HttpStatus.IM_USED);
	        }

	        bookRepository.addBook(book);
	        return ResponseEntity.status(HttpStatus.CREATED).build();
	    }


	    @PutMapping
	    public ResponseEntity<?> updateBook(@RequestBody Book book) {
	    	
	    	
	        if(bookRepository.getBookById(book.getId()) == null){
	            return new ResponseEntity<String>("Unable to update "+ book.getId()+" book not found", HttpStatus.NOT_FOUND);
	        }

	        bookRepository.updateBook(book);
	        return new ResponseEntity<Book>(book,HttpStatus.OK);
	    }

	    
	   
	    @PutMapping(value = "/{id}")
	    public ResponseEntity<?> updateBook(@RequestBody Book book,@PathVariable Integer id) {
	    	
	    	
	        if(bookRepository.getBookById(book.getId()) == null){
	            return new ResponseEntity<String>("Unable to update "+ book.getId()+" book not found", HttpStatus.NOT_FOUND);
	        }

	        bookRepository.updateBook(book);
	        return new ResponseEntity<Book>(book,HttpStatus.OK);
	    }
	    
	    
	    
	    
	    @DeleteMapping(value = "/{id}")
	    public ResponseEntity<?> deleteBook(@PathVariable("id") Integer id) {
	    	
	        if(bookRepository.getBookById(id) == null){
	            return new ResponseEntity<String>(" book not found" + id, HttpStatus.NOT_FOUND);
	        }

	        bookRepository.deleteBook(id);
	        return new ResponseEntity<Book>(HttpStatus.NO_CONTENT);
	    }


}
