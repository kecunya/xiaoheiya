package com.example.listviewtest;
/**
 * Created by FoolishFan on 2016/7/14.
 */

public class UserData {
    private String userName;
    private String userPwd;
    private int userId;
    public int pwdresetFlag=0;
    //获取用户名
    public String getUserName() {             //获取用户名
        return userName;
    }
    //设置用户名
    public void setUserName(String userName) {  //输入用户名
        this.userName = userName;
    }
    //获取用户密码
    public String getUserPwd() {                //获取用户密码
        return userPwd;
    }
    //设置用户密码
    public void setUserPwd(String userPwd) {     //输入用户密码
        this.userPwd = userPwd;
    }
    //获取用户id
    public int getUserId() {                   //获取用户ID号
        return userId;
    }
    //设置用户id
    public void setUserId(int userId) {       //设置用户ID号
        this.userId = userId;
    }

   /* public UserData(String userName, String userPwd, int userId) {    //用户信息
        super();
        this.userName = userName;
        this.userPwd = userPwd;
        this.userId = userId;
    }*/

    public UserData(String userName, String userPwd) {
        super();
        this.userName = userName;
        this.userPwd = userPwd;
    }

}
