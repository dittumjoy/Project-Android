package com.app.thoughtsharing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.thoughtsharing.Adapters.ChatListAdapter;
import com.app.thoughtsharing.App.ThoughtSharingApp;
import com.app.thoughtsharing.model.ChatMessage;
import com.app.thoughtsharing.model.Status;
import com.app.thoughtsharing.model.UserType;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private ListView chatListView;
    private EditText chatEditText1;
    private ArrayList<ChatMessage> chatMessages;
    private ImageView enterChatView1, emojiButton;
    private ChatListAdapter listAdapter;
    private RelativeLayout mRelativeLayout;
    private WindowManager.LayoutParams windowLayoutParams;
    private String classId;
    android.support.v4.widget.SwipeRefreshLayout mSwipeRefreshLayout;
   /* private EditText.OnKeyListener keyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {

            // If the event is a key-down event on the "enter" button
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                // Perform action on key press

                EditText editText = (EditText) v;

                if(v==chatEditText1)
                {
                    sendMessage(editText.getText().toString(), UserType.OTHER);
                }

                chatEditText1.setText("");

                return true;
            }
            return false;

        }
    };*/

    private ImageView.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(v==enterChatView1)
            {
                sendMessage(chatEditText1.getText().toString(), UserType.OTHER);
            }

            chatEditText1.setText("");

        }
    };

    private final TextWatcher watcher1 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (chatEditText1.getText().toString().equals("")) {

            } else {
                enterChatView1.setImageResource(R.drawable.ic_chat_send);

            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if(editable.length()==0){
                enterChatView1.setImageResource(R.drawable.ic_chat_send);
            }else{
                enterChatView1.setImageResource(R.drawable.ic_chat_send_active);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        chatMessages = new ArrayList<>();
        Intent intent=getIntent();

        if(intent.getStringExtra("objectId")!=null) {
            getSupportActionBar().setTitle(intent.getStringExtra("name"));
            Log.d("hai", intent.getStringExtra("objectId") + intent.getStringExtra("name"));
            classId=intent.getStringExtra("objectId");
            loadPage();
        }
        chatListView = (ListView) findViewById(R.id.chat_list_view);

        chatEditText1 = (EditText) findViewById(R.id.chat_edit_text1);
        enterChatView1 = (ImageView) findViewById(R.id.enter_chat1);

        // Hide the emoji on click of edit text
        chatEditText1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

       mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadPage();
            }
        });
        emojiButton = (ImageView)findViewById(R.id.emojiButton);

        emojiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // showEmojiPopup(!showingEmoji);
            }
        });


       // chatEditText1.setOnKeyListener(keyListener);

        enterChatView1.setOnClickListener(clickListener);

        chatEditText1.addTextChangedListener(watcher1);

        mRelativeLayout = (RelativeLayout) findViewById(R.id.chat_layout);


    }
    public void loadPage()
    {
        chatMessages.clear();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("GroupComments");
        query.whereEqualTo("ClassID", classId);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> Classes, ParseException e) {
                if (e == null) {
                    Log.d("hai", "Retrieved " + Classes.size() + " classes");
                    for (ParseObject mclass:Classes)
                    {
                        final ChatMessage message = new ChatMessage();
                        message.setMessageStatus(Status.SENT);
                        message.setMessageText(mclass.getString("Comment"));
                        if(ThoughtSharingApp.getInstance().getmUsernamae().equals(mclass.getString("UserID")))
                        {
                            message.setUserType(UserType.OTHER);
                            message.setMessageStatus(Status.DELIVERED);

                        }
                        else
                        {
                            message.setUserType(UserType.SELF);

                        }
                        message.setUserName(mclass.getString("UserID"));
                        message.setMessageTime(new Date().getTime());
                        chatMessages.add(message);
                    }


                    listAdapter = new ChatListAdapter(chatMessages, DetailActivity.this);

                    chatListView.setAdapter(listAdapter);
                    listAdapter.notifyDataSetChanged();
                    chatListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
                    chatListView.setStackFromBottom(true);
                    mSwipeRefreshLayout.setRefreshing(false);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please try again...",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
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
            mSwipeRefreshLayout.setRefreshing(true);
            loadPage();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void sendMessage(final String messageText, final UserType userType)
    {
        if(messageText.trim().length()==0)
            return;

        final ChatMessage message = new ChatMessage();
        message.setMessageStatus(Status.SENT);
        message.setMessageText(messageText);
        message.setUserType(userType);
        message.setUserName(ThoughtSharingApp.getInstance().getmUsernamae());
        message.setMessageTime(new Date().getTime());
        chatMessages.add(message);

        if(listAdapter!=null)
            listAdapter.notifyDataSetChanged();
        final ParseObject po = new ParseObject("GroupComments");
        po.put("ClassID", classId);
        po.put("Comment", messageText);
        po.put("UserID", ThoughtSharingApp.getInstance().getmUsernamae());

        //po.saveInBackground();
        // Replaced above call with one below so objectId can be saved for future calls.
        po.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Saved successfully.
                    message.setMessageStatus(Status.DELIVERED);
                    DetailActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            listAdapter.notifyDataSetChanged();
                        }
                    });
                } else {
                    e.printStackTrace();
                }
            }
        });

       /* ParseQuery<ParseObject> query = ParseQuery.getQuery("GroupComments");
        query.whereEqualTo("ClassID", classId);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> Classes, ParseException e) {
                if (e == null) {
                    Log.d("hai", "Retrieved " + Classes.size() + " classes");
                    for (ParseObject mclass:Classes)
                    {
                        final ChatMessage message = new ChatMessage();
                        message.setMessageStatus(Status.SENT);
                        message.setMessageText(mclass.getString("Comment"));
                        if(ThoughtSharingApp.getInstance().getmUsernamae().equals(mclass.getString("UserID")))
                        {
                            message.setUserType(UserType.OTHER);
                            message.setMessageStatus(Status.DELIVERED);

                        }
                        else
                        {
                            message.setUserType(UserType.SELF);

                        }
                        message.setUserName(mclass.getString("UserID"));
                        message.setMessageTime(new Date().getTime());
                        chatMessages.add(message);
                    }
                    listAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please try again...",
                            Toast.LENGTH_LONG).show();
                }
            }
        });*/

    }
}
