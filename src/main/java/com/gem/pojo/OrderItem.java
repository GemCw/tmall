package com.gem.pojo;

/**
 * @author 39726
 */
/**订单项类*/
public class OrderItem {
    private Integer id;
    /**产品表id**/
    private Integer pid;
    /**订单表id**/
    private Integer oid;
    /**用户表id**/
    private Integer uid;
    /**购买数量**/
    private Integer number;

    /**非数据库字段**/
    private Product product;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getOid() {
        return oid;
    }

    public void setOid(Integer oid) {
        this.oid = oid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}