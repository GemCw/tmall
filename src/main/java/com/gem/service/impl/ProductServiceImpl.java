package com.gem.service.impl;

import com.gem.mapper.ProductMapper;
import com.gem.pojo.Category;
import com.gem.pojo.Product;
import com.gem.pojo.ProductExample;
import com.gem.pojo.ProductImage;
import com.gem.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductMapper productMapper;
    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductImageService productImageService;
    @Autowired
    OrderItemService orderItemService;
    @Autowired
    ReviewService reviewService;

    @Override
    public void add(Product p) {
        productMapper.insert(p);
    }

    @Override
    public void delete(int id) {
        productMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Product p) {
        productMapper.updateByPrimaryKeySelective(p);
    }

    @Override
    public Product get(int id) {
        Product p = productMapper.selectByPrimaryKey(id);
        setCategory(p);
        setFirstProductImage(p);
        return p;
    }
    public void setCategory(List<Product> ps){
        for(Product p:ps)
            setCategory(p);
    }
    public void setCategory(Product p){
        int cid = p.getCid();
        Category c =categoryService.get(cid);
        p.setCategory(c);
    }
    /**根据cid得到此cid下的产品**/
    @Override
    public List list(int cid) {
        ProductExample example = new ProductExample();
        example.createCriteria().andCidEqualTo(cid);
        example.setOrderByClause("id desc");
        List result = productMapper.selectByExample(example);
        setFirstProductImage(result);
        setCategory(result);
        return result;
    }

    /**为多个分类填充产品集合**/
    @Override
    public void fill(List<Category> cs) {
        for(Category c:cs){
            fill(c);
        }
    }

    /**为分类填充产品集合**/
    @Override
    public void fill(Category c) {
        List<Product> ps = list(c.getId());
        c.setProducts(ps);
    }

    /**为多个分类填充推荐产品集合，即把分类下的产品集合，按照8个为一行，拆成多行，以利于后续页面上进行显示**/
    @Override
    public void fillByRow(List<Category> cs) {
        int productNumberEachRow = 8;
        for(Category c :cs){
            List<Product> products = c.getProducts();
            ////取当前分类的所有产品，每8个产品为一行放入一个list<product>，再把所有的行list<product>,
            // 放入新的Arraylist，所以是list<list<product>>.第一次为null
            List<List<Product>> productsByRow = new ArrayList<>();
//            把products 按每8个装入 行list 的过程
            for(int i=0;i<products.size();i+=productNumberEachRow){
                //每次循环都是8个 8个地装入，第一次size=0+8，第二次size=8+8以此类推
                int size=i+productNumberEachRow;
                //每次8个8个地装，可能最后一次要装的产品不足8个，所以临界特殊处理。
                size = size>products.size()?products.size():size;
                //sublist代表把prodcts里的第i个和第 size-1 个之间产品装入productsOfEachRow集合中。
                // （第一次0至7，第二次8至15依此类推）
                List<Product> productsOfEachRow = products.subList(i,size);
                //把这一行的产品  装入先前定义好的 list，这样productsByRow里面就是按行隔离了。
                productsByRow.add(productsOfEachRow);
            }
            //把按行隔离的 productsByRow集合 传回 该 Category 对象中~
            c.setProductsByRow(productsByRow);
        }
    }

    @Override
    public void setSaleAndReviewNumber(List<Product> ps) {
        for(Product p :ps){
            setSaleAndReviewNumber(p);
        }
    }

    @Override
    public List<Product> search(String keyword) {
        ProductExample example = new ProductExample();
        example.createCriteria().andNameLike("%"+keyword+"%");
        example.setOrderByClause("id desc");
        List result = productMapper.selectByExample(example);
        setFirstProductImage(result);
        setCategory(result);
        return result;
    }

    @Override
    public void setSaleAndReviewNumber(Product p) {
        int saleCount = orderItemService.getSaleCount(p.getId());
        p.setSaleCount(saleCount);

        int reviewCount =  reviewService.getCount(p.getId());
        p.setReviewCount(reviewCount);
    }

    public void setFirstProductImage(List<Product> ps){
        for(Product p :ps){
            setFirstProductImage(p);
        }
    }
    @Override
    public void  setFirstProductImage(Product p) {
        List<ProductImage> pis = productImageService.list(p.getId(),ProductImageService.type_single);
        if(!pis.isEmpty()){
            ProductImage pi = pis.get(0);
            p.setFirstProductImage(pi);
        }
    }


}
