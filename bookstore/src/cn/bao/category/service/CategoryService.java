package cn.bao.category.service;

import java.util.List;

import cn.bao.book.dao.BookDao;
import cn.bao.category.dao.CategoryDao;
import cn.bao.category.domain.Category;
import cn.bao.order.domain.OrderException;

public class CategoryService {
	private CategoryDao categoryDao = new CategoryDao();
	private BookDao bookDao = new BookDao();

	public List<Category> findAll() {
		return categoryDao.findAll();
	}

	public void add(Category category) {
		categoryDao.add(category);
	}

	public void delete(String cid) throws OrderException {
		int count = bookDao.getCountByCid(cid);
		
		if(count>0) throw new OrderException("该分类下还有图书不能删除");
		
		 categoryDao.delete(cid);
	}

	public Category load(String cid) {
		return categoryDao.load(cid);
	}

	public void edit(Category category) {
		categoryDao.eidt(category);
	}
	
	public String findCidByCname(String cname) {
		return categoryDao.findCidByCname(cname);
	}
	
	
	

}
