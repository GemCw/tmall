package com.gem.util.Comparator;

import com.gem.pojo.Product;

import java.util.Comparator;

public class ProductPriceComparator implements Comparator<Product> {
    //升序
    @Override
    public int compare(Product p1, Product p2) {
        return (int) (p1.getPromotePrice()-p2.getPromotePrice());
    }
}
