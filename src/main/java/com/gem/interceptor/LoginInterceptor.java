package com.gem.interceptor;

import com.gem.pojo.User;
import com.gem.service.CategoryService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;

public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    CategoryService categoryService;
/**
 * 在业务处理器处理请求之前被调用
 * //true表示请求放行，false表示拦截请求，不再向下寻找处理器
 * 如果用户不存在，返回false
 *     从当前的拦截器往回执行所有拦截器的afterCompletion(),再退出拦截器链
 * 如果url开头不是fore,返回true
 *    执行下一个拦截器,直到所有的拦截器都执行完毕
 *    再执行被拦截的Controller
 *    然后进入拦截器链,
 *    从最后一个拦截器往回执行所有的postHandle()
 *    接着再从最后一个拦截器往回执行所有的afterCompletion()

    如果访问的地址是/fore开头
4.1 取出fore后面的字符串，比如是forecart,那么就取出cart
4.2 判断cart是否是在noNeedAuthPage
4.2 如果不在，那么就需要进行是否登录验证
4.3 从session中取出"user"对象
4.4 如果对象不存在，就客户端跳转到login.jsp
4.5 否则就正常执行
 */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler
                            ) throws Exception{
        HttpSession session = request.getSession();
        String contextPath = session.getServletContext().getContextPath();
        String[] noNeedAuthPage = new String[]{
                "home",
                "checkLogin",
                "register",
                "loginAjax",
                "login",
                "product",
                "category",
                "search"};
        String uri = request.getRequestURI();
        uri = StringUtils.remove(uri,contextPath);
        if(uri.startsWith("/fore")){
            String method = StringUtils.substringAfterLast(uri,"/fore");
            if(!Arrays.asList(noNeedAuthPage).contains(method)){
                User user = (User)session.getAttribute("user");
                if(null == user){
                    response.sendRedirect("loginPage");
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 在业务处理器处理请求执行完成后,生成视图之前执行的动作
     * 可在modelAndView中加入数据，比如当前时间
     */
    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView){
    }

    /**
     * 在DispatcherServlet完全处理完请求后被调用,可用于清理资源等
     *
     * 当有拦截器抛出异常时,会从当前拦截器往回执行所有的拦截器的afterCompletion()
     */
    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) {

    }






}
