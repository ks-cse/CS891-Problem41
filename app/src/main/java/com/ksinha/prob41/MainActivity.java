package com.ksinha.prob41;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private AddBookAdapter abAdapter;

    private List<Contact> myContact = new ArrayList<>();
    Dialog dl;
    Intent intent;
    Contact cn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recycler_view);
        abAdapter = new AddBookAdapter(myContact);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(abAdapter); // Mandatory

        // Activity to be performed on touching each item
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                cn = myContact.get(position);
                int id = cn.getId();
                dl = onCreateMyDialog(id);
                dl.show();
                Toast.makeText(getApplicationContext(), cn.getName() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        // Database Handing
        DatabaseHandler dbh = new DatabaseHandler(this);
        ArrayList<Contact> contacts = dbh.getAllNames();
        for(Contact ct:contacts){
            myContact.add(new Contact(ct.getId(),ct.getName(),ct.getPhone(),ct.getEmail()));
        }

        //prepareFakeData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.mnu_add:
                //finish();
                intent = new Intent(this, ContactActivity.class);
                startActivity(intent);
                break;
            case R.id.mnu_refresh:
                finish();
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.mnu_about:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("This app is created by:\nK Sinha").setTitle("About");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.mnu_exit:
                finish();
                return true;
            default:
                return true;
        }
        //return true;
        return super.onOptionsItemSelected(item);
    }

    private Dialog onCreateMyDialog(int id) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Select a task for "+cn.getName()).setItems(R.array.task_array, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0: //make a call
                        makePhoneCall();
                        break;
                    case 1: //send email
                        sendEmail();
                        break;
                    case 2: //edit contact
                        intent = new Intent(getApplicationContext(),EditActivity.class);
                        intent.putExtra("id",id);
                        startActivity(intent);
                        break;
                    case 4: //exit dialog
                        dialog.cancel();
                        break;
                }
            }
        });
        return builder.create();
    }

    private void makePhoneCall() {
        try
        {
            if(Build.VERSION.SDK_INT > 22)
            {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 101);
                    return;
                }

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + cn.getPhone()));
                startActivity(callIntent);

            }
            else {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + cn.getPhone()));
                startActivity(callIntent);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void sendEmail() {
        //Log.i("Send email", "");
        String[] TO = {cn.getEmail()};
        //String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        //emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            //Log.i("Finished sending email...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    /* USED FOR TESTING BEFORE IMPLEMENTATION OF SQLITE DATABASE */
    private void prepareFakeData() {
        Contact ct;

        ct = new Contact(1,"Fake Data 01","1231231231","fake01@gmail.com");
        myContact.add(ct);

        ct = new Contact(2,"Fake Data 02","9898989898","fake02@gmail.com");
        myContact.add(ct);

        ct = new Contact(3,"Fake Data 03","3333333333","fake03@gmail.com");
        myContact.add(ct);

        ct = new Contact(4,"Fake Data 04","4545454545","fake04@gmail.com");
        myContact.add(ct);

        ct = new Contact(5,"Fake Data 05","1112223330","fake05@gmail.com");
        myContact.add(ct);

        ct = new Contact(6,"Fake Data 06","7778889990","fake06@gmail.com");
        myContact.add(ct);

        ct = new Contact(7,"Fake Data 07","2525252525","fake07@gmail.com");
        myContact.add(ct);

        ct = new Contact(8,"Fake Data 08","9876543210","fake08@gmail.com");
        myContact.add(ct);

        ct = new Contact(9,"Fake Data 09","1234567890","fake09@gmail.com");
        myContact.add(ct);

        ct = new Contact(10,"Fake Data 10","4488996655","fake10@gmail.com");
        myContact.add(ct);

        ct = new Contact(11,"Fake Data 11","3213213210","fake11@gmail.com");
        myContact.add(ct);

        ct = new Contact(12,"Fake Data 12","1515151515","fake12@gmail.com");
        myContact.add(ct);

        ct = new Contact(13,"Fake Data 13","6000000000","fake13@gmail.com");
        myContact.add(ct);

        ct = new Contact(14,"Fake Data 14","9500000095","fake14@gmail.com");
        myContact.add(ct);

        ct = new Contact(15,"Fake Data 15","3600360036","fake15@gmail.com");
        myContact.add(ct);
    }
}