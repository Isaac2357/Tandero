package com.iteso.tanderomobile.repositories.authentication;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
/**Authentication interface for Firebase.*/
public interface Authentication {
    /**Create account method.
     * @param email Email.
     * @param password password.
     * @return Task with AuthResult.*/
    Task<AuthResult> createAccount(String email, String password);
    /**Sign in method.
     * @param email Email.
     * @param password password.
     * @return Task with AuthResult.*/
    Task<AuthResult> signIn(String email, String password);
    /**Sign out method.*/
    void signOut();
    /**Reauthenticate method.
     * @param email user email
     * @param password user password
     * @return Task.*/
    Task<Void> reauthenticateUser(String email, String password);
    /**Delete user method.
     * @return Task.*/
    Task<Void> deleteUser();
    /**update User with new password method.
     * @param newPassword The new password.
     * @return Task.*/
    Task<Void> updateUserPassword(String newPassword);
}
