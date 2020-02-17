package com.gem.service.impl;

import com.gem.mapper.CategoryMapper;
import com.gem.pojo.Category;
import com.gem.pojo.CategoryExample;
import com.gem.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryMapper categoryMapper;

    @Override
    public List<Category> list() {
        CategoryExample example =new CategoryExample();
        example.setOrderByClause("id desc");
        return categoryMapper.selectByExample(example);
    }

    @Override
    public void add(Category category) {
        categoryMapper.insert(category);
    }

    @Override
    public void delete(int id) {
        categoryMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Category get(int id) {
        return categoryMapper.selectByPrimaryKey(id);
    }

    @Override
    public void update(Category category) {
        categoryMapper.updateByPrimaryKeySelective(category);
    }
}
