package com.example.hechbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    RecyclerView rv;

    BottomNavigationView bnv;
    private  String url;
    private final String baseUrl="http://api.brainshop.ai/";
    ArrayList<MessageModel> messages;
    EditText editText;
    Button send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bnv=findViewById(R.id.answer_bloc);
        bnv.setVisibility(View.INVISIBLE);
        editText= findViewById(R.id.editText);
        send = findViewById(R.id.send);
        messages= new ArrayList<MessageModel>();
        rv = findViewById(R.id.recycler_view);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().isEmpty())
                {
                    bnv.setVisibility(View.INVISIBLE);

                    Toast.makeText(getApplicationContext(),"Please enter your message !",Toast.LENGTH_SHORT);

                    return;
                }

                messages.add(new MessageModel(editText.getText().toString(),ChatAdapter.MESSAGE_TYPE_IN));

                bnv.setVisibility(View.INVISIBLE);

                rv.setAdapter(new ChatAdapter(getApplicationContext(),messages));

                botResponse(editText.getText().toString());
                editText.setText("");
            }

        });

    }
    private void botResponse(String messageUser){
        url= "http://api.brainshop.ai/get?bid=162980&key=oFj1FJPlwh8ZeAye&uid=[uid]&msg="+messageUser;
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<Response> responseCall= retrofitAPI.getMessage(url);
        responseCall.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if( response.isSuccessful())
                {
                    String resp = response.body().getCnt();
                    if((resp.contains("Hi")|| resp.contains("Hello")|| resp.contains("Hey"))&& messageUser.toLowerCase().contains("aaslema"))
                    {
                        resp = resp.replace("Hi,","Hi, Boulhech, ");
                        resp = resp.replace("Hey,","Hi Boulhech, ");
                        resp = resp.replace("Hello,","Hello Boulhech, ");
                    }

                    MessageModel m = new MessageModel(resp,ChatAdapter.MESSAGE_TYPE_OUT);

                    messages.add(m);
                    rv.setAdapter(new ChatAdapter(getApplicationContext(),messages));
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Log.e("error 1: ",t.getMessage());
                Log.e("error 2: ",call.toString());
                messages.add(new MessageModel("What does that mean sir ?",ChatAdapter.MESSAGE_TYPE_OUT));
                rv.setAdapter(new ChatAdapter(getApplicationContext(),messages));



            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.bottom_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.answer:
                bnv.setVisibility(View.VISIBLE);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
