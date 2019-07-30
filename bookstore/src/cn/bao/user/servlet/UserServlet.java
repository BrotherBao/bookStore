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
     * ע�Ṧ��
     */
    public String regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	/*
    	 * 1����װ�����ݵ�form������
    	 * 2����ȫ��uid��code
    	 * 3������У��
    	 *   >���������Ϣ��form��request���У�ת����regist.jsp
    	 * 4������service�������ע��
    	 *   >���������Ϣ��form��request���У�ת����regist.jsp
    	 * 5�����ʼ�
    	 * 6������ɹ���Ϣת����regist.jsp
    	 */
    	//һ����װ
    	User form = CommonUtils.toBean(request.getParameterMap(), User.class);
    	//��ȫ
    	form.setUid(CommonUtils.uuid());
    	form.setCode(CommonUtils.uuid()+CommonUtils.uuid());
    	form.setState(false);
    	/*
    	 * ����У��
    	 * 1������һ��Map��������װ������Ϣ������keyΪ���ֶ����ƣ�ֵΪ������Ϣ
    	 */
    	Map<String, String> errors = new HashMap<String,String>();
    	/*
    	 * 2����ȡform�е�username��password��email
    	 */
    	String username = form.getUsername();
    	if(username.length()<3||username.length()>10) {
    		errors.put("username", "�û���������3~10֮��");
    	} else if(username == null||username.trim()==null) {
    		errors.put("username", "�û�������Ϊ��");
    	}
    	
    	String password = form.getPassword();
    	if(password.length()<3||password.length()>10) {
    		errors.put("password", "���������3~10֮��");
    	} else if(password == null||password.trim()==null) {
    		errors.put("password", "���벻��Ϊ��");
    	}
    	
    	String email = form.getEmail();
    	if(email == null||email.trim() == null) {
    		errors.put("email", "���䲻��Ϊ��");
    	} else if(!email.matches("\\w+@\\w+\\.\\w+")) {
    		errors.put("email", "�����ʽ����");
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
    	 * ���ʼ�
    	 * ׼�������ļ�
    	 */
    	//��ȡ�����ļ���Ϣ
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
    		MailUtils.send(session, mail);//���ʼ���
		} catch (MessagingException e) {
			e.printStackTrace();
		}
    	
		request.setAttribute("msg", "ע��ɹ�");
		return "f:/jsps/msg.jsp";
    }
    
    /*
     * �����
     */
    public String active(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String code = request.getParameter("code");
    	try {
			userService.active(code);
			request.setAttribute("msg", "����ɹ������¼");
		} catch (UserException e) {
			request.setAttribute("msg", e.getMessage());
		}
    	return "f:/jsps/msg.jsp";
    }
    
    /*
     * ��¼����
     */
    public String login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	/*
    	 * 1����װ������
    	 * 2������У��
    	 * 3������service������ɵ�¼
    	 *   >���������Ϣ��form��request���У�ת����login.jsp
    	 * 4�������û���Ϣ��session�У�Ȼ���ض���index.jsp
    	 */
    	User form = CommonUtils.toBean(request.getParameterMap(), User.class);
    	Map<String, String> errors = new HashMap<String,String>();
    	String username = form.getUsername();
    	if(username.length()<3||username.length()>10) {
    		errors.put("username", "�û���������3~10֮��");
    	} else if(username == null||username.trim()==null) {
    		errors.put("username", "�û�������Ϊ��");
    	}
    	
    	String password = form.getPassword();
    	if(password.length()<3||password.length()>10) {
    		errors.put("password", "���������3~10֮��");
    	} else if(password == null||password.trim()==null) {
    		errors.put("password", "���벻��Ϊ��");
    	}
    	
    	try {
			User user = userService.login(form);
			request.getSession().setAttribute("session_user", user);
			/*
			 * ���û����һ�����ﳵ������session�б���һ��Cart����
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
     * �˳�
     */
    public String quit(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
    	request.getSession().invalidate();
    	return "r:/index.jsp";
    }
    
}









