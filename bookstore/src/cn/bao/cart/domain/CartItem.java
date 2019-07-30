package cn.bao.cart.domain;

import java.math.BigDecimal;

import cn.bao.book.domain.Book;

public class CartItem {
	private Book book;//ͼ����Ʒ
	private int count;//ͼ������
	
	public double getSubtotal() {//С��,�����˶������������
		BigDecimal d1 = new BigDecimal(book.getPrice()+"");
		BigDecimal d2 = new BigDecimal(count+"");
		return d1.multiply(d2).doubleValue();
	}
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	@Override
	public String toString() {
		return "CartItem [book=" + book + ", count=" + count + "]";
	}
	
	
}
