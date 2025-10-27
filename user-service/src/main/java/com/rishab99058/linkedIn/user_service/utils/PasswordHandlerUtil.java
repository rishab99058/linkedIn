package com.rishab99058.linkedIn.user_service.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHandlerUtil {

    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean checkPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}
