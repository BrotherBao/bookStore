package cn.bao.book.servlet;

import cn.bao.book.service.BookService;
import cn.itcast.servlet.BaseServlet;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/BookServlet")
public class BookServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
    
	private BookService bookService = new BookService();
	
	/*
	 * 查询所有图书
	 */
	public String findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("bookList", bookService.findAll());
		return "f:/jsps/book/list.jsp";
	}
	/*
	 * 按分类查询图书
	 */
	public String findByCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cid = request.getParameter("cid");
		request.setAttribute("bookList", bookService.findByCategory(cid));
		return "f:/jsps/book/list.jsp";
	}
	/*
	 * 按bid查询图书来加载图书详细内容
	 */
	public String load(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("book", bookService.load(request.getParameter("bid")));
		return "f:/jsps/book/desc.jsp";
	}

}
