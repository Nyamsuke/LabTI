package util;

import model.User;

public final class Session {

    private static User user;

    private Session() {
    }

    public static void login(User u) {
        user = u;
    }

    public static User getUser() {
        return user;
    }

    public static void logout() {
        user = null;
    }
}
