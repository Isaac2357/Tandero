package com.iteso.tanderomobile.repositories.authentication;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public interface Authentication {
    Task<AuthResult> createAccount(String email, String password );
    Task<AuthResult> signIn(String email, String password );
    void signOut();
    Task<Void> reauthenticateUser();
    Task<Void> deleteUser();
    Task<Void> updateUserPassword(String newPassword);
}
