package com.wzy.mvc.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class UserVo {
    private String name;
    private Integer age;
    private Date birthday;


}
