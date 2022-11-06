package com.wzy.mvc.controller;

import com.wzy.mvc.annotation.ResponseBody;
import com.wzy.mvc.handler.view.View;
import com.wzy.mvc.vo.UserVo;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TestReturnValueController {

    @ResponseBody
    public UserVo testResponseBody() {
        UserVo userVo = new UserVo();
        userVo.setBirthday(new Date());
        userVo.setAge(20);
        userVo.setName("Silently9527");
        return userVo;
    }

    @ResponseBody
    public Map<String,Object> testResponseBody1(){
        HashMap<String, Object> map = new HashMap<>();
        UserVo userVo = new UserVo();
        userVo.setBirthday(new Date());
        userVo.setAge(20);
        userVo.setName("Silently9527");
        map.put("res",userVo);
        return map;
    }

    public String testViewName() {
        return "/jsp/index.jsp";
    }

    public View testView() {
        return null;
    }

    public Model testModel(Model model) {
        model.addAttribute("testModel", "Silently9527");
        return model;
    }

    public Map<String, Object> testMap() {
        Map<String, Object> params = new HashMap<>();
        params.put("testMap", "Silently9527");
        return params;
    }
}
