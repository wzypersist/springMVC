package com.wzy.mvc.controller;

import com.wzy.mvc.annotation.RequestMapping;
import com.wzy.mvc.annotation.RequestParam;
import com.wzy.mvc.annotation.ResponseBody;
import com.wzy.mvc.http.RequestMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;


@Controller
@RequestMapping(path = "/test")
public class DispatcherController {

    @RequestMapping(path = "/dispatch", method = RequestMethod.GET)
    public String dispatch(@RequestParam(name = "name") String name, Model model) {
        System.out.println("DispatcherController.dispatch: name=>" + name);
        model.addAttribute("name", name);
        return "redirect:/silently9527.cn";
    }



}
