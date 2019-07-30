package cn.bao.cart.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.bao.book.domain.Book;
import cn.bao.book.service.BookService;
import cn.bao.cart.domain.Cart;
import cn.bao.cart.domain.CartItem;
import cn.itcast.servlet.BaseServlet;


@WebServlet("/CartServlet")
public class CartServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
	
	
	public String add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		 * 1、得到购物车
		 * 2、得到条目（得到图书和数量)
		 * 3、将条没有添加到车中
		 */
		Cart cart = (Cart) request.getSession().getAttribute("cart");
		
		String bid = request.getParameter("bid");
		Book book = new BookService().load(bid);
		int count = Integer.parseInt(request.getParameter("count"));
		CartItem cartItem = new CartItem();
		cartItem.setBook(book);
		cartItem.setCount(count);
		
		cart.add(cartItem);
		return "f:/jsps/cart/list.jsp";
	}
	
	public String clear(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Cart cart  = (Cart) request.getSession().getAttribute("cart");
		cart.clear();
		return "f:/jsps/cart/list.jsp";
	}
	
	public String delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Cart cart  = (Cart) request.getSession().getAttribute("cart");
		String bid = request.getParameter("bid");
		cart.delete(bid);
		return "f:/jsps/cart/list.jsp";
	}

}
