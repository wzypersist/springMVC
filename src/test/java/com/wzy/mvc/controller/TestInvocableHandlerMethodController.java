package com.wzy.mvc.controller;

import com.wzy.mvc.annotation.RequestParam;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class TestInvocableHandlerMethodController {


    public String  testRequestAndResponse(HttpServletRequest request, HttpServletResponse response,@RequestParam(name = "name") String name) {
        Assert.notNull(request, "request can not null");
        Assert.notNull(response, "response can not null");

        try (PrintWriter writer = response.getWriter()) {
            name = request.getParameter("name");
            writer.println("Hello InvocableHandlerMethod, params:" + name);
            return name;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return name;
    }


    public String testViewName(Model model) {
        model.addAttribute("blogURL", "http://silently9527.cn");
        return "/silently9527.jsp";
    }


}
