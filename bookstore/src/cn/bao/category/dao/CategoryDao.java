package cn.bao.category.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.bao.category.domain.Category;
import cn.itcast.jdbc.TxQueryRunner;

public class CategoryDao {
	private QueryRunner queryRunner = new TxQueryRunner();
	public List<Category> findAll() {
		String sql = "select * from category";
		try {
			return queryRunner.query(sql, new BeanListHandler<Category>(Category.class));
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
	public void add(Category category) {
		String sql ="insert into category values(?,?)";
		try {
			queryRunner.update(sql,category.getCid(),category.getCname());
		} catch (SQLException e) {
			throw new RuntimeException();
		}
		
	}
	public void delete(String cid) {
		String sql ="delete from category where cid=?";
		try {
			queryRunner.update(sql,cid);
		} catch (SQLException e) {
			throw new RuntimeException();
		}
		
	}
	public Category load(String cid) {
		String sql ="select * from category where cid=?";
		try {
			return queryRunner.query(sql, new BeanHandler<Category>(Category.class),cid);
		} catch (SQLException e) {
			throw new RuntimeException();
		}
		
		
		
	}
	public void eidt(Category category) {
		String sql = "update category set cname=? where cid =?";
		try {
			queryRunner.update(sql,category.getCname(),category.getCid());
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
	public String findCidByCname(String cname) {
		String sql = "select cid from category where cname=?";
		try {
			return (String) queryRunner.query(sql, new ScalarHandler(),cname);
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
}
