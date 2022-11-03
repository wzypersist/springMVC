package com.wzy.mvc.controller;


import com.wzy.mvc.annotation.RequestMapping;
import com.wzy.mvc.http.RequestMethod;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
public class TestController {

    @RequestMapping(path = "/test4", method = RequestMethod.POST)
    public void test4(String name2) {

    }


}
