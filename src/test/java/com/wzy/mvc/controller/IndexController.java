package com.wzy.mvc.controller;

import com.wzy.mvc.annotation.RequestMapping;
import com.wzy.mvc.http.RequestMethod;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping(path = "/index")
public class IndexController {

    @RequestMapping(path = "/test",method = RequestMethod.GET)
    public void test(String name){

    }

    @RequestMapping(path = "/test2", method = RequestMethod.POST)
    public void test2(String name2) {

    }

    public void test3(String name3) {

    }

}
