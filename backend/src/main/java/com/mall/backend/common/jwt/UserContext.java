package com.mall.backend.common.jwt;

public class UserContext {
    private static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();
    private static final ThreadLocal<String> USERNAME = new ThreadLocal<>();
    private static final ThreadLocal<Integer> ROLE = new ThreadLocal<>();

    public static void setUserId(Long id) {
        USER_ID.set(id);
    }

    public static void setUsername(String username) {
        USERNAME.set(username);
    }

    public static void setRole(Integer role) {
        ROLE.set(role);
    }

    public static Long getUserId() {
        return USER_ID.get();
    }

    public static String getUsername() {
        return USERNAME.get();
    }

    public static Integer getRole() {
        return ROLE.get();
    }

    public static boolean isAdmin() {
        return Integer.valueOf(1).equals(getRole());
    }

    public static void clear() {
        USER_ID.remove();
        USERNAME.remove();
        ROLE.remove();
    }
}
