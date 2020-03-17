package com.newland.spring.request.interceptor;

import java.util.List;

/**
 * 用户信息-线程上下文
 *
 * @author WRP
 * @since 2019/11/5
 */
public class SessionInfo {

    private String userId;

    private String userName;

    private String userPhone;

    private String deptId;

    private String deptName;

    private Boolean efficiency;

    public List<String> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<String> roleList) {
        this.roleList = roleList;
    }

    private List<String> roleList;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Boolean getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(Boolean efficiency) {
        this.efficiency = efficiency;
    }
}
