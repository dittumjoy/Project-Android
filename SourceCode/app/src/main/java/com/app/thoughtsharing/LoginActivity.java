package com.app.thoughtsharing;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.thoughtsharing.App.ThoughtSharingApp;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity  {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */

    //private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private  TextView mRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        // Set up the login form.
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);


        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        mRegister= (TextView) findViewById(R.id.register_refirect);
        mRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        LoginActivity.this,
                        RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        Button fb= (Button) findViewById(R.id.facebooklogin);
        fb.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseFacebookUtils.logInWithReadPermissionsInBackground(LoginActivity.this, Arrays.asList("email"), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException err) {
                        if (user == null) {
                            Log.d("hai", "Uh oh. The user cancelled the Facebook login.");
                        } else if (user.isNew()) {
                            getUserDetailsFromFB();
                            Log.d("hai", "User signed up and logged in through Facebook!");
                        } else {

                            Log.d("hai", "User logged in through Facebook!"+user.getUsername());
                            ThoughtSharingApp.getInstance().setmUsernamae(user.getUsername());
                            LogintoHomeActivity(user);
                        }
                    }
                });
            }
        });
        //showHashKey(this);
    }
    public static void showHashKey(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    "com.app.thoughtsharing", PackageManager.GET_SIGNATURES); //Your            package name here
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }
    private void getUserDetailsFromFB() {
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            Log.d("hai",object.getString("email")+object.getString("name"));
                        String  email =object.getString("email");
                        String name = object.getString("name");
                        saveNewUser(name,email);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email");
        request.setParameters(parameters);
        request.executeAsync();
       /* new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
           *//* handle the result *//*
                        try {
                           String  email = "";
                            String name = response.getJSONObject().getString("name");
                            saveNewUser(name,email);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();*/

    }

    private void saveNewUser( final String name,String email) {
        ParseUser parseUser = ParseUser.getCurrentUser();
        parseUser.setUsername(email);
        parseUser.setEmail(email);
        parseUser.put("firstName", name);
        parseUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Toast.makeText(LoginActivity.this, "New user:" + name + " Signed up, Please contact your admin to use the app", Toast.LENGTH_LONG).show();
            }
        });
//
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }



    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
       /* if (mAuthTask != null) {
            return;
        }*/

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);


           // Parse.enableLocalDatastore(this);

          /*  ParseObject testObject = new ParseObject("TestObject");
            testObject.put("foo", "bar");
            testObject.saveInBackground();*/

           // ParseUser parseuser=new ParseUser();
            ParseUser.logInInBackground(email, password,
                    new LogInCallback() {
                        public void done(final ParseUser user, ParseException e) {
                            // e.printStackTrace();
                            if (user != null) {
                                Log.d("hai",user.getUsername());
                                ThoughtSharingApp.getInstance().setmUsernamae(user.getUsername());

                               LogintoHomeActivity(user);
                                // If user exist and authenticated, send user to Welcome.class

                            } else {
                                Toast.makeText(
                                        getApplicationContext(),
                                        "No such user exist, please signup",
                                        Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }
                    });
        }
    }

    private void LogintoHomeActivity(final ParseUser user) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ApplicationUsers");
        query.whereEqualTo("Email", user.getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> mUsers, ParseException e) {
                if (e == null) {
                    if(mUsers.size()>0){
                        Log.d("hai", "Retrieved " + mUsers.size() + " users");
                        Log.d("hai", mUsers.get(0).getString("Type"));
                        ThoughtSharingApp.getInstance().setmType(mUsers.get(0).getString("Type"));
                        if(mUsers.get(0).getString("Type").equals("T")) {
                            ParseQuery<ParseObject> query = ParseQuery.getQuery("Groups");
                            query.whereEqualTo("Owner", user.getUsername());
                            query.findInBackground(new FindCallback<ParseObject>() {
                                public void done(List<ParseObject> Classes, ParseException e) {
                                    if (e == null) {
                                        Log.d("hai", "Retrieved " + Classes.size() + " classes");
                                        ThoughtSharingApp.getInstance().setmParseObject(Classes);
                                        Intent intent = new Intent(
                                                LoginActivity.this,
                                                HomeActivity.class);
                                        intent.putExtra("type", "teacher");
                                        startActivity(intent);
                                        Toast.makeText(getApplicationContext(),
                                                "Successfully Logged in",
                                                Toast.LENGTH_LONG).show();

                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(),
                                                "Please contact your Admin",
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),
                                    "Successfully Logged in",
                                    Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(
                                    LoginActivity.this,
                                    HomeActivity.class);
                            intent.putExtra("type", "student");
                            startActivity(intent);



                            finish();
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(),
                                "Please contact your Teacher to use the app",
                                Toast.LENGTH_LONG).show();
                        finish();
                    }

                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please contact your Teacher to use the app",
                            Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }




    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.



            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            //mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            //mAuthTask = null;
            showProgress(false);
        }
    }
}

