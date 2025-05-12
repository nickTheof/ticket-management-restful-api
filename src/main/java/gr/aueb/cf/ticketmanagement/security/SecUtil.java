package gr.aueb.cf.ticketmanagement.security;

import org.mindrot.jbcrypt.BCrypt;

public class SecUtil {
    private SecUtil() {

    }

    public static String hashPassword(String plainPassword) {
        int workload = 12;
        String salt = BCrypt.gensalt(workload);
        return BCrypt.hashpw(plainPassword, salt);
    }

    public static Boolean checkPassword(String plainPassword, String storedPassword) {
        return BCrypt.checkpw(plainPassword, storedPassword);
    }
}
