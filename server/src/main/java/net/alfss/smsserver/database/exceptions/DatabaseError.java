package net.alfss.smsserver.database.exceptions;

/**
 * User: alfss
 * Date: 17.12.13
 * Time: 3:07
 */
public class DatabaseError extends RuntimeException {
    public DatabaseError(String message) {
        super(message);
    }

    public DatabaseError(Throwable e) {
        super(e);
    }

    public DatabaseError(String message, Throwable cause) {
        super(message, cause);
    }
}