package com.gem.service.impl;

import com.gem.mapper.ReviewMapper;
import com.gem.pojo.Review;
import com.gem.pojo.ReviewExample;
import com.gem.pojo.User;
import com.gem.service.ReviewService;
import com.gem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    ReviewMapper reviewMapper;
    @Autowired
    UserService userService;

    @Override
    public void add(Review c) {
        reviewMapper.insert(c);
    }

    @Override
    public void delete(int id) {
        reviewMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Review c) {
        reviewMapper.updateByPrimaryKeySelective(c);
    }

    @Override
    public Review get(int id) {
        return reviewMapper.selectByPrimaryKey(id);
    }

    @Override
    public List list(int pid) {
        ReviewExample example = new ReviewExample();
        example.createCriteria().andPidEqualTo(pid);
        example.setOrderByClause("id desc");

        List<Review> rs = reviewMapper.selectByExample(example);
        setUser(rs);
        return  rs;
    }

    public void setUser(List<Review> reviews){
        for(Review review : reviews){
            setUser(review);}
    }
    public void setUser(Review review){
        int uid =review.getUid();
        User user = userService.get(uid);
        review.setUser(user);
    }
    @Override
    public int getCount(int pid) {
        return  list(pid).size();
    }
}
