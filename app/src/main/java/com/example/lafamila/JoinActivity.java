package com.example.lafamila;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class JoinActivity extends AppCompatActivity {
    Button join;
    EditText id, pw;
    Switch isHospital;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        join = findViewById(R.id.join_join);

        id = findViewById(R.id.join_id);
        pw = findViewById(R.id.join_pw);
        isHospital = findViewById(R.id.join_hospital);

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getBaseContext(), ""+isHospital.isChecked(), Toast.LENGTH_SHORT).show();
                String hospital = isHospital.isChecked() ? "1" : "0";
                (new JoinTask()).execute("http://10.0.2.2", "5000", "join", "id", id.getText().toString(), "pw", pw.getText().toString(), "hospital", hospital);

            }
        });

    }

    private class JoinTask extends PostTask {
        @Override
        protected void onPostExecute(String s) {
            finish();
        }
    }
}
