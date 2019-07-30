package cn.bao.user.servlet;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.bao.cart.domain.Cart;
import cn.bao.user.domain.User;
import cn.bao.user.service.UserException;
import cn.bao.user.service.UserService;
import cn.itcast.commons.CommonUtils;
import cn.itcast.mail.Mail;
import cn.itcast.mail.MailUtils;
import cn.itcast.servlet.BaseServlet;


@WebServlet("/UserServlet")
public class UserServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
       
    private UserService userService = new UserService();
   
    /*
     * 注册功能
     */
    public String regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	/*
    	 * 1、封装表单数据到form对象中
    	 * 2、不全：uid、code
    	 * 3、输入校验
    	 *   >保存错误信息、form到request域中，转发到regist.jsp
    	 * 4、调用service方法完成注册
    	 *   >保存错误信息、form到request域中，转发到regist.jsp
    	 * 5、发邮件
    	 * 6、保存成功信息转发到regist.jsp
    	 */
    	//一键封装
    	User form = CommonUtils.toBean(request.getParameterMap(), User.class);
    	//补全
    	form.setUid(CommonUtils.uuid());
    	form.setCode(CommonUtils.uuid()+CommonUtils.uuid());
    	form.setState(false);
    	/*
    	 * 输入校验
    	 * 1、创建一个Map，用来封装错误信息，其中key为表单字段名称，值为错误信息
    	 */
    	Map<String, String> errors = new HashMap<String,String>();
    	/*
    	 * 2、获取form中的username、password、email
    	 */
    	String username = form.getUsername();
    	if(username.length()<3||username.length()>10) {
    		errors.put("username", "用户名必须在3~10之间");
    	} else if(username == null||username.trim()==null) {
    		errors.put("username", "用户名不能为空");
    	}
    	
    	String password = form.getPassword();
    	if(password.length()<3||password.length()>10) {
    		errors.put("password", "密码必须在3~10之间");
    	} else if(password == null||password.trim()==null) {
    		errors.put("password", "密码不能为空");
    	}
    	
    	String email = form.getEmail();
    	if(email == null||email.trim() == null) {
    		errors.put("email", "邮箱不能为空");
    	} else if(!email.matches("\\w+@\\w+\\.\\w+")) {
    		errors.put("email", "邮箱格式不对");
    	}
    	
    	if(errors.size()>0) {
    		request.setAttribute("errors", errors);
    		request.setAttribute("form", form);
    		return "f:/jsps/user/regist.jsp";
    	}
    	
    	
    	try {
			userService.regist(form);
		} catch (UserException e) {
			request.setAttribute("msg", e.getMessage());
			request.setAttribute("form", form);
			return "f:/jsps/user/regist.jsp";
		}
    	/*
    	 * 发邮件
    	 * 准备配置文件
    	 */
    	//获取配置文件信息
    	Properties properties = new Properties();
    	properties.load(this.getClass().getClassLoader()
				.getResourceAsStream("email_template.properties"));
    	String host = properties.getProperty("host");
    	String uname = properties.getProperty("uname");
    	String pwd = properties.getProperty("pwd");
    	String from = properties.getProperty("from");
    	String to = form.getEmail();
    	String subject = properties.getProperty("subject");
    	String content = properties.getProperty("content");
    	content = MessageFormat.format(content, form.getCode());
    	Session session = MailUtils.createSession(host, uname, pwd);
    	Mail mail = new Mail(from, to, subject, content);
    	try {
    		MailUtils.send(session, mail);//发邮件！
		} catch (MessagingException e) {
			e.printStackTrace();
		}
    	
		request.setAttribute("msg", "注册成功");
		return "f:/jsps/msg.jsp";
    }
    
    /*
     * 激活功能
     */
    public String active(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String code = request.getParameter("code");
    	try {
			userService.active(code);
			request.setAttribute("msg", "激活成功，请登录");
		} catch (UserException e) {
			request.setAttribute("msg", e.getMessage());
		}
    	return "f:/jsps/msg.jsp";
    }
    
    /*
     * 登录功能
     */
    public String login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	/*
    	 * 1、封装表单数据
    	 * 2、输入校验
    	 * 3、调用service方法完成登录
    	 *   >保存错误信息、form到request域中，转发到login.jsp
    	 * 4、保存用户信息到session中，然后重定向到index.jsp
    	 */
    	User form = CommonUtils.toBean(request.getParameterMap(), User.class);
    	Map<String, String> errors = new HashMap<String,String>();
    	String username = form.getUsername();
    	if(username.length()<3||username.length()>10) {
    		errors.put("username", "用户名必须在3~10之间");
    	} else if(username == null||username.trim()==null) {
    		errors.put("username", "用户名不能为空");
    	}
    	
    	String password = form.getPassword();
    	if(password.length()<3||password.length()>10) {
    		errors.put("password", "密码必须在3~10之间");
    	} else if(password == null||password.trim()==null) {
    		errors.put("password", "密码不能为空");
    	}
    	
    	try {
			User user = userService.login(form);
			request.getSession().setAttribute("session_user", user);
			/*
			 * 给用户添加一个购物车，即向session中保存一个Cart对象
			 */
			request.getSession().setAttribute("cart", new Cart());
			return "r:/index.jsp";
		} catch (UserException e) {
			request.setAttribute("msg",e.getMessage());
			request.setAttribute("form", form);
			return "f:/jsps/user/login.jsp";
		}
    }
    
    /*
     * 退出
     */
    public String quit(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
    	request.getSession().invalidate();
    	return "r:/index.jsp";
    }
    
}









