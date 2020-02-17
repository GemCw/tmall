package com.gem.service.impl;

import com.gem.mapper.OrderMapper;
import com.gem.pojo.Order;
import com.gem.pojo.OrderExample;
import com.gem.pojo.OrderItem;
import com.gem.pojo.User;
import com.gem.service.OrderItemService;
import com.gem.service.OrderService;
import com.gem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderMapper orderMapper;
    @Autowired
    UserService userService;
    @Autowired
    OrderItemService orderItemService;

    @Override
    public void add(Order c) {
        orderMapper.insert(c);
    }
    /**生成订单**/
    @Override
    //避免回滚产生脏读???
    @Transactional(propagation= Propagation.REQUIRED,rollbackForClassName="Exception")
    public float add(Order o, List<OrderItem> ois) {
        float total = 0;
        add(o);

        if(false)
            throw new RuntimeException();

        for(OrderItem oi:ois){
            oi.setOid(o.getId());
            orderItemService.update(oi);
            total +=oi.getProduct().getPromotePrice()*oi.getNumber();
        }
    return total;
    }

    @Override
    public void delete(int id) {
        orderMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Order c) {
        orderMapper.updateByPrimaryKeySelective(c);
    }

    @Override
    public Order get(int id) {
        return orderMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Order> list() {
        OrderExample example = new OrderExample();
        example.setOrderByClause("id desc");
        List<Order> result = orderMapper.selectByExample(example);

        setUser(result);
        return result;
    }

    @Override
    public List list(int uid, String Status) {
        OrderExample example = new OrderExample();
        example.createCriteria().andUidEqualTo(uid).andStatusNotEqualTo(Status);
        example.setOrderByClause("id desc");
        return orderMapper.selectByExample(example);
    }

    public void setUser(List<Order> os){
        for(Order o : os){setUser(o);}
    }

    public void setUser(Order o){
        int uid  = o.getUid();
        User u = userService.get(uid);
        //把user信息写入order
        o.setUser(u);
    }

}
