package com.here.zuki.imhere.Auth;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.here.zuki.imhere.R;
import com.here.zuki.imhere.Utils.SessionManager;

/**
 * Created by zuki on 4/10/17.
 */

public class GMailLoginAuth implements
        GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = ":::::GmailLoginAuth";
    private Context pContext;
    private Activity pActivity;
    private static final int RC_SIGN_IN = 9001;
    GoogleSignInAccount acct = null;


    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;
    private SessionManager sessionManager = null;
    private Authcred mAuthor = Authcred.getInstance();

    public GMailLoginAuth(Context context, Activity activity)
    {
        super();
        this.pContext = context;
        this.pActivity = activity;
        sessionManager = SessionManager.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.EMAIL))
                .requestIdToken(pContext.getResources().getString(R.string.server_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(pContext)
                .enableAutoManage((FragmentActivity) pActivity /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();

    }

    public void signIn()
    {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        pActivity.startActivityForResult(signInIntent, RC_SIGN_IN);
        showProgressDialog();

    }

    public void signOut() {
        mAuthor.signOut();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        acct = null;
                    }
                });
        mGoogleApiClient.disconnect();
    }
    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        //----->updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        acct = null;
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            acct = result.getSignInAccount();
            AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
            if(mAuthor != null)
                mAuthor.signInWithCredential(credential);
        } else {
            acct = null;
        }
    }

    public void OnActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(pContext);
            mProgressDialog.setMessage(pContext.getString(R.string.CommonLoading));
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }
}
