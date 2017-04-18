package com.here.zuki.imhere.Auth;

import android.app.Activity;
import android.content.Context;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.here.zuki.imhere.LoginActivity;
import com.here.zuki.imhere.MapActivity;
import com.here.zuki.imhere.R;
import com.here.zuki.imhere.Utils.SessionManager;

/**
 * Created by zuki on 4/17/17.
 */

public class Authcred {

    private final static  String TAG = "AuthorCredential";
    private static Authcred instance;
    private MapActivity.OurHandler pHandler;
    //Author
    private FirebaseAuth mAuthor;
    //Author listener
    private FirebaseAuth.AuthStateListener mAuthListener;
    //User
    private FirebaseUser user;

    private Activity    pActivity;
    private Context     pContext;

    public Authcred(Context context, Activity activity, MapActivity.OurHandler handler)
    {
        super();
        user = null;
        pContext = context;
        pActivity = activity;
        pHandler = handler;
        mAuthor = FirebaseAuth.getInstance();
        final SessionManager sessionManager = SessionManager.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();

                if(user != null)
                {
                    //User is logined
                    Log.d(TAG, "User be already to use");
                    int iType;
                    String type = sessionManager.getLastLoginType();
                    if(type.equals(LoginActivity.TAG_FACE) )
                        iType = 1;
                    else if(type.equals(LoginActivity.TAG_GMAIL))
                        iType = 2;
                    else
                        return;
                    if(!user.getProviderData().isEmpty() && user.getProviderData().size() > 1)
                        updateProfile(user.getDisplayName(), iType, user.getProviderData().get (1).getUid());
                    else
                        updateProfile(user.getDisplayName(),iType, pContext.getString(R.string.nonStr));
                    sessionManager.createLoginSession(user.getDisplayName(), LoginActivity.TAG_FACE);
                    if(LoginActivity.class == pActivity.getClass()) {
                        pActivity.finish();
                    }

                }
                else
                {
                    Log.d(TAG, "User is signed out");
                }
            }
        };
        mAuthor.addAuthStateListener(mAuthListener);
        instance = this;
    }

    public void setNullUser()
    {
        user = null;
    }

    public void stop()
    {
        if(mAuthor != null && mAuthListener  != null)
        {
            mAuthor.removeAuthStateListener(mAuthListener);
        }
    }

    public void signOut()
    {
        mAuthor.signOut();
        user = null;
    }

    public static  synchronized Authcred getInstance()
    {
        return instance;
    }

    public void signInWithCredential(AuthCredential credential)
    {
        mAuthor.signInWithCredential(credential).addOnCompleteListener(pActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful())
                        {
                            Toast.makeText(pContext, pContext.getText(R.string.authorfail), Toast.LENGTH_SHORT).show();
                        }else
                        {
                            Toast.makeText(pContext, "Login successful", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    public void updateProfile(String name, int type, String  id)
    {
        Message msg = pHandler
                .obtainMessage(
                        MapActivity.OurHandler.WHAT_PROFILE,
                        MapActivity.OurHandler.ARG_PROFILE_NAME,
                        name);
        pHandler.sendMessage(msg);
        msg = pHandler
                .obtainMessage(
                        MapActivity.OurHandler.WHAT_PROFILE,
                        MapActivity.OurHandler.ARG_PROFILE_PICTURE,
                        type ,id);
        pHandler.sendMessage(msg);
    }

}
