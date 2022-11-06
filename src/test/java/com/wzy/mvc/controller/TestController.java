package com.wzy.mvc.controller;


import com.wzy.mvc.annotation.RequestBody;
import com.wzy.mvc.annotation.RequestMapping;
import com.wzy.mvc.annotation.RequestParam;
import com.wzy.mvc.http.RequestMethod;
import com.wzy.mvc.vo.UserVo;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
public class TestController {

    @RequestMapping(path = "/test4", method = RequestMethod.POST)
    public void test4(@RequestParam(name = "name") String name,
                      @RequestParam(name = "age") Integer age,
                      @RequestParam(name = "birthday") Date birthday,
                      HttpServletRequest request) {
    }

    @RequestMapping(path = "/user", method = RequestMethod.POST)
    public void user(@RequestBody UserVo userVo) {
    }
}
