package cn.bao.book.service;

import java.util.List;

import cn.bao.book.dao.BookDao;
import cn.bao.book.domain.Book;

public class BookService {
	private BookDao bookDao = new BookDao();
	/*
	 * 查询所有图书
	 */
	public List<Book> findAll(){
		return bookDao.findAll();
	}
	/*
	 * 按分类查询图书
	 */
	public List<Book> findByCategory(String cid) {
		return bookDao.findByCategory(cid);
	}
	/*
	 * 按bid查找图书显示详细信息
	 */
	public Book load(String bid) {
		return bookDao.findByBid(bid);
	}
	public void add(Book book) {
		bookDao.add(book);
	}
}
