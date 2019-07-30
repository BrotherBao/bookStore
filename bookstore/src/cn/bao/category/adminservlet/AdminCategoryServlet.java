package cn.bao.category.adminservlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.bao.category.domain.Category;
import cn.bao.category.service.CategoryService;
import cn.bao.order.domain.OrderException;
import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;


@WebServlet("/AdminCategoryServlet")
public class AdminCategoryServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
	
	CategoryService categoryService  = new CategoryService();
	
	
	public String findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("categoryList", categoryService.findAll());
		return "f:adminjsps/admin/category/list.jsp";
	}
  
	public String add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Category category = CommonUtils.toBean(request.getParameterMap(), Category.class);
		category.setCid(CommonUtils.uuid());
		categoryService.add(category);
		return findAll(request, response);
	}
	
	public String delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cid = request.getParameter("cid");
		try {
			categoryService.delete(cid);
			return findAll(request, response);
		} catch (OrderException e) {
			request.setAttribute("msg", e.getMessage());
			return "f:/adminjsps/msg.jsp";
		}
	}
	
	public String eidtPre(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cid = request.getParameter("cid");
		request.setAttribute("category", categoryService.load(cid));
		return "f:/adminjsps/admin/category/mod.jsp";
	}
	
	public String eidt(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Category category = CommonUtils.toBean(request.getParameterMap(), Category.class);
		categoryService.edit(category);
		return findAll(request, response);
	}
}
