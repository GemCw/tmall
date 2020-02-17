package com.gem.service;

import com.gem.pojo.Order;
import com.gem.pojo.OrderItem;

import java.util.List;

public interface OrderService {
    String waitPay = "waitPay";
    String waitDelivery = "waitDelivery";
    String waitConfirm = "waitConfirm";
    String waitReview = "waitReview";
    String finish = "finish";
    String delete = "delete";

    void add(Order c);
    float add(Order o, List<OrderItem>ois);
    void delete(int id);
    void update(Order c);
    Order get(int id);
    List list();
    List list(int uid, String Status);
}
