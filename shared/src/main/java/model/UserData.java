package model;

/**
 * A record containing information about the user
 * @param username the user's username
 * @param password the user's password corresponding to the username
 * @param email the user's email
 */
public record UserData(String username, String password, String email) {}
