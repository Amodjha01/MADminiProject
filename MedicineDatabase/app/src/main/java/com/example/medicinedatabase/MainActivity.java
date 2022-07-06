package com.example.medicinedatabase;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.DatePicker;
import java.util.*;
import java.text.*;
import android.app.DatePickerDialog;
import java.util.Calendar;
import android.content.Intent;
import android.provider.AlarmClock;

public class MainActivity extends AppCompatActivity {

    EditText MedicineName, StartDate, EndDate, MedicineQuantity;
    TextView MedicineText, QuantityText;
    Button InsertBtn, DataButton , AlarmBtn;
    EditText Timing;
    DataBase dbConnection;
    final Calendar myCalendar = Calendar.getInstance();
    final Calendar endCalendar = Calendar.getInstance();
    int hour;
    int min;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbConnection = new DataBase(this); //initialising dbconnection
        setContentView(R.layout.activity_main);
        MedicineName = findViewById(R.id.EditText1);
        MedicineText = findViewById(R.id.textView2);
        MedicineQuantity = findViewById(R.id.EditTextQuantity);
        QuantityText = findViewById(R.id.textView7);
        StartDate = findViewById(R.id.startDate);
        EndDate = findViewById(R.id.endDate);
        InsertBtn = findViewById(R.id.insertBtn);
        DataButton = findViewById(R.id.dataBtn);
        Timing= findViewById(R.id.timing);
        //AlarmBtn = findViewById(R.id.alarmBtn);


        DatePickerDialog.OnDateSetListener startdate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabel();
            }
        };

        DatePickerDialog.OnDateSetListener enddate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                endCalendar.set(Calendar.YEAR, year);
                endCalendar.set(Calendar.MONTH, month);
                endCalendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabel();
            }
        };

        StartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(MainActivity.this, startdate, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        EndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(MainActivity.this, enddate, endCalendar.get(Calendar.YEAR), endCalendar.get(Calendar.MONTH), endCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        InsertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = MedicineName.getText().toString();
                String quantity = MedicineQuantity.getText().toString();
                String startdate = StartDate.getText().toString();
                String enddate = EndDate.getText().toString();
                String time = Timing.getText().toString();
                setAlarm(enddate);
                // for storing these values we need a database so we will create a database for this
                boolean insert = dbConnection.insertvalues(name, quantity, startdate, enddate, time);
               if (insert) {
                        Toast.makeText(getApplicationContext(), "Data Inserted Successfully", Toast.LENGTH_SHORT).show();
                        MedicineName.setText(null);
                        MedicineQuantity.setText(null);
                        StartDate.setText(null);
                        EndDate.setText(null);
                        Timing.setText(null);
                }
               else {
                   Toast.makeText(getApplicationContext(), "Data Insertion Failed", Toast.LENGTH_SHORT).show();
               }
               // openAlarm();
            }
        });

        DataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = dbConnection.getdata();
                if (res.getCount() == 0) {
                    Toast.makeText(getApplicationContext(), "No Data Found", Toast.LENGTH_SHORT).show();
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                while (res.moveToNext()) {
                    buffer.append("MedicineName :" + res.getString(0) + "\n");
                    buffer.append("MedicineQuantity :" + res.getString(1) + "\n");
                    buffer.append("StartDate :" + res.getString(2) + "\n");
                    buffer.append("EndDate :" + res.getString(3) + "\n");
                    buffer.append("MedicineTime :" + res.getString(4) + "\n");
                    buffer.append("\n");
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Remaining Medicines");
                builder.setMessage(buffer.toString());
                builder.show();
            }
        });

        //        AlarmBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
//                intent.putExtra(AlarmClock.EXTRA_HOUR,hour);
//                intent.putExtra(AlarmClock.EXTRA_MINUTES,min);
//
//                if(hour<=24 && min<=60){
//                    startActivity(intent);
//                }
//            }
//        });
    }

    private void openAlarm() {
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
        intent.putExtra(AlarmClock.EXTRA_HOUR,hour);
        intent.putExtra(AlarmClock.EXTRA_MINUTES,min);
        if(hour<=24 && min<=60){
            startActivity(intent);
        }
    }
    private void openAlarm(Calendar c) {
        Toast.makeText(this,"Alarm set", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
        Intent intent1 = intent.putExtra(AlarmClock.EXTRA_DAYS, c.get(Calendar.DAY_OF_MONTH));
        intent.putExtra(AlarmClock.EXTRA_HOUR,c.get(Calendar.HOUR));
        intent.putExtra(AlarmClock.EXTRA_MINUTES,c.get(Calendar.MINUTE));
        if(hour<=24 && min<=60){
            startActivity(intent);
        }
    }

    private void setAlarm(String enddate) {
        Calendar alarmCalendar = endCalendar;
        alarmCalendar.add(Calendar.DATE,-3);
        alarmCalendar.set(Calendar.HOUR, 8);
        alarmCalendar.set(Calendar.MINUTE, 0);
        openAlarm(alarmCalendar);
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        StartDate.setText(dateFormat.format(myCalendar.getTime()));
        EndDate.setText(dateFormat.format(endCalendar.getTime()));
    }

    public void popTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimesetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                min = selectedMinute;
                String time = String.format(Locale.getDefault(), "%02d:%02d", hour, min);
                if (hour >= 12 && hour < 17) {
                    Timing.setText("Afternoon");
                } else if (hour < 12) {
                    Timing.setText("Morning");
                } else if (hour >= 17 && hour < 19) {
                    Timing.setText("Evening");
                } else if (hour >= 19) {
                    Timing.setText("Night");
                }
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,onTimesetListener,hour,min,true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }
}