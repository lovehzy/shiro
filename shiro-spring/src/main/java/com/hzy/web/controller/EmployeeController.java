package com.hzy.web.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hzy.realm.PermissionName;

@Controller
@RequestMapping("/employee")
public class EmployeeController {


    @RequestMapping("")
    @RequiresPermissions("employee:list")
    @PermissionName("员工列表")
    public String index() throws  Exception{
        System.out.println("执行了员工列表....");
        return "employee";
    }

    @RequestMapping("/save")
    @RequiresPermissions("employee:save")
    @PermissionName("员工保存")
    public String save() throws  Exception{
        System.out.println("执行了员工保存....");
        return "employee";
    }

    @RequestMapping("/edit")
    @RequiresPermissions("employee:edit")
    @PermissionName("员工编辑")
    public String edit() throws  Exception{
        System.out.println("执行了员工编辑....");
        return "employee";
    }

    @RequestMapping("/delete")
    @PermissionName("员工删除")
    @RequiresPermissions("employee:delete")
    public String delete() throws  Exception{
        System.out.println("执行了员工删除....");
        return "employee";
    }
}
