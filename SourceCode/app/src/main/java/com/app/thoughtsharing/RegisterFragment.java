package com.app.thoughtsharing;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class RegisterFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @Nullable
    @Bind(R.id.edit_text__name) EditText mName;
    @Nullable
    @Bind(R.id.edit_text__email) EditText mEmail;
    @Nullable
    @Bind(R.id.edit_text__phone) EditText mPhone;
    @Nullable
    @Bind(R.id.edit_text__passsword) EditText mPassword;
    @Nullable
    @Bind(R.id.submit_register) Button Register;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String selectedId=null;


    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_register, container, false);

        ButterKnife.bind(this, view);

       // _combineLatestEvents();

        return view;
    }
    @OnClick(R.id.submit_register)
    public void submit()
    {
        boolean numValid = (mPhone.getText().toString()!=null) && !mPhone.getText().toString().isEmpty() && mPhone.getText().toString().toLowerCase().equals("teacher") || mPhone.getText().toString().toLowerCase().equals("student");

        if (!numValid) {
            mPhone.setError("Invalid type!");
        }

        boolean emailValid = (mEmail.getText().toString()!=null) && !(mEmail.getText().toString().trim().isEmpty())&& Utils.isValidEmail(mEmail.getText().toString().trim());

        if (!emailValid) {
            mEmail.setError("Invalid Email!");
        }
        else
        {
            mEmail.setError(null);
        }

        boolean nameValid = (mName.getText().toString()!=null) && !mName.getText().toString().isEmpty();
        if (!nameValid) {
            mName.setError("Please Enter Name");
        }
        boolean passwordValid = (mPassword.getText().toString()!=null) && !mPassword.getText().toString().isEmpty();
        if (!nameValid) {
            mPassword.setError("Please Enter Password");
        }
        if(emailValid && numValid && nameValid && passwordValid) {
            //mPref.setUserDetails( mName.getText().toString(), mEmail.getText().toString().toLowerCase(),mPhone.getText().toString(),mLocation.getText().toString());
            ParseUser user = new ParseUser();
            user.setUsername(mEmail.getText().toString());
            user.setPassword(mPassword.getText().toString());
            user.setEmail(mEmail.getText().toString());
            user.signUpInBackground(new SignUpCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                       // final Intent intent = new Intent(getActivity(), HomeActivity.class);
                       // startActivity(intent);
                        Toast.makeText(getActivity(),
                                "Successfully Registered, Please contact your admin to use the app",
                                Toast.LENGTH_LONG).show();
                        getActivity().finish();
                      /*  ParseObject parse=new ParseObject("ApplicationUsers");
                        parse.put("Email", mEmail.getText().toString());
                        if(mPhone.getText().toString().equals("teacher"))
                        {
                            parse.put("Type", "T");
                            intent.putExtra("type","teacher");
                        }
                        else {
                            parse.put("Type", "S");
                            intent.putExtra("type", "student");
                        }
                        parse.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                startActivity(intent);
                                Toast.makeText(getActivity(),
                                        "Successfully Registered",
                                        Toast.LENGTH_LONG).show();
                                getActivity().finish();
                            }
                        });*/



                    } else {
                        e.printStackTrace();
                        Toast.makeText(getActivity(),
                                "Plaese Try again",
                                Toast.LENGTH_LONG).show();
                        // Sign up didn't succeed. Look at the ParseException
                        // to figure out what went wrong
                    }
                }
            });

        }
    }



    @Override
    public void onPause() {
        super.onPause();

    }



}
