package com.example.android.project1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnRangeSelectedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CalendarActivity extends AppCompatActivity {
    TextView selectedDatesBegin, selectedDatesEnd, selectDateSingle;
    Button selectDates;
    private List<Long> dates = new ArrayList<>();
    Date singleDate, beginDate, endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final MaterialCalendarView calendar = findViewById(R.id.calendarView);
        calendar.setSelectionMode(MaterialCalendarView.SELECTION_MODE_RANGE);
        //final List<Calendar> listSelectedDates = calendar.getSelectedDates();
        selectedDatesBegin = findViewById(R.id.selected_date_begin);
        selectedDatesEnd = findViewById(R.id.selected_date_end);
        selectDateSingle = findViewById(R.id.selected_date_single);
        selectDates = findViewById(R.id.select_dates);



        calendar.setOnRangeSelectedListener(new OnRangeSelectedListener() {
            @Override
            public void onRangeSelected(@NonNull MaterialCalendarView widget, @NonNull List<CalendarDay> dates) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, d MMMM yyyy");
                selectDateSingle.setVisibility(View.GONE);
                selectedDatesBegin.setVisibility(View.VISIBLE);
                selectedDatesEnd.setVisibility(View.VISIBLE);
                selectedDatesBegin.setText(dateFormat.format(dates.get(0).getCalendar().getTime()));
                selectedDatesEnd.setText(dateFormat.format(dates.get(dates.size()-1).getCalendar().getTime()));

            }
        });

        calendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                selectedDatesBegin.setVisibility(View.GONE);
                selectedDatesEnd.setVisibility(View.GONE);
                selectDateSingle.setVisibility(View.VISIBLE);
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, d MMMM yyyy");
                selectDateSingle.setText(dateFormat.format(date.getCalendar().getTime()));
            }
        });

        selectDates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, d MMMM yyyy");
                Intent intent = new Intent(CalendarActivity.this, HistoryActivity.class);
                if (selectDateSingle.getVisibility() == View.GONE) {
                    try {
                    beginDate = dateFormat.parse(selectedDatesBegin.getText().toString());
                    endDate = dateFormat.parse(selectedDatesEnd.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    dates.add(beginDate.getTime());
                    dates.add(endDate.getTime());
                    intent.putExtra("beginDate", dates.get(0).toString());
                    intent.putExtra("endDate", dates.get(1).toString());
                    dates = null;

                } else {
                    try {
                        singleDate = dateFormat.parse(selectDateSingle.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    dates.add(singleDate.getTime());
                    Log.v("My Project", "dates" + dates);
                    intent.putExtra("singleDate", dates.get(0).toString());
                    dates = null;
                }
                startActivity(intent);
                finish();
            }
        });


    }
    /*    @Override
    protected void onResume() {
        super.onResume();
        long currentTime = new Date().getTime();
        long timeDifference = currentTime - Buffer.stopTime;
        if (timeDifference >= 180000 || !Buffer.checkPassword){
            startActivity(new Intent(AboutActivity.this, SighInActivity.class));
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Buffer.setStopTime();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Buffer.checkPassword = false;
    }
*/

}
