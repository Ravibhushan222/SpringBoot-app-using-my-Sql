package book.bookstore.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;

import java.sql.Types;

import book.bookstore.entity.Book;
import book.bookstore.entity.BookRowMapper;

@Repository
public class BookRepositoryImpl implements BookRepository{


    @Autowired
    
    JdbcTemplate jdbcTemplate;

    @Override
    public Boolean addBook(Book book) {
       String sql = "INSERT INTO books VALUES (?,?,?,?)";
        return jdbcTemplate.execute(sql, new PreparedStatementCallback<Boolean>() {
           @Override
           public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
              ps.setInt(1,book.getId());
              ps.setString(2,book.getTitle());
               ps.setString(3,book.getAuthor());
               ps.setFloat(4,book.getPrice());
              
               return ps.execute();
           }
       });

    }

    @Override
    public List<Book> getAllBooks() {
        return jdbcTemplate.query("SELECT * FROM books",new BookRowMapper());
    }

    @Override
    @Cacheable(value = "book", key = "#id")
    public Book getBookById(int id) {
        String sql = "SELECT * FROM books WHERE id = ?";
        try{
            return (Book) this.jdbcTemplate.queryForObject(sql,new Object[] {id},new BookRowMapper());
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    @Override
    public List<Book> getBookByAuthor(String author) {
        return jdbcTemplate.query("Select * from books WHERE author  = ? ",new BookRowMapper(),author);
    
    	
    }

    @Override
    @CachePut(value = "books", key = "#book.id")
    public Integer updateBook(Book book) {
        String query = "UPDATE books SET title = ? , author = ? ,price = ? WHERE id = ?";
        Object[] params = {book.getTitle(),book.getAuthor(),book.getPrice(),book.getId()};
        int[] types = {Types.VARCHAR,Types.VARCHAR,Types.FLOAT,Types.INTEGER};
        return jdbcTemplate.update(query,params,types);
    }

    @Override
    @CacheEvict(value="book",key = "#id")
    public Integer deleteBook(Integer id) {
        return jdbcTemplate.update("DELETE FROM books WHERE id = ?",id);
    }
}