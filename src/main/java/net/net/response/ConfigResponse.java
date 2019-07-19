package net.net.response;

import net.net.entity.Role;
import net.net.entity.User;

import java.util.List;

public class ConfigResponse extends BaseResponse {
    private List<User> users;
    private List<Role> roles;


    public ConfigResponse(List<User> users, List<Role> roles) {
        this.users = users;
        this.roles = roles;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
