package com.example.androidlabs;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomActivity extends AppCompatActivity {

    ListView listView;
    EditText editText;
    List<message> UserMessage = new ArrayList<>();
    Button btnSend;
    Button btnReceive;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        listView = (ListView)findViewById(R.id.lstView);
        editText = (EditText)findViewById(R.id.chatEditText);
        btnSend = (Button)findViewById(R.id.SendBtn);
        btnReceive = (Button)findViewById(R.id.ReceiveBtn);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMessage();
            }
        });

        btnReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }
    public void getMessage(){
        String sendMessage = editText.getText().toString();
        if ( !sendMessage.equals("")){
            message model = new message(sendMessage, true);
            UserMessage.add(model);

            ChatAdapter adt = new ChatAdapter(UserMessage, getApplicationContext());
            listView.setAdapter(adt);
            editText.setText("");
        }
    };

    public void sendMessage(){
        String sendMessage = editText.getText().toString();
        if ( !sendMessage.equals("")){
            message model = new message(sendMessage, false);
            UserMessage.add(model);

            ChatAdapter adt = new ChatAdapter(UserMessage, getApplicationContext());
            listView.setAdapter(adt);
            editText.setText("");
        }
    }
}

class ChatAdapter extends BaseAdapter{

    private List<message> messageModels;
    private Context context;
    private LayoutInflater inflater;

    public ChatAdapter(List<message> messageModels, Context context) {
        this.messageModels = messageModels;
        this.context = context;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return messageModels.size();
    }

    @Override
    public Object getItem(int position) {
        return messageModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null){
            if (messageModels.get(position).isSend()){
                view = inflater.inflate(R.layout.activity_send, null);

            }else {
                view = inflater.inflate(R.layout.activity_recieve, null);
            }
            TextView messageText = (TextView)view.findViewById(R.id.messageText);
            messageText.setText(messageModels.get(position).getMsg());
        }
        return view;
    }
}
