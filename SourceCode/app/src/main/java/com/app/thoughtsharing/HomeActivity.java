package com.app.thoughtsharing;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.thoughtsharing.App.ThoughtSharingApp;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    public List<ParseObject> mParseobject;
    String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mParseobject= ThoughtSharingApp.getInstance().getmParseObject();
        Intent mIntent=getIntent();
        if(mIntent!=null)
        {
            type=getIntent().getStringExtra("type");
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),type);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if(type.equals("student"))
        {
            fab.setVisibility(View.GONE);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

    }

    public void signIn()
    {

        final Dialog dialog = new Dialog(HomeActivity.this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.addclass);
        dialog.setTitle("Login");

        // get the Refferences of views
        final EditText editTextUserName=(EditText)dialog.findViewById(R.id.editTextUserNameToLogin);
        final  EditText editTextPassword=(EditText)dialog.findViewById(R.id.editTextPasswordToLogin);

        Button btnSignIn=(Button)dialog.findViewById(R.id.buttonSignIn);

        // Set On ClickListener
        btnSignIn.setOnClickListener(new View.OnClickListener() {

            public void onClick(final View v) {
                // TODO Auto-generated method stub

                // get The User name and Password
                String userName=editTextUserName.getText().toString();
                String password=editTextPassword.getText().toString();

                // fetch the Password form database for respective user name


                // check if the Stored password matches with  Password entered by user
                if(!password.equals("") && userName.equals(""))
                {
                    Snackbar.make(v, "Please enter values", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();


                }
                else
                {
                    final ParseObject po = new ParseObject("Groups");
                    po.put("GroupName", userName);
                    po.put("GroupDescription", password);
                    po.put("Owner", ThoughtSharingApp.getInstance().getmUsernamae());

                    //po.saveInBackground();
                    // Replaced above call with one below so objectId can be saved for future calls.
                    po.saveInBackground(new SaveCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                // Saved successfully.
                                HomeActivity.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(HomeActivity.this, "Added successfully", Toast.LENGTH_SHORT)
                                               .show();
                                        dialog.dismiss();
                                        BusProvider.getInstance().post("added");
                                    }
                                });

                            } else {
                                e.printStackTrace();
                            }
                        }
                    });

                    //Toast.makeText(HomeActivity.this, "Please enter valid names", Toast.LENGTH_LONG).show();
                }

            }
        });


        dialog.show();



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {

            ParseUser.logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    Toast.makeText(HomeActivity.this,
                            "Successfully Logged out",
                            Toast.LENGTH_LONG).show();
                    finish();
                }
            });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_home, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        String mType;
        public SectionsPagerAdapter(FragmentManager fm, String type) {
            super(fm);
            mType=type;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if(mType.equals("teacher")) {
                if (position == 0) {
                    return MyClassesFragment.newInstance("title", "des");
                } else {
                    return AllclassesFragment.newInstance("title", "des");
                }
            }
            else
            {
                return AllclassesFragment.newInstance("title", "des");
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            if(mType.equals("teacher"))
            {
                return 2;
            }
            else
            {
                return 1;
            }

        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(mType.equals("teacher"))
            {
                switch (position) {

                    case 0:
                        return "MY CLASSES";
                    case 1:
                        return "ALL CLASSES";

                }
                return null;
            }
            else
            {
                switch (position) {

                    case 0:
                        return "ALL CLASSES";

                }
                return null;
            }

        }
    }
}
