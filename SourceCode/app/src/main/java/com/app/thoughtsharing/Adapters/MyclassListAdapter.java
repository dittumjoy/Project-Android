package com.app.thoughtsharing.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.thoughtsharing.BusProvider;
import com.app.thoughtsharing.DetailActivity;
import com.app.thoughtsharing.R;
import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.List;

/**
 * Created by dewneot-pc on 12/10/2015.
 */
public class MyclassListAdapter extends RecyclerView.Adapter<MyclassListAdapter.ViewHolder> {


    Context con;
    List<ParseObject> parseObjects;

    public MyclassListAdapter(Context activity,List<ParseObject> mParseObjects) {
        con=  activity;
       parseObjects= mParseObjects;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //arr = new int[]{R.drawable.image1, R.drawable.image2, R.drawable.image3, R.drawable.image4, R.drawable.image5, R.drawable.image6, R.drawable.image1, R.drawable.image2};
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.classeslistitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,  final int position) {
        // holder.mImageView = mValues.getNews().get(position).getTitle();

        final int count=position;
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("haiobj",parseObjects.get(position).getObjectId());
                Intent browserIntent = new Intent(con, DetailActivity.class);
                browserIntent.putExtra("objectId",parseObjects.get(position).getObjectId());
                browserIntent.putExtra("name",parseObjects.get(position).getString("GroupName"));
                con.startActivity(browserIntent);
            }
        });
        holder.mTitle.setText(parseObjects.get(position).getString("GroupName"));
        holder.mDes.setText(parseObjects.get(position).getString("GroupDescription"));
        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                new AlertDialog.Builder(con)
                        .setTitle("Delete entry")
                        .setMessage("Are you sure you want to delete this entry?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                // continue with delete
                                parseObjects.get(position).deleteInBackground(new DeleteCallback() {
                                    @Override
                                    public void done(ParseException e) {

                                        Toast.makeText(con,"Successfully deleted",Toast.LENGTH_SHORT).show();
                                        BusProvider.getInstance().post(parseObjects.get(position).getObjectId());
                                        parseObjects.remove(position);
                                        notifyDataSetChanged();



                                    }
                                });
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return true;
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

