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
import com.google.firebase.auth.UserInfo;
import com.here.zuki.imhere.LoginActivity;
import com.here.zuki.imhere.MapActivity;
import com.here.zuki.imhere.R;
import com.here.zuki.imhere.Utils.ApplicationContextProvider;
import com.here.zuki.imhere.Utils.SessionManager;
import com.here.zuki.imhere.Utils.SharedObject;

import java.util.Arrays;
import java.util.List;

/**
 * Created by zuki on 4/17/17.
 */

public class Authcred {

    private final static  String TAG = "---->AuthCred<-----";
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
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                loginUpdate(0);
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
        if(user == null)
            return;
        List<UserInfo> infos = (List<UserInfo>) user.getProviderData();
        if(!infos.isEmpty()  && infos.size() > 1)
        {
            String sType = infos.get(1).getProviderId();
            if(sType.equals(ApplicationContextProvider.getContext().getString(R.string.faceId)))
            {
                FacebookLoginAuth f = FacebookLoginAuth.getInstance();
                if(f != null)
                    f.signOut();
            }else if(sType.equals(ApplicationContextProvider.getContext().getString(R.string.googleId)))
            {
                GMailLoginAuth g = GMailLoginAuth.getInstance();
                if(g!= null)
                    g.signOut();
            }
        }
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
                            Log.d(TAG, task.getException().toString());
                            Toast.makeText(pContext, pContext.getText(R.string.authorfail) + "\n" + task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }else
                        {
                            Toast.makeText(pContext, "Login successful", Toast.LENGTH_SHORT).show();
                        }
                        loginUpdate(1);
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

    public void updateProfile(FirebaseUser user)
    {
        String sType = pContext.getString(R.string.nonStr);;
        String name = "";
        String id = sType;
        try{
            name = user.getDisplayName();
            List<UserInfo> userInfos = (List<UserInfo>) user.getProviderData();
            if(userInfos.size() > 1)
            {
                id = userInfos.get(1).getUid();
                sType = userInfos.get(1).getProviderId();
            }
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        try {
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
                            Arrays.asList(sType, id));
            pHandler.sendMessage(msg);
            SessionManager sessionManager = SessionManager.getInstance();
            sessionManager.createLoginSession(name, sType);
        }
        catch (Exception ex)
        {

        }
    }

    private void loginUpdate(int type)
    {
        user = mAuthor.getCurrentUser();
        updateProfile(user);
        if(user != null)
        {
            if(type == 0)
                Log.d(TAG, "User be already to use");
            else
                Log.d(TAG, "Login successfull");
            if(LoginActivity.class == pActivity.getClass()) {
                pActivity.finish();
            }
        }
        else
        {
            if(type == 0)
                Log.d(TAG, "User is signed out");
            else
                Log.d(TAG, "Login failed");
        }
        SharedObject.getInstance().setCurUser(user);
    }

}
