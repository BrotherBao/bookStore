package cn.bao.user.service;

import cn.bao.user.dao.UserDao;
import cn.bao.user.domain.User;

public class UserService {

	private UserDao userDao = new UserDao();
	
	/*
	 * ע�Ṧ��
	 */
	public void regist(User form) throws UserException {
		//У���û���
		User user = userDao.findByUsername(form.getUsername());
		if(user != null) throw new UserException("�û����ѱ�ע��");
		//У������
		user = userDao.findByEmail(form.getEmail());
		if(user != null) throw new UserException("�����ѱ�ע��");
		//�����û������ݿ���
		userDao.add(form);
	}
	
	/*
	 * �����
	 */
	public void active(String code) throws UserException {
		/*
		 * ͨ��code��ѯ���û�
		 */
		User user = userDao.findByCode(code);
		/*
		 * ����û�Ϊnull���׳�userException
		 */
		if(user==null) throw new UserException("��������Ч");
		/*
		 * �û������Ѽ���״̬���׳�userException
		 */
		if(user.getState()) throw new UserException("���Ѿ������ˣ��벻Ҫ�ظ�����");
		/*
		 * ��������û�uidƥ���û���״̬������޸�
		 */
		userDao.updateState(user.getUid(), true);
	}
	
	/*
	 * ��¼����
	 */
	public User login(User form) throws UserException {
		User user =userDao.findByUsername(form.getUsername());
		if(user == null) throw new UserException("�û���������");
		if(!user.getPassword().equals(form.getPassword())) throw new UserException("�������");
		if(!user.getState()) throw new UserException("�û���δ����");
		
		return user;
	}
}
