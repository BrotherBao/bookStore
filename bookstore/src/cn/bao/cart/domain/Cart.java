package cn.bao.cart.domain;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class Cart {
	private Map<String, CartItem> map = new LinkedHashMap<String,CartItem>();
	/*
	 * 计算所有的总价格
	 */
	public double getTotal() {
		BigDecimal total = new BigDecimal("0");
		for(CartItem cartItem:map.values()) {
			BigDecimal subtotal = new BigDecimal(""+cartItem.getSubtotal());
			total = total.add(subtotal);
		}
		return total.doubleValue();
	}
	/*
	 * 添加条目到车中
	 */
	public void add(CartItem cartItem) {
		if(map.containsKey(cartItem.getBook().getBid())) {//判断原车中受否存在该条目
			CartItem _cartItem = map.get(cartItem.getBook().getBid());//通过图书id获取原条目
			_cartItem.setCount(_cartItem.getCount()+cartItem.getCount());//将原条目与现条目相加
			map.put(cartItem.getBook().getBid(), _cartItem);//将相加后的条目设置进去
		} else {
			map.put(cartItem.getBook().getBid(), cartItem);
		}
	}
	/*
	 * 清空所有条目
	 */
	public void clear() {
		map.clear();
	}
	/*
	 * 删除置顶条目
	 */
	public void delete(String bid) {
		map.remove(bid);
	}
	/*
	 * 获取所有条目
	 */
	public Collection<CartItem> getCartItems(){
		return map.values();
	}
	
}
