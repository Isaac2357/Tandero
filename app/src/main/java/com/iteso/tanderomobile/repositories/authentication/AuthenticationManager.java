package com.iteso.tanderomobile.repositories.authentication;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.iteso.tanderomobile.utils.Parameters;

/**Authentication manager, implements the Authentication interface.*/
public final class AuthenticationManager implements Authentication {
    /**The authentication manager instance to serve as a singleton.*/
    private static  AuthenticationManager authenticationManager;
    /**Firebase Auth variable.*/
    private FirebaseAuth mFirebaseAuth;
    /**Constructor for the authenticationManager class.*/
    private AuthenticationManager() {
        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    /**AuthenticationManager singleton instance creation.
     * @return the new or existing instance of the singleton.*/
    public static AuthenticationManager createInstance() {
        if (authenticationManager == null) {
            authenticationManager = new AuthenticationManager();
        }
        return authenticationManager;
    }
    /**Create account method.
     * @param email Email.
     * @param password password.
     * @return Task with AuthResult.*/
    @Override
    public Task<AuthResult> createAccount(
            final String email,
            final String password) {
        return mFirebaseAuth.createUserWithEmailAndPassword(email, password);
    }
    /**Sign in method.
     * @param email Email.
     * @param password password.
     * @return Task with AuthResult.*/
    @Override
    public Task<AuthResult> signIn(final String email, final String password) {
        return mFirebaseAuth.signInWithEmailAndPassword(email, password);
    }
    /**Sign out method.*/
    @Override
    public void signOut() {
        mFirebaseAuth.signOut();
    }
    /**Reauthenticate method.
     * @return Task.*/
    @Override
    public Task<Void> reauthenticateUser(final String email, final String password) {
        AuthCredential credential = EmailAuthProvider.getCredential(
                email,
                password
        );
        FirebaseUser user = getCurrentUser();
        if (user != null) {
            return user.reauthenticate(credential);
        }
        return null;
    }
    /**Delete user method.
     * @return Task.*/
    @Override
    public Task<Void> deleteUser() {
        FirebaseUser user = getCurrentUser();
        if (user != null) {
            return user.delete();
        }
        return null;
    }
    /**update User with new password method.
     * @param newPassword The new password.
     * @return Task.*/
    @Override
    public Task<Void> updateUserPassword(final String newPassword) {
        FirebaseUser user = getCurrentUser();
        if (user != null) {
           return user.updatePassword(newPassword);
        }
        return null;
    }
    /**Gets the current Firebase user.
     * @return current Firebase user.*/
    public FirebaseUser getCurrentUser() {
       return  mFirebaseAuth.getCurrentUser();
    }
}
