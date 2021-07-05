package com.xxx.crm.query;

import com.xxx.crm.base.BaseQuery;

public class RoleQuery  extends BaseQuery {
    private String RoleName;

    public RoleQuery() {
    }

    public RoleQuery(String roleName) {
        RoleName = roleName;
    }

    public String getRoleName() {
        return RoleName;
    }

    public void setRoleName(String roleName) {
        RoleName = roleName;
    }

    @Override
    public String toString() {
        return "RoleQuery{" +
                "RoleName='" + RoleName + '\'' +
                '}';
    }
}
