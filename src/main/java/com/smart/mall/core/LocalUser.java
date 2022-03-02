package com.smart.mall.core;

import com.smart.mall.model.User;

public class LocalUser {
    private static ThreadLocal<User> threadLocal = new ThreadLocal<>();

    public static User getUser(){
        return LocalUser.threadLocal.get();
    }

    public static void setUser(User user){
        LocalUser.threadLocal.set(user);
    }

    public static void clear(){
        LocalUser.threadLocal.remove();
    }
}
