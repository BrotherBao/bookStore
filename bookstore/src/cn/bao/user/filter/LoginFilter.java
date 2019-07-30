package cn.bao.user.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import cn.bao.user.domain.User;

/**
 * Servlet Filter implementation class LoginFilter
 */
@WebFilter(
		urlPatterns = { 
				"/jsps/cart/*", 
				"/jsps/order/*"
		}, 
		servletNames = { 
				"CartServlet", 
				"OrderServlet"
		})
public class LoginFilter implements Filter {

 
	public void destroy() {
		
	}

	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		User user = (User) httpServletRequest.getSession().getAttribute("session_user");
		if(user != null) chain.doFilter(request, response);
		else {
			httpServletRequest.setAttribute("msg", "Äú»¹Ã»µÇÂ¼");
			httpServletRequest.getRequestDispatcher("jsps/user/login.jsp").forward(httpServletRequest, response);
		}
	}

	public void init(FilterConfig fConfig) throws ServletException {
	}

}