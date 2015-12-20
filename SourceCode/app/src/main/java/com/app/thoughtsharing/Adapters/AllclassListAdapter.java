package com.app.thoughtsharing.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.thoughtsharing.App.ThoughtSharingApp;
import com.app.thoughtsharing.DetailActivity;
import com.app.thoughtsharing.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.HashMap;
import java.util.List;

/**
 * Created by dewneot-pc on 12/10/2015.
 */
public class AllclassListAdapter extends RecyclerView.Adapter<AllclassListAdapter.ViewHolder> {


    Context con;
    List<ParseObject> parseObjects;
    HashMap<String,Boolean> liked;
    public AllclassListAdapter(Context activity, List<ParseObject> mParseObjects) {
        con=activity;
       parseObjects= mParseObjects;
        liked=new HashMap<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //arr = new int[]{R.drawable.image1, R.drawable.image2, R.drawable.image3, R.drawable.image4, R.drawable.image5, R.drawable.image6, R.drawable.image1, R.drawable.image2};
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.allclasseslistitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder,  final int position) {
        // holder.mImageView = mValues.getNews().get(position).getTitle();

        final int count=position;
        liked.put(parseObjects.get(position).getObjectId(), false);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Likes");
        query.whereEqualTo("userID", ThoughtSharingApp.getInstance().getmUsernamae());
        query.whereEqualTo("classID", parseObjects.get(position).getObjectId());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> mUsers, ParseException e) {
                if (e == null) {

                    if (mUsers.size() > 0) {
                        Log.d("hai", "liked " + mUsers.get(0).getString("classID") + " users");
                        if(mUsers.get(0).getString("like").equals("true")) {
                            holder.mLike.setBackgroundResource(R.drawable.like_pressed);
                            liked.put(parseObjects.get(position).getObjectId(), true);
                        }else
                        {
                            holder.mLike.setBackgroundResource(R.drawable.like);
                            liked.put(parseObjects.get(position).getObjectId(), false);
                        }
                    }
                    else
                    {
                        holder.mLike.setBackgroundResource(R.drawable.like);
                        liked.put(parseObjects.get(position).getObjectId(), false);
                    }

                } else {
                    holder.mLike.setBackgroundResource(R.drawable.like);
                    liked.put(parseObjects.get(position).getObjectId(), false);
                    Toast.makeText(con,
                            "Please contact your Teacher",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("haiobj", parseObjects.get(position).getObjectId());
                Intent browserIntent = new Intent(con, DetailActivity.class);
                browserIntent.putExtra("objectId", parseObjects.get(position).getObjectId());
                browserIntent.putExtra("name", parseObjects.get(position).getString("GroupName"));
                con.startActivity(browserIntent);
            }
        });
        holder.mTitle.setText(parseObjects.get(position).getString("GroupName"));
        holder.mDes.setText(parseObjects.get(position).getString("GroupDescription"));

        holder.mLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(liked.get(parseObjects.get(position).getObjectId())) {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Likes");
                    query.whereEqualTo("userID", ThoughtSharingApp.getInstance().getmUsernamae());
                    query.whereEqualTo("classID", parseObjects.get(position).getObjectId());
                    query.findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> mUsers, ParseException e) {
                            if (e == null) {

                                if (mUsers.size() > 0) {
                                    mUsers.get(0).put("like", "false");
                                    mUsers.get(0).saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (e == null) {

                                                holder.mLike.setBackgroundResource(R.drawable.like);
                                                liked.put(parseObjects.get(position).getObjectId(), false);
                                            }

                                        }
                                    });
                                } else {
                                    Log.d("hai", "No classes Found!!");
                                }
                            }
                        }
                    });
                }else {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Likes");
                    query.whereEqualTo("userID", ThoughtSharingApp.getInstance().getmUsernamae());
                    query.whereEqualTo("classID", parseObjects.get(position).getObjectId());
                    query.findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> mUsers, ParseException e) {
                            if (e == null) {

                                if (mUsers.size() > 0) {
                                    mUsers.get(0).put("like", "true");
                                    mUsers.get(0).saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (e == null) {

                                                holder.mLike.setBackgroundResource(R.drawable.like_pressed);
                                                liked.put(parseObjects.get(position).getObjectId(), true);
                                            }

                                        }
                                    });
                                } else {
                                    Log.d("hai", "No classes Found!!");
                                    final ParseObject po = new ParseObject("Likes");
                                    po.put("classID", parseObjects.get(position).getObjectId());
                                    po.put("like", "true");
                                    po.put("userID", ThoughtSharingApp.getInstance().getmUsernamae());

                                    //po.saveInBackground();
                                    // Replaced above call with one below so objectId can be saved for future calls.
                                    po.saveInBackground(new SaveCallback() {
                                        public void done(ParseException e) {
                                            if (e == null) {
                                                holder.mLike.setBackgroundResource(R.drawable.like_pressed);
                                                liked.put(parseObjects.get(position).getObjectId(), true);
                                            } else {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    });

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return parseObjects.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final TextView mTitle;
        public final TextView mDes;
        public final Button mLike;

        public ViewHolder(View view) {
            super(view);

            mView = view;
            mTitle = (TextView) view.findViewById(R.id.title);
            mDes= (TextView) view.findViewById(R.id.description);
            mLike=(Button)view.findViewById(R.id.like);
        }


    }
}

