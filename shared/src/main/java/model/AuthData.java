package model;

/**
 * A record containing authorization data
 * @param authToken a unique authorization token
 * @param username a username that corresponds to the authToken
 */
record AuthData(String authToken, String username) {}