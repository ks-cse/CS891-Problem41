package com.ksinha.prob41;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {
    EditText edn,edp,ede;
    Button btn_save,btn_del,btn_can;
    int id;
    Contact ct_ed;// = new Contact();
    DatabaseHandler dbh;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        context = this;
        edn = findViewById(R.id.ed_name_ed);
        edp = findViewById(R.id.ed_phone_ed);
        ede = findViewById(R.id.ed_email_ed);
        btn_save = findViewById(R.id.btn_edit);
        btn_del = findViewById(R.id.btn_del);
        btn_can = findViewById(R.id.btn_cancel);

        id = getIntent().getIntExtra("id",0);
        dbh = new DatabaseHandler(this);
        ct_ed = dbh.getContact(id);

        edn.setText(ct_ed.getName());
        edp.setText(ct_ed.getPhone());
        ede.setText(ct_ed.getEmail());

        btn_can.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ct_ed.setName(edn.getText().toString());
                ct_ed.setPhone(edp.getText().toString());
                ct_ed.setEmail(ede.getText().toString());

                int success = dbh.updateContact(ct_ed);
                finish();
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            }
        });

        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbh.deleteContact(ct_ed);
                finish();
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);

/*                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure to delete this contact ?").setTitle("Confirmation");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        dbh.deleteContact(ct_ed);
                        finish();
                        Intent intent = new Intent(context, MainActivity.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked CANCEL button
                    }
                });*/
            }
        });
    }
}