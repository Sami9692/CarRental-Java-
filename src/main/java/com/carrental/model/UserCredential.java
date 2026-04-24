package com.carrental.model;

public class UserCredential {
    private int credentialId;
    private int userId;
    private String username;
    private String passwordHash;

    public UserCredential() {}
    public UserCredential(int credentialId, int userId, String username, String passwordHash) {
        this.credentialId = credentialId;
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
    }

    public int getCredentialId()            { return credentialId; }
    public void setCredentialId(int v)      { this.credentialId = v; }
    public int getUserId()                  { return userId; }
    public void setUserId(int v)            { this.userId = v; }
    public String getUsername()             { return username; }
    public void setUsername(String v)       { this.username = v; }
    public String getPasswordHash()         { return passwordHash; }
    public void setPasswordHash(String v)   { this.passwordHash = v; }
}
