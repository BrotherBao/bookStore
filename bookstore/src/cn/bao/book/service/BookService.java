package cn.bao.book.service;

import java.util.List;

import cn.bao.book.dao.BookDao;
import cn.bao.book.domain.Book;

public class BookService {
	private BookDao bookDao = new BookDao();
	/*
	 * ��ѯ����ͼ��
	 */
	public List<Book> findAll(){
		return bookDao.findAll();
	}
	/*
	 * �������ѯͼ��
	 */
	public List<Book> findByCategory(String cid) {
		return bookDao.findByCategory(cid);
	}
	/*
	 * ��bid����ͼ����ʾ��ϸ��Ϣ
	 */
	public Book load(String bid) {
		return bookDao.findByBid(bid);
	}
	public void add(Book book) {
		bookDao.add(book);
	}
}
