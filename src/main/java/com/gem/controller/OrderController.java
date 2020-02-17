package com.gem.controller;

import com.gem.pojo.Order;
import com.gem.service.OrderItemService;
import com.gem.service.OrderService;
import com.gem.util.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("")
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    OrderItemService orderItemService;

    @RequestMapping("admin_order_list")
    public String list(Model model, Page page){
 //     PageHelper通过一个拦截器,会给你紧接着执行的方法拦截下来加上分页代码.然后再查询数据库.
        PageHelper.offsetPage(page.getStart(),page.getCount());

        List<Order> os = orderService.list();

        int total = (int)new PageInfo<>(os).getTotal();
        page.setTotal(total);
//        借助orderItemService.fill()方法为这些订单填充上orderItems信息
        orderItemService.fill(os);

        model.addAttribute("os",os);
        model.addAttribute("page",page);
        return "admin/listOrder";
    }

    /**发货**/
    @RequestMapping("admin_order_delivery")
    public String delivery(Order o){
        o.setStatus(orderService.waitConfirm);
        orderService.update(o);
        return "redirect:admin_order_list";
    }

}
