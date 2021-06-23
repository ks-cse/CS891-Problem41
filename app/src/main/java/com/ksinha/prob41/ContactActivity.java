package com.ksinha.prob41;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ContactActivity extends AppCompatActivity {
    Button btn_add,btn_add_close;
    EditText ed_n,ed_p,ed_e;
    String st_n,st_p,st_e;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        btn_add_close = findViewById(R.id.btn_add_exit);
        btn_add = findViewById(R.id.btn_add);
        ed_n = findViewById(R.id.ed_name);
        ed_p = findViewById(R.id.ed_phone);
        ed_e = findViewById(R.id.ed_email);
        context = this;

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                st_n = ed_n.getText().toString();
                st_p = ed_p.getText().toString();
                st_e = ed_e.getText().toString();

                Contact ct = new Contact(st_n,st_p,st_e);
                DatabaseHandler dbh = new DatabaseHandler(context);
                dbh.addContact(ct);
                Toast.makeText(getApplicationContext(), "Data saved", Toast.LENGTH_LONG).show();
                finish();
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            }
        });

        btn_add_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}