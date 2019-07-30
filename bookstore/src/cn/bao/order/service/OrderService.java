package cn.bao.order.service;

import java.sql.SQLException;
import java.util.List;

import cn.bao.order.dao.OrderDao;
import cn.bao.order.domain.Order;
import cn.bao.order.domain.OrderException;
import cn.itcast.jdbc.JdbcUtils;

public class OrderService {

	private OrderDao orderDao = new OrderDao();
	
	public void add(Order order) {
		try {
			//开启事务
			JdbcUtils.beginTransaction();
			
			orderDao.addOrder(order);
			orderDao.addOrderItemList(order.getOrderItemList());
			
			//关闭事务
			JdbcUtils.commitTransaction();
		} catch (Exception e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {
			}
			throw new RuntimeException();
		}
	}

	public List<Order> myOrders(String uid) {
		
		return orderDao.findByUid(uid);
	}

	public Order load(String oid) {
		return orderDao.load(oid);
	}
	
	public void confirm(String oid) throws OrderException{
		int state = orderDao.getStateByOid(oid);
		if(state!=3) throw new OrderException("订单确认失败！");
		
		orderDao.updateState(oid, 4);
	}
	
	public void zhiFu(String oid) {
		int state = orderDao.getStateByOid(oid);
		if(state == 1) {
			orderDao.updateState(oid, 2);
		}
	}
}
