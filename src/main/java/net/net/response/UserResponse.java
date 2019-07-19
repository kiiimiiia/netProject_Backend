package net.net.response;

import net.net.entity.User;

public class UserResponse extends BaseResponse {
    private User user;

    public UserResponse(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
