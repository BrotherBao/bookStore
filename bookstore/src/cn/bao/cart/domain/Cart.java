package cn.bao.cart.domain;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class Cart {
	private Map<String, CartItem> map = new LinkedHashMap<String,CartItem>();
	/*
	 * �������е��ܼ۸�
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
	 * �����Ŀ������
	 */
	public void add(CartItem cartItem) {
		if(map.containsKey(cartItem.getBook().getBid())) {//�ж�ԭ�����ܷ���ڸ���Ŀ
			CartItem _cartItem = map.get(cartItem.getBook().getBid());//ͨ��ͼ��id��ȡԭ��Ŀ
			_cartItem.setCount(_cartItem.getCount()+cartItem.getCount());//��ԭ��Ŀ������Ŀ���
			map.put(cartItem.getBook().getBid(), _cartItem);//����Ӻ����Ŀ���ý�ȥ
		} else {
			map.put(cartItem.getBook().getBid(), cartItem);
		}
	}
	/*
	 * ���������Ŀ
	 */
	public void clear() {
		map.clear();
	}
	/*
	 * ɾ���ö���Ŀ
	 */
	public void delete(String bid) {
		map.remove(bid);
	}
	/*
	 * ��ȡ������Ŀ
	 */
	public Collection<CartItem> getCartItems(){
		return map.values();
	}
	
}
