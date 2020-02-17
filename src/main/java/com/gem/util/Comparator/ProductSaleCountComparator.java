package com.gem.util.Comparator;

import com.gem.pojo.Product;

import java.util.Comparator;

public class ProductSaleCountComparator implements Comparator<Product> {

    @Override
    public int compare(Product p1, Product p2) {
        //降序
        return p2.getSaleCount()-p1.getSaleCount();
    }
}
