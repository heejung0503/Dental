package com.example.lafamila;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    Button login, join;
    EditText id, pw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.login_login);
        join = findViewById(R.id.login_join);

        id = findViewById(R.id.login_id);
        pw = findViewById(R.id.login_pw);


        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), JoinActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                (new LoginTask()).execute("http://10.0.2.2", "5000", "login", "id", id.getText().toString(), "pw", pw.getText().toString());
            }
        });
    }

    private class LoginTask extends GetTask{
        @Override
        protected void onPostExecute(String s) {
            Log.d("lafamila", s);
            try {
                JSONArray result = new JSONArray(s);
                if(result.length() > 0){
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    intent.putExtra("user_sn", ((JSONObject)result.get(0)).getInt("user_sn"));
                    startActivity(intent);
                }
            } catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}
