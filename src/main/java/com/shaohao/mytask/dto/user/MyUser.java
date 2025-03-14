package com.shaohao.mytask.dto.user;


import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MyUser {

    private static final ThreadLocal<User> currentUserInfo = new ThreadLocal<>();


    public static void put(User user) {
        currentUserInfo.set(user);
    }



    public static String getAddress() {
        MyUser.User user  = currentUserInfo.get();
        return null != user ? user.getAddress() : null;
    }

    public static void setStartTime(Long startTime) {
        MyUser.User user  = currentUserInfo.get();
        user.setStartTime(startTime);

    }

    public static Long getStartTime() {
        MyUser.User user  = currentUserInfo.get();
        return null != user ? user.getStartTime() : null;
    }

    public static void setLastTime(Long lastTime) {
        MyUser.User user  = currentUserInfo.get();
        user.setLastTime(lastTime);

    }

    public static Long getLastTime() {
        MyUser.User user  = currentUserInfo.get();
        return null != user ? user.getLastTime() : null;

    }

    public static void clear() {
        currentUserInfo.remove();
    }


    @Data
    public static class User {

        /**
         * 地址
         */
        private String address;

        /**
         * 最新交易时间
         */
        private Long lastTime;

        /**
         * 控制获取时间
         */
        private Long startTime;
    }


}
