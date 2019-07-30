package cn.bao.user.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import cn.bao.user.domain.User;
import cn.itcast.jdbc.TxQueryRunner;

public class UserDao {

	private QueryRunner queryRunner = new TxQueryRunner();
	
	/*
	 * 按用户名查询
	 */
	public User findByUsername(String username) {
		String sql ="select * from tb_user where username=?";
		try {
			return queryRunner.query(sql, new BeanHandler<User>(User.class),username);
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
	
	/*
	 * 按邮箱查询
	 */
	public User findByEmail(String email) {
		String sql = "select * from tb_user where email=?";
		try {
			return queryRunner.query(sql, new BeanHandler<User>(User.class),email);
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
	
	/*
	 * 插入用户
	 */
	public void add(User user) {
		try {
			String sql = "insert into tb_user values(?,?,?,?,?,?)";
			Object[] params = {user.getUid(), user.getUsername(), 
					user.getPassword(), user.getEmail(), user.getCode(),
					user.getState()};
			queryRunner.update(sql, params);
		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/*
	 * 按激活码查询
	 */
	public User findByCode(String code) {
		String sql ="select * from tb_user where code=?";
		try {
			return queryRunner.query(sql, new BeanHandler<User>(User.class),code);
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
	
	/*
	 * 修改指定用户的指定状态
	 */
	public void updateState(String uid,boolean state) {
		String sql = "update tb_user set state=? where uid=?";
		try {
			queryRunner.update(sql, state,uid);
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
	
}
