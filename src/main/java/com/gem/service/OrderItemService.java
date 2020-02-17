package com.gem.service;

import com.gem.pojo.Order;
import com.gem.pojo.OrderItem;

import java.util.List;

public interface OrderItemService {

    void add(OrderItem c);
    void delete(int id);
    void update(OrderItem c);
    OrderItem get(int id);
    List list();

    void fill(List<Order> os);

    void fill(Order o);
    /**根据产品id获取销量**/
    int getSaleCount(int pid);

    List<OrderItem> listByUser(int uid);
}
