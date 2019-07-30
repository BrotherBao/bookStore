package cn.bao.book.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.bao.book.domain.Book;
import cn.bao.category.domain.Category;
import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;

public class BookDao {
	private QueryRunner queryRunner = new TxQueryRunner();
	/*
	 * 查询所有图书
	 */
	public List<Book> findAll(){
		String sql = "select * from book";
		try {
			return queryRunner.query(sql, new BeanListHandler<Book>(Book.class));
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
	
	/*
	 * 按分类查询
	 */
	public List<Book> findByCategory(String cid) {
		String sql = "select * from book where cid=?";
		try {
			return queryRunner.query(sql, new BeanListHandler<Book>(Book.class),cid);
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
	/*
	 * 按bid查询
	 */

	public Book findByBid(String bid) {
		String sql = "select * from book where bid=?";
		try {
			Map<String, Object> map = queryRunner.query(sql, new MapHandler(),bid);
			Category category = CommonUtils.toBean(map, Category.class);
			Book book = CommonUtils.toBean(map, Book.class);
			book.setCategory(category);
			return book;
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}

	public int getCountByCid(String cid) {
		String sql = "select count(*) from book where cid=?";
		try {
			Number cnt = (Number)queryRunner.query(sql, new ScalarHandler(), cid);
			return cnt.intValue();
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}

	public void add(Book book) {
		String sql = "insert into book values(?,?,?,?,?,?)";
		Object[] params = {book.getBid(), book.getBname(), book.getPrice(),
				book.getAuthor(), book.getImage(), book.getCategory().getCid()};
		try {
			queryRunner.update(sql, params);
		} catch(SQLException e) {
			System.out.println(book.getCategory().getCid());
			throw new RuntimeException(e);
		}
	}

}
