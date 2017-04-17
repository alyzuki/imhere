package com.here.zuki.imhere.Auth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.here.zuki.imhere.LoginActivity;
import com.here.zuki.imhere.MapActivity;
import com.here.zuki.imhere.Utils.SessionManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


/**
 * Created by zuki on 4/10/17.
 */

public class FacebookLoginAuth {

    private static final String TAG = ":::::FacebookLoginAuth";

    private Context pContext;
    private Activity pActivity;
    private  FirebaseUser user;

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    private CallbackManager mCallbackManager;
    private ProfileTracker mProfileTracker;
    private SessionManager sessionManager;
    private MapActivity.OurHandler handler;
    private Profile curProfile;

    public static FacebookLoginAuth instance = null;

    public FacebookLoginAuth(){}
    public FacebookLoginAuth(Context context, final Activity activity, MapActivity.OurHandler handler)
    {
        super();
        instance = this;
        sessionManager = SessionManager.getInstance();
        this.pContext = context;
        this.pActivity = activity;
        this.handler = handler;
        FirebaseApp.initializeApp(pContext);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    if(!user.getProviderData().isEmpty() && user.getProviderData().size() > 1)
                        updateProfile(user.getDisplayName(),user.getProviderData().get (1).getUid());
                    else
                        updateProfile(user.getDisplayName(),"none");
                    sessionManager.createLoginSession(user.getDisplayName(), LoginActivity.TAG_FACE);
                    if(LoginActivity.class == activity.getClass()) {
                        activity.finish();
                    }
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }

            }
        };
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "Login Success");
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "Login Cancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "Login Error");
            }
        });

        mAuth.addAuthStateListener(mAuthListener);
        showHashKey(pContext);

    }
    public static void showHashKey(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    "com.here.zuki.imhere", PackageManager.GET_SIGNATURES); //Your            package name here
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("+++++++KeyHash++++:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }


    private void handleFacebookAccessToken(AccessToken token) {
        final AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener( pActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(pContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        curProfile  = Profile.getCurrentProfile();
        if( null == curProfile) {
            mProfileTracker = new ProfileTracker() {
                @Override
                protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                    curProfile = profile2;
                    updateProfile(curProfile.getName(), curProfile.getId());
                    sessionManager.createLoginSession(user.getDisplayName(), LoginActivity.TAG_FACE);
                    mProfileTracker.stopTracking();

                }
            };

        }else
        {
            updateProfile(curProfile.getName(), curProfile.getId());
            sessionManager.createLoginSession(curProfile.getName(), LoginActivity.TAG_FACE);
        }
    }

    public void RemoveAuthStateListener()
    {
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public  FirebaseUser getCurLoginUser()
    {
        return this.user;
    }

    public void updateProfile(String name, String  id)
    {
        if(user != null)
        {
            Message msg = handler
                    .obtainMessage(
                            MapActivity.OurHandler.WHAT_PROFILE,
                            MapActivity.OurHandler.ARG_PROFILE_NAME,
                            name);
            handler.sendMessage(msg);
            msg = handler
                    .obtainMessage(
                            MapActivity.OurHandler.WHAT_PROFILE,
                            MapActivity.OurHandler.ARG_PROFILE_PICTURE,
                            "https://graph.facebook.com/" + id +"/picture?type=large");
            handler.sendMessage(msg);
        }
    }

    public  static  synchronized FacebookLoginAuth getInstance()
    {
        return instance;
    }

    public void signIn()
    {
        if(instance == null)
            return;
        LoginManager.getInstance().logInWithReadPermissions(pActivity, Arrays.asList("email", "public_profile", "user_friends"));
    }

    public void signOut()
    {
        if(instance == null)
            return;
        if(user != null)
        {
            mAuth.signOut();
        }
        if(AccessToken.getCurrentAccessToken() != null || Profile.getCurrentProfile() != null)
        {
            LoginManager.getInstance().logOut();
            AccessToken.setCurrentAccessToken(null);
        }
    }

    public void OnActivityResult(int requestCode, int resultCode, Intent data)
    {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void setActivity(Activity activity)
    {
        if(instance == null)
            return;
        this.pActivity = activity;
    }
}
