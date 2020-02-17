package com.gem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
/**用来做服务端跳转用**/
@Controller
@RequestMapping("")
public class PageController {

    @RequestMapping("registerPage")
    public String registerPage(){
        return "fore/register";
    }

    @RequestMapping("registerSuccessPage")
    public String registerSuccessPage(){
        return "fore/registerSuccess";
    }

    @RequestMapping("loginPage")
    public String loginPage() {
        return "fore/login";
    }

    @RequestMapping("forealipay")
    public String alipay(){
        return "fore/alipay";
    }

}
