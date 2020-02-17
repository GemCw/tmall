package com.gem.controller;

import com.gem.pojo.Category;
import com.gem.service.CategoryService;
import com.gem.util.ImageUtil;
import com.gem.util.Page;
import com.gem.util.UploadedImageFile;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

@Controller
@RequestMapping("")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    /**获取分类信息，完成分页操作*/
    @RequestMapping("admin_category_list")
    public String list(Model model, Page page){
//        通过分页插件指定分页参数
        PageHelper.offsetPage(page.getStart(),page.getCount());
//        调用list() 获取对应分页的数据
        List<Category>  cs = categoryService.list();
//        通过PageInfo获取总数
        int total = (int) new PageInfo<>(cs).getTotal();
        page.setTotal(total);
        model.addAttribute("cs",cs);
        model.addAttribute("page",page);
        return "admin/listCategory";
    }

    /**添加新分类 **/
    @RequestMapping("admin_category_add")
    public String add(Category c, HttpSession session, UploadedImageFile uploadedImageFile) throws  Exception{
        categoryService.add(c);
        //imageFolder图片所在路径
        File imageFolder = new File(session.getServletContext().getRealPath("img/category"));
        File file = new File(imageFolder,c.getId()+".jpg");
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdirs();}
        uploadedImageFile.getImage().transferTo(file);
        BufferedImage img = ImageUtil.change2jpg(file);
        ImageIO.write(img,"jpg",file);
        return "redirect:/admin_category_list";
    }
    /**更新分类信息**/
    @RequestMapping("admin_category_update")
    public String update(Category c,HttpSession session,UploadedImageFile uploadedImageFile)throws Exception{
        categoryService.update(c);
        MultipartFile image = uploadedImageFile.getImage();
        if(null!=image&&!image.isEmpty()){
            //imageFolder图片所在路径
            File imageFolder = new File(session.getServletContext().getRealPath("img/category"));
            File file = new File(imageFolder,c.getId()+".jpg");
            image.transferTo(file);
            BufferedImage img = ImageUtil.change2jpg(file);
            ImageIO.write(img,"jpg",file);
        }
        return "redirect:/admin_category_list";
    }

    /**删除分类**/
    @RequestMapping("admin_category_delete")
    public String delete(int id,HttpSession session)  {
        categoryService.delete(id);
//      imageFolder图片所在路径
        File imageFolder = new File(session.getServletContext().getRealPath("img/category"));
        File file = new File(imageFolder,id+".jpg");
        file.delete();
        return  "redirect:/admin_category_list";
    }

    /**编辑分类**/
    @RequestMapping("admin_category_edit")
    public String edit(int id,Model model) {
        Category c = categoryService.get(id);
        model.addAttribute("c",c);
        return "admin/editCategory";
    }


}
