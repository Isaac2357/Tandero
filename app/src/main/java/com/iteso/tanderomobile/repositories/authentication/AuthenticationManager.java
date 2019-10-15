package com.iteso.tanderomobile.repositories.authentication;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.iteso.tanderomobile.utils.Parameters;

public class AuthenticationManager implements Authentication{
    private static  AuthenticationManager authenticationManager;
    private FirebaseAuth _firebaseAuth;
    private AuthenticationManager() {
        _firebaseAuth = FirebaseAuth.getInstance();
    }

    public static AuthenticationManager createInstance() {
        if (authenticationManager == null) {
            authenticationManager = new AuthenticationManager();
        }
        return authenticationManager;
    }

    @Override
    public Task<AuthResult> createAccount(String email, String password ) {
        return _firebaseAuth.createUserWithEmailAndPassword(email, password);
    }

    @Override
    public Task<AuthResult> signIn(String email, String password) {
        return _firebaseAuth.signInWithEmailAndPassword(email, password);
    }

    @Override
    public void signOut() {
        _firebaseAuth.signOut();
    }

    @Override
    public Task<Void> reauthenticateUser() {
        AuthCredential credential = EmailAuthProvider.getCredential(
                Parameters.CURRENT_USER_EMAIL,
                Parameters.CURRENT_USER_PASSWORD
        );
        FirebaseUser user = getCurrentUser();
        if (user != null) {
            return user.reauthenticate(credential);
        }
        return null;
    }

    @Override
    public Task<Void> deleteUser() {
        FirebaseUser user = getCurrentUser();
        if (user != null) {
            return user.delete();
        }
        return null;
    }

    @Override
    public Task<Void> updateUserPassword(String newPassword) {
        FirebaseUser user = getCurrentUser();
        if (user != null) {
           return user.updatePassword(newPassword);
        }
        return null;
    }

    public FirebaseUser getCurrentUser() {
       return  _firebaseAuth.getCurrentUser();
    }
}
