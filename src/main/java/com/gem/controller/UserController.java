package com.gem.controller;

import com.gem.mapper.UserMapper;
import com.gem.pojo.User;
import com.gem.service.UserService;
import com.gem.util.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author 39726
 */
@Controller
@RequestMapping("")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    UserMapper userMapper;

    @RequestMapping("admin_user_list")
    public String list(Model model, Page page){
//       PageHelper通过一个拦截器,会给你紧接着执行的方法拦截下来加上分页代码.然后再查询数据库.
        PageHelper.offsetPage(page.getStart(),page.getCount());

        List<User> users = userService.list();

        int total = (int) new PageInfo<>(users).getTotal();
        page.setTotal(total);

        model.addAttribute("us",users);
        model.addAttribute("page",page);

        return "admin/listUser";
    }

}
