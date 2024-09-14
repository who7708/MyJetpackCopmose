package com.example.fe.myapplication.staffcard;


public class LoginRequest {

    private String username;

    private String password;

    /**
     * {@link com.mtl.fhl.cyy.acs.fe.domain.enums.AcLoginType}
     */
    private String type;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
