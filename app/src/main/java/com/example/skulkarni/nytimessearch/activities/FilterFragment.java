package com.example.skulkarni.nytimessearch.activities;

import android.app.DatePickerDialog;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.Serializable;
import java.util.Date;

import com.example.skulkarni.nytimessearch.DatePickerFragment;

import com.example.skulkarni.nytimessearch.R;
import com.example.skulkarni.nytimessearch.Filter;


import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by skulkarni on 8/14/16.
 */
public class FilterFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private static Filter filter;
    Spinner s;
    EditText date;
    Boolean sortOrder;
    Button filterBtn;
    Filter newFilter;
    CheckBox sports;
    CheckBox arts;
    CheckBox fashion;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.filter, container, false);
    }

    public static FilterFragment newInstance(Filter f) {
        FilterFragment frag = new FilterFragment();
        filter = f;
        Bundle args = new Bundle();
        args.putString("beginDate", f.getBeginDate().toString());
        frag.setArguments(args);
        return frag;
    }

    public interface FilterDialogListener {
        void onFinishDialog(Filter f);
    }

    public void sendBackResult(Filter f) {
        FilterDialogListener fdl = (FilterDialogListener) getActivity();
        fdl.onFinishDialog(f);
        dismiss();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        s = (Spinner) view.findViewById(R.id.spSort);
        sports = (CheckBox) view.findViewById(R.id.sports);
        arts = (CheckBox) view.findViewById(R.id.arts);
        fashion = (CheckBox) view.findViewById(R.id.fashion);

        s.setSelection(0);

        date = (EditText) view.findViewById(R.id.eTDate);
        filterBtn = (Button) view.findViewById(R.id.filter);
        date.setInputType(InputType.TYPE_NULL);

        final DialogFragment self = this;
        // set date based on filter
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
        String dateString = sdf.format(filter.getBeginDate());
        date.setText(dateString);

        // set sort order based on filter
        if (filter.getSortOrder()) {
            s.setSelection(0);
        }
        else {
            s.setSelection(1);
        }

        // set news desk based on filter
        String newsDesk = filter.getNewsDesk();
        if (newsDesk.indexOf("Sports") != -1) {
            sports.setChecked(true);
        }
        if (newsDesk.indexOf("Arts") != -1) {
            arts.setChecked(true);
        }
        if (newsDesk.indexOf("Fashion") != -1) {
            fashion.setChecked(true);
        }

        final FragmentManager fm = this.getFragmentManager();

        date.setOnClickListener(new EditText.OnClickListener(){
            @Override
            public void onClick(View view) {
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.setTargetFragment(self, 300);
                newFragment.show(fm, "datePicker");
            }
        });

        filterBtn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Date d = new Date(Date.parse(date.getText().toString()));
                if (s.getSelectedItemPosition() == 0) {
                    sortOrder = true;
                }
                else {
                    sortOrder = false;
                }

                String newsDesk = "";
                
                if (sports.isChecked()) {
                    newsDesk += "Sports ";
                }
                
                if (arts.isChecked()) {
                    newsDesk += "Arts ";
                }
                
                if (fashion.isChecked()) {
                    newsDesk += "Fashion & Style";
                }
                
                filter.setNewsDesk(newsDesk);

                newFilter = new Filter(d, sortOrder, filter.getNewsDesk());
                sendBackResult(newFilter);
            }
        });
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        // store the values selected into a Calendar instance
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        long selectedDate = c.getTimeInMillis();

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
        String dateString = sdf.format(selectedDate);
        date.setText(dateString);
        filter.setBeginDate(new Date(selectedDate));
    }

}
