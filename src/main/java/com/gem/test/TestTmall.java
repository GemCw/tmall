package com.gem.test;

import org.apache.commons.lang.StringUtils;

public class TestTmall {
 
    public static void main(String args[]) {

        String uri = StringUtils.substringAfterLast("abcdedf","cd");
        System.out.println(uri);

    }
 
}