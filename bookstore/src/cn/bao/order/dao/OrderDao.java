package cn.bao.order.dao;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.bao.book.domain.Book;
import cn.bao.order.domain.Order;
import cn.bao.order.domain.OrderItem;
import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;

public class OrderDao {

	private QueryRunner queryRunner = new TxQueryRunner();
	
	public void addOrder(Order order) {
		String sql ="insert into orders values(?,?,?,?,?,?)";
		Timestamp timestamp = new Timestamp(order.getOrdertime().getTime());
		Object[] params = {order.getOid(),timestamp,order.getTotal(),order.getState(),
				order.getOwner().getUid(),order.getAddress()};
		try {
			queryRunner.update(sql, params);
		} catch (SQLException e) {
			throw new RuntimeException();
		}
		
	}
	
	public void addOrderItemList(List<OrderItem> orderItemList) {
		String sql ="insert into orderitem values(?,?,?,?,?)";
		Object[][] params = new Object[orderItemList.size()][];
		
		for(int i=0;i<orderItemList.size();i++) {
			OrderItem item = orderItemList.get(i);
			params[i] = new Object[] {
					item.getIid(),item.getCount(),item.getSubtotal(),
					item.getOrder().getOid(),item.getBook().getBid()};
		}
		
		try {
			queryRunner.batch(sql, params);
		} catch (SQLException e) {
			throw new RuntimeException();		
		}
	}

	public List<Order> findByUid(String uid) {
		String sql ="select * from orders where uid=?";
		try {
			List<Order> orderList = queryRunner.query(sql, new BeanListHandler<Order>(Order.class),uid);
			for(Order order:orderList) {
				loadOrderItems(order);
			}
			return orderList;
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}

	private void loadOrderItems(Order order) {
		String sql = "select * from orderitem i, book b where i.bid=b.bid and oid=?";
		try {
			List<Map<String, Object>> mapList = queryRunner.query(sql, new MapListHandler(),order.getOid());
			List<OrderItem> orderItemList = toOrderItemList(mapList);
			order.setOrderItemList(orderItemList);
		} catch (SQLException e) {
			throw new RuntimeException();		}
	}

	private List<OrderItem> toOrderItemList(List<Map<String, Object>> mapList) {
		List<OrderItem> orderItemList = new ArrayList<OrderItem>();
		for(Map<String, Object> map:mapList) {
			OrderItem item = toOrderItem(map);
			orderItemList.add(item);
		}
		return orderItemList;
	}

	private OrderItem toOrderItem(Map<String, Object> map) {
		OrderItem orderItem = CommonUtils.toBean(map, OrderItem.class);
		Book book = CommonUtils.toBean(map, Book.class);
		orderItem.setBook(book);
		return orderItem;
	}

	public Order load(String oid) {
		String sql ="select * from orders where oid=?";
		try {
			Order order= queryRunner.query(sql, new BeanHandler<Order>(Order.class),oid);
			loadOrderItems(order);
			return order;
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
	
	public int getStateByOid(String oid) {
		String sql = "select state from orders where oid=?";
		try {
			return (Integer) queryRunner.query(sql, new ScalarHandler(),oid);
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
	
	public void updateState(String oid,int state) {
		String sql = "update orders set state=? where oid=?";
		try {
			queryRunner.update(sql,state,oid);
		} catch (SQLException e) {
			throw new RuntimeException();
		}
		
	}
}
