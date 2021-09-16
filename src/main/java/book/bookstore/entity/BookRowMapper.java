package book.bookstore.entity; 

import book.bookstore.entity.Book;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookRowMapper  implements RowMapper{

	
	  public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
	        Book book = new Book();
	        book.setId(rs.getInt("id"));
	        book.setTitle(rs.getString("title"));
	        book.setAuthor(rs.getString("author"));
	        book.setPrice(rs.getFloat("price"));
	     
	        return book;
	  }
}
