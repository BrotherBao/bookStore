package cn.bao.book.adminservlet;

import cn.bao.book.service.BookService;
import cn.bao.category.service.CategoryService;
import cn.itcast.servlet.BaseServlet;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/AdminBookServlet")
public class AdminBookServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
	
	private BookService bookService = new BookService();
	private CategoryService categoryService = new CategoryService();
	
	public String findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("bookList", bookService.findAll());
		return "f:/adminjsps/admin/book/list.jsp";
	}
	
	public String load(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("categoryList", categoryService.findAll());
		request.setAttribute("book", bookService.load(request.getParameter("bid")));
		return "f:/adminjsps/admin/book/desc.jsp";
	}
	
	public String addPre(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("categoryList", categoryService.findAll());
		return "f:/adminjsps/admin/book/add.jsp";
	}
}
