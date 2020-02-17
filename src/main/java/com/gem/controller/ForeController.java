package com.gem.controller;

import com.gem.pojo.*;
import com.gem.service.*;
import com.gem.util.Comparator.ProductAllComparator;
import com.gem.util.Comparator.ProductDateComparator;
import com.gem.util.Comparator.ProductPriceComparator;
import com.gem.util.Comparator.ProductReviewComparator;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author 39726
 */
@Controller
@RequestMapping("")
public class ForeController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;
    @Autowired
    UserService userService;
    @Autowired
    ProductImageService productImageService;
    @Autowired
    PropertyValueService propertyValueService;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderItemService orderItemService;
    @Autowired
    ReviewService reviewService;


    @RequestMapping("forehome")
    public String home(Model model){
        List<Category> cs = categoryService.list();
//        为这些分类填充产品集合
        productService.fill(cs);
//        为这些分类填充推荐产品集合
        productService.fillByRow(cs);
        model.addAttribute("cs",cs);
        return "fore/home";
    }

    @RequestMapping("foreregister")
    public String register(Model model, User user){
        String name = user.getName();
        name = HtmlUtils.htmlEscape(name);
        boolean exist = userService.isExist(name);

        if(exist){
            String m = " 用户名已被使用，不能使用";
            model.addAttribute("msg",m);
            model.addAttribute("user",null);
            return "fore/register";
        }
        user.setName(name);
        userService.add(user);
        return "redirect:registerSuccessPage";
    }

    @RequestMapping("forelogin")
    public String login(@RequestParam("name") String name,
                        @RequestParam("password") String password,
                        Model model, HttpSession session){
        /**HTML编码转义**/
        name = HtmlUtils.htmlEscape(name);
        User user = userService.get(name,password);

        if(null == user){
            /**Model 的值是放在request里传递到jsp上去的
               注意request和session的区别**/
            model.addAttribute("msg","账号密码错误");
            return "fore/login";
        }
        session.setAttribute("user",user);
        return "redirect:forehome";
    }

    @RequestMapping("forelogout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return "redirect:forehome";
    }

    @RequestMapping("foreproduct")
    public String product(int pid,Model model){
        Product p =productService.get(pid);

        List<ProductImage> productSingleImages =
                productImageService.list(p.getId(),ProductImageService.type_single);
        List<ProductImage> productDetailImages =
                productImageService.list(p.getId(),ProductImageService.type_detail);
        p.setProductSingleImages(productSingleImages);
        p.setProductDetailImages(productDetailImages);
        //设置首图
        p.setFirstProductImage(productSingleImages.get(0));
        //获取产品的所有属性值
        List<PropertyValue> pvs = propertyValueService.list(pid);
        //获取产品对应的所有的评价
        List<Review> reviews =  reviewService.list(pid);
        //设置产品的销量和评价数量
        productService.setSaleAndReviewNumber(p);
        model.addAttribute("reviews",reviews);
        model.addAttribute("p",p);
        model.addAttribute("pvs",pvs);

        return "fore/product";
    }

    @RequestMapping("forecheckLogin")
    @ResponseBody//返回json数据
    public String checkLogin(HttpSession session){
        User user = (User) session.getAttribute("user");
        if(null!=user){
            return "success";
        }
            return "fail";
    }

    @RequestMapping("foreloginAjax")
    @ResponseBody
    public String loginAjax(@RequestParam("name") String name,@RequestParam("password") String password,HttpSession session){
        name = HtmlUtils.htmlEscape(name);
        User user = userService.get(name,password);

        if(null==user){
            return "fail";
        }
        session.setAttribute("user",user);
        return "success";
    }

    @RequestMapping("forecategory")
    public String category(int cid,String sort,Model model){
        Category c = categoryService.get(cid);
        //填充产品
        productService.fill(c);
        //填充销量和评价数据
        productService.setSaleAndReviewNumber(c.getProducts());

        if(null!=sort){
            switch (sort){
                case "review":
                    Collections.sort(c.getProducts(), new ProductReviewComparator());
                    break;
                case "date":
                    Collections.sort(c.getProducts(),new ProductDateComparator());
                    break;
                case "saleCount":
                    Collections.sort(c.getProducts(),new ProductPriceComparator());
                    break;
                case "price":
                    Collections.sort(c.getProducts(),new ProductPriceComparator());
                    break;
                case "all":
                    Collections.sort(c.getProducts(),new ProductAllComparator());
                    break;
            }
        }
        model.addAttribute("c",c);
        return "fore/category";
    }

    @RequestMapping("foresearch")
    public String search(String keyword,Model model){

        PageHelper.offsetPage(0,20);
        List<Product> ps = productService.search(keyword);
        productService.setSaleAndReviewNumber(ps);
        model.addAttribute("ps",ps);
        return "fore/searchResult";
    }
    /**立即购买***/
    @RequestMapping("forebuyone")
    public String buyone(int pid,int num,HttpSession session){
        int oiid = 0;
        User user = (User)session.getAttribute("user");
        boolean found = false;

        List<OrderItem> ois = orderItemService.listByUser(user.getId());
        for(OrderItem oi : ois){
            if(oi.getProduct().getId().intValue()==pid){
                oi.setNumber(oi.getNumber()+num);
                orderItemService.update(oi);
                found = true;
                oiid = oi.getId();
                break;
            }
        }
        if(!found){
            OrderItem oi = new OrderItem();
            oi.setUid(user.getId());
            oi.setNumber(num);
            oi.setPid(pid);
            orderItemService.add(oi);
            oiid = oi.getId();
        }
        //进入结算页面
        return "redirect:forebuy?oiid="+oiid;
    }
    /**结算页面**/
    @RequestMapping("forebuy")
    public String buy(Model model,String[] oiid ,HttpSession session){
        List<OrderItem> ois = new ArrayList<>();
        float total = 0;

        for(String strid : oiid){
            //字符串转整数
            int id = Integer.parseInt(strid);
            OrderItem oi = orderItemService.get(id);
            total += oi.getProduct().getPromotePrice()*oi.getNumber();
            ois.add(oi);
        }
        session.setAttribute("ois",ois);
        model.addAttribute("total",total);
        return "fore/buy";
    }

    /**加入购物车**/
    @RequestMapping("foreaddCart")
    @ResponseBody
    public String addCart(int pid,int num,Model model,HttpSession session){
        Product p =productService.get(pid);
        User user = (User)session.getAttribute("user");
        boolean found = false;
        List<OrderItem> ois = orderItemService.listByUser(user.getId());
        for(OrderItem oi:ois){
            if(oi.getProduct().getId().intValue()==pid){
                oi.setNumber(oi.getNumber()+num);
                orderItemService.update(oi);
                found=true;
                break;
            }
        }
        if(!found){
            OrderItem oi = new OrderItem();
            oi.setUid(user.getId());
            oi.setPid(pid);
            oi.setNumber(num);
            orderItemService.add(oi);
        }
        return "success";
    }

    /**查看购物车**/
    @RequestMapping("forecart")
    public String cart(Model model,HttpSession session){
        User user = (User)session.getAttribute("user");
        List<OrderItem> ois = orderItemService.listByUser(user.getId());
        model.addAttribute("ois",ois);
        return "fore/cart";
    }

    /**修改购物车商品数目**/
    @RequestMapping("forechangeOrderItem")
    public String changeOrderItem(HttpSession session,int pid,int number){
        User user = (User) session.getAttribute("user");
        //防止停留时间过久导致session失效，再次确认
        if(null==user){
            return "fail";}
        List<OrderItem> ois = orderItemService.listByUser(user.getId());
        for(OrderItem oi :ois){
            if(oi.getProduct().getId()==pid){
                oi.setNumber(number);
                orderItemService.update(oi);
                break;
            }
        }
        return "success";
    }

    /**删除购物车订单**/
    @RequestMapping("foredeleteOrderItem")
    public String deleteOrderItem(Model model,HttpSession session,int oiid){
        User user = (User) session.getAttribute("user");
        //防止停留时间过久导致session失效
        if(null == user){
            return "fail";}
        orderItemService.delete(oiid);
        return "success";
    }

    @RequestMapping("forecreateOrder")
    public String createOrder(Model model,Order order,HttpSession session){
        User user = (User) session.getAttribute("user");
        String orderCode = new SimpleDateFormat("yyyyMMddHHmmssSSS")
                .format(new Date())+RandomUtils.nextInt(10000);

        order.setUid(user.getId());
        order.setOrderCode(orderCode);
        order.setCreateDate(new Date());
        order.setStatus(OrderService.waitPay);

        List<OrderItem> ois = (List<OrderItem>) session.getAttribute("ois");
        float total = orderService.add(order,ois);
        return "redirect:forealipay?oid="+order.getId() +"&total="+total;
    }
    /**支付成功**/
    @RequestMapping("forepayed")
    public String payed(int oid,float total,Model model){
        Order order = orderService.get(oid);

        order.setStatus(OrderService.waitDelivery);
        order.setPayDate(new Date());
        order.setTotal(total);//????

        orderService.update(order);
        model.addAttribute("o",order);
        return "fore/payed";
    }
    /**查看订单页**/
    @RequestMapping("forebought")
    public String bought(Model model,HttpSession session) {
        User user = (User) session.getAttribute("user");
        List<Order> os = orderService.list(user.getId(), OrderService.delete);
        orderItemService.fill(os);

        model.addAttribute("os", os);
        return "fore/bought";
    }

    @RequestMapping("foreconfirmReceive")
    public String confirmReceive(Model model,int oid){
        Order o = orderService.get(oid);
        orderItemService.fill(o);
        model.addAttribute("o",o);
        return "fore/confirmReceive";
    }

    @RequestMapping("foreorderConfirmed")
    public String orderConfirmed(Model model,int oid){
        Order o = orderService.get(oid);
        o.setConfirmDate(new Date());
        o.setStatus(OrderService.waitReview);
        orderService.update(o);
        return "fore/orderConfirmed";
    }

    @RequestMapping("foredeleteOrder")
    public String deleteOrder(int oid){
        Order o = orderService.get(oid);
        o.setStatus(OrderService.delete);
        orderService.update(o);
        return "success";
    }

    /**获取评价页面**/
    @RequestMapping("forereview")
    public String review(Model model,int oid){
        Order o = orderService.get(oid);
        orderItemService.fill(o);
        Product p = o.getOrderItems().get(0).getProduct();
        List<Review> reviews = reviewService.list(p.getId());
        productService.setSaleAndReviewNumber(p);
        model.addAttribute("p",p);
        model.addAttribute("o",o);
        model.addAttribute("reviews",reviews);
        return "fore/review";
    }
    /**进行评价**/
    @RequestMapping("foredoreview")
    public String doreview(Model model, HttpSession session,int oid,int pid,String content){
        Order o =orderService.get(oid);
        o.setStatus(OrderService.finish);
        orderService.update(o);

        content = HtmlUtils.htmlEscape(content);
        User user = (User) session.getAttribute("user");

        Review review = new Review();
        review.setContent(content);
        review.setPid(pid);
        review.setUid(user.getId());
        review.setContent(content);
        reviewService.add(review);

        return "redirect:forereview?oid="+oid+"&showonly=true";
    }

}
