package com.gem.util;

import org.springframework.web.multipart.MultipartFile;

public class UploadedImageFile {

    /**这里的属性名称image必须和 listCategory.jsp 页面中的增加分类部分中的type="file"的name值保持一致。**/
    MultipartFile image;

    public MultipartFile getImage(){
        return image;
    }
    public void setImage(MultipartFile image){
        this.image=image;
    }


}
