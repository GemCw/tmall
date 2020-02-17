package com.gem.service;

import com.gem.pojo.Category;

import java.util.List;

public interface CategoryService {
//    int total();

    List<Category> list();

    void add(Category category);

    void delete(int id);

    Category get(int id);

    void update(Category category);
}
