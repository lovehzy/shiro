package com.hzy.dao;

import java.util.List;

import com.hzy.pojo.Permission;

public interface IPermissionDAO {

    /**
     * 保存权限对象
     * @param permission
     */
    void save(Permission permission);

    /**
     * 获取员工的权限表达式
     * @param userId
     * @return
     */
    List<String> getPermissionResourceByUserId(Long userId);


    List<String> getAllResources();
}
