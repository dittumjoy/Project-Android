package com.app.thoughtsharing;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.app.thoughtsharing.Adapters.AllclassListAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.squareup.otto.Subscribe;

import java.util.List;

/**
 * Created by dewneot-pc on 12/11/2015.
 */
public class AllclassesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView mRecyclerView;
    ProgressBar mProgressBar;
    List<ParseObject> mParseObjects;
    List<ParseObject> mClasses;
    AllclassListAdapter mAdapter;
    public AllclassesFragment() {
        // Required empty public constructor
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public static AllclassesFragment newInstance(String param1, String param2) {
        AllclassesFragment fragment = new AllclassesFragment();
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
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        BusProvider.getInstance().unregister(this);
    }

    @Subscribe
    public void handleButtonPress(String s) {
        Toast.makeText(getActivity(), "Event handled   " + s, Toast.LENGTH_SHORT).show();

            if(mClasses!=null)
            {
                for (int i=0;i<mClasses.size();i++)
                {
                    if(mClasses.get(i).getObjectId().equals(s))
                    {
                        mClasses.remove(i);
                        mAdapter.notifyDataSetChanged();
                        break;

                    }
                }
            }


    }
    @Subscribe
    public void addclasses(String s)
    {
        if(s.equals("added"))
        {
         loadpage();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_all_classes, container, false);
        LinearLayoutManager layoutManager= new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView= (RecyclerView) v.findViewById(R.id.myclasses_list_recyclerview);
        mProgressBar=(ProgressBar) v.findViewById(R.id.allclass_progress);
        mRecyclerView.setLayoutManager(layoutManager);

       loadpage();

        return  v;
    }

    private void loadpage() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Groups");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> Classes, ParseException e) {
                if (e == null) {
                    mClasses=Classes;
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.GONE);
                    mAdapter=new AllclassListAdapter(getActivity(),Classes);
                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),
                            "Please try agian",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}
