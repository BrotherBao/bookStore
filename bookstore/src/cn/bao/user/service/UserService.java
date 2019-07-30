package cn.bao.user.service;

import cn.bao.user.dao.UserDao;
import cn.bao.user.domain.User;

public class UserService {

	private UserDao userDao = new UserDao();
	
	/*
	 * 注册功能
	 */
	public void regist(User form) throws UserException {
		//校验用户名
		User user = userDao.findByUsername(form.getUsername());
		if(user != null) throw new UserException("用户名已被注册");
		//校验邮箱
		user = userDao.findByEmail(form.getEmail());
		if(user != null) throw new UserException("邮箱已被注册");
		//插入用户到数据库中
		userDao.add(form);
	}
	
	/*
	 * 激活功能
	 */
	public void active(String code) throws UserException {
		/*
		 * 通过code查询出用户
		 */
		User user = userDao.findByCode(code);
		/*
		 * 如果用户为null则抛出userException
		 */
		if(user==null) throw new UserException("激活码无效");
		/*
		 * 用户处于已激活状态，抛出userException
		 */
		if(user.getState()) throw new UserException("您已经激活了，请不要重复激活");
		/*
		 * 激活，更新用户uid匹配用户对状态码进行修改
		 */
		userDao.updateState(user.getUid(), true);
	}
	
	/*
	 * 登录功能
	 */
	public User login(User form) throws UserException {
		User user =userDao.findByUsername(form.getUsername());
		if(user == null) throw new UserException("用户名不存在");
		if(!user.getPassword().equals(form.getPassword())) throw new UserException("密码错误");
		if(!user.getState()) throw new UserException("用户尚未激活");
		
		return user;
	}
}
