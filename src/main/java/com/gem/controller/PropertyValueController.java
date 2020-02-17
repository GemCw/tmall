package com.gem.controller;

import com.gem.pojo.Product;
import com.gem.pojo.PropertyValue;
import com.gem.service.ProductService;
import com.gem.service.PropertyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("")
public class PropertyValueController {
    @Autowired
    PropertyValueService propertyValueService;
    @Autowired
    ProductService productService;

    @RequestMapping("admin_propertyValue_edit")
    public String edit(Model model, int pid){
        //通过产品id获取
        Product p = productService.get(pid);
        //因为在第一次访问的时候，这些属性值是不存在的，需要进行初始化。
        propertyValueService.init(p);
        List<PropertyValue> pvs = propertyValueService.list(p.getId());

        model.addAttribute("p",p);
        model.addAttribute("pvs",pvs);
        return "admin/editPropertyValue";
    }

    @RequestMapping("admin_propertyValue_update")
    public String update(PropertyValue pv){
        propertyValueService.update(pv);
        return "success";
    }
}
