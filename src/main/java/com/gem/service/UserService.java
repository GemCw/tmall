package com.gem.service;

import com.gem.pojo.User;

import java.util.List;

public interface UserService {
    void add(User u);
    void delete(int id);
    void update(User u);
    User get(int id);
    List list();

    boolean isExist(String name);

    User get(String name ,String password);
}
