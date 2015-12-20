package com.app.thoughtsharing.App;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by dewneot-pc on 12/10/2015.
 */
public class ThoughtSharingApp extends Application {

    private static ThoughtSharingApp Instance;
    public  static List<ParseObject> mParseObject;

    public static String getmUsernamae() {
        return mUsernamae;
    }

    public static void setmUsernamae(String mUsernamae) {
        ThoughtSharingApp.mUsernamae = mUsernamae;
    }

    public static String mUsernamae;
    public static String mType;

    public static String getmType() {
        return mType;
    }

    public static void setmType(String mType) {
        ThoughtSharingApp.mType = mType;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Instance=this;
        Parse.initialize(this);
        ParseUser.enableAutomaticUser();
        ParseFacebookUtils.initialize(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
    }
    public static ThoughtSharingApp getInstance()
    {
        return Instance;
    }

    public static List<ParseObject> getmParseObject() {
        return mParseObject;
    }

    public static void setmParseObject(List<ParseObject> mParseObject) {
        ThoughtSharingApp.mParseObject = mParseObject;
    }
}
