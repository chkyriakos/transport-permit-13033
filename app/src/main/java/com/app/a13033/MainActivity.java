package com.app.a13033;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    final int SEND_SMS_PERMISSION_REQUEST_CODE = 1;

    EditText name;
    EditText street;
    Button save;
    String data1;
    String data2;
    RadioButton reason1;
    RadioButton reason2;
    RadioButton reason3;
    RadioButton reason4;
    RadioButton reason5;
    RadioButton reason6;
    Button send;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.name);
        street = findViewById(R.id.street);
        save = findViewById(R.id.save);
        send = findViewById((R.id.send));
        reason1 = findViewById(R.id.radio_one);
        reason2 = findViewById(R.id.radio_two);
        reason3 = findViewById(R.id.radio_three);
        reason4 = findViewById(R.id.radio_four);
        reason5 = findViewById(R.id.radio_five);
        reason6 = findViewById(R.id.radio_six);

        // To retrieve data
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String data11 = prefs.getString("data1", data1);
        String data22 = prefs.getString("data2", data2);
        name.setText(data11);
        street.setText(data22);

        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                data1=name.getText().toString();
                data2=street.getText().toString();
                // To save the data
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("data1",data1);
                editor.putString("data2",data2);
                editor.commit();
                Toast toast = Toast.makeText(MainActivity.this, "Τα στοιχεία σας αποθηκεύτηκαν επιτυχώς!", Toast.LENGTH_SHORT);
                View view = toast.getView();
                view.setBackgroundColor(Color.rgb(191, 191, 191));
                toast.show();
            }
        });

        if (check(Manifest.permission.SEND_SMS)) {
            send.setEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);
        }
    }

    public void sendSms(View v) {
        String reason;

        if (reason1.isChecked()) {
            reason = "1";
        } else if (reason2.isChecked()) {
            reason = "2";
        } else if (reason3.isChecked()) {
            reason = "3";
        } else if (reason4.isChecked()) {
            reason = "4";
        } else if (reason5.isChecked()) {
            reason = "5";
        } else if (reason6.isChecked()) {
            reason = "6";
        } else {
            reason = "empty";
        }
        String number = "13033";
        String str2 = name.getText().toString();
        String str3 = street.getText().toString();
        String sms = reason + " " + str2 + " " + str3;

        if (reason.equals("empty") || str2.equals("") || str3.equals("")) {
            Toast toast = Toast.makeText(this, "Συμπληρώστε όλα τα πεδία!", Toast.LENGTH_SHORT);
            View view = toast.getView();
            view.setBackgroundColor(Color.rgb(191, 191, 191));
            toast.show();
            return;
        }

        final ProgressBar bar = findViewById(R.id.loading_spinner);
        bar.setVisibility(View.VISIBLE);
        bar.postDelayed(new Runnable() {
            @Override
            public void run() {
                bar.setVisibility(View.INVISIBLE);
            }
        }, 1000);

        if (check(Manifest.permission.SEND_SMS)) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(number, null, sms, null, null);
            Toast toast = Toast.makeText(this, "Μήνυμα Εστάλη!", Toast.LENGTH_SHORT);
            View view = toast.getView();
            view.setBackgroundColor(Color.rgb(191, 191, 191));
            toast.show();
        } else {
            Toast toast = Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT);
            View view = toast.getView();
            view.setBackgroundColor(Color.RED);
            toast.show();

        }
        reason1.setChecked(false);
        reason2.setChecked(false);
        reason3.setChecked(false);
        reason4.setChecked(false);
        reason5.setChecked(false);
        reason6.setChecked(false);

    }

    public boolean check(String permission)
    {
        int check = ContextCompat.checkSelfPermission(this, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

}


