package com.gem.controller;

import com.gem.pojo.Category;
import com.gem.pojo.Property;
import com.gem.service.CategoryService;
import com.gem.service.PropertyService;
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
public class PropertyController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    PropertyService propertyService;

    @RequestMapping("admin_property_add")
    public String add(Property p){
        propertyService.add(p);
        return "redirect:admin_property_list?cid="+p.getCid();
    }

    @RequestMapping("admin_property_delete")
    public String delete(int id){
        Property p =propertyService.get(id);
        propertyService.delete(id);
        return "redirect:admin_property_list?cid="+ p.getCid();
    }
    @RequestMapping("admin_property_edit")
    public String edit(Model model, int id){
//        根据id获取Property对象
        Property p = propertyService.get(id);
//        根据properoty对象的cid属性获取Category对象
        Category c = categoryService.get(p.getCid());
        p.setCategory(c);
        model.addAttribute("p",p);
        return "admin/editProperty";
    }
    @RequestMapping("admin_property_update")
    public String update(Property p){
        propertyService.update(p);
        return "redirect:admin_property_list?cid="+p.getCid();
    }
    @RequestMapping("admin_property_list")
    public String list(int cid, Model model, Page page){
        Category c =categoryService.get(cid);
//        通过PageHelper设置分页参数
        PageHelper.offsetPage(page.getStart(),page.getCount());
        List<Property> ps = propertyService.list(cid);
//        通过PageInfo获取属性总数
        int total = (int)new PageInfo<>(ps).getTotal();
        page.setTotal(total);
        page.setParam("&cid="+c.getId());
        //ps-属性集合
        model.addAttribute("ps",ps);
        //c-分类对象
        model.addAttribute("c",c);
        //page-分页对象
        model.addAttribute("page",page);

        return "admin/listProperty";
    }



}
