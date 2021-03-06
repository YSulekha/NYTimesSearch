package com.codepath.alse.nytimessearch.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.codepath.alse.nytimessearch.Model.Filter;
import com.codepath.alse.nytimessearch.R;
import com.codepath.alse.nytimessearch.activities.SearchActivity;
import com.codepath.alse.nytimessearch.databinding.FilterLayoutBinding;

import org.parceler.Parcels;

import java.util.Calendar;

import static com.codepath.alse.nytimessearch.R.id.checkbox_arts;
import static com.codepath.alse.nytimessearch.R.id.checkbox_fashion;
import static com.codepath.alse.nytimessearch.R.id.checkbox_sports;
import static com.codepath.alse.nytimessearch.R.id.filter_save;
import static com.codepath.alse.nytimessearch.R.id.sort_dateValue;

/**
 * Created by aharyadi on 10/22/16.
 */

public class FilterDialogFragment extends DialogFragment {

    public final static String EXTRA_FILTER = "extra_fliter";
    Filter filter;
    Spinner sort;
    CheckBox arts;
   CheckBox fashion;
     CheckBox sports;
    FilterLayoutBinding binding;
     static EditText date;
    SaveFilterListener listener;
    boolean isArts = false;
    boolean isFashion = false;
    boolean isSports = false;
    static String mDate;
    String news = "news_desk:(";
    static Context mContext;


    public interface SaveFilterListener{
       void onSaveFilter(Filter f);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static FilterDialogFragment newInstance(Context context,Bundle args) {

        FilterDialogFragment fragment = new FilterDialogFragment();
        fragment.setArguments(args);
        mContext = context;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
 binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), com.codepath.alse.nytimessearch.R.layout.filter_layout, null, false);

        binding.setHandlers(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
      //  ButterKnife.bind(this, view);

        filter = (Filter) Parcels.unwrap(getArguments().getParcelable(EXTRA_FILTER));


       // date = (EditText) view.findViewById(R.id.sort_dateValue);
        date = binding.sortDateValue;
        sort = binding.sortOrder;
        arts = binding.checkboxArts;
        fashion = binding.checkboxFashion;
        sports = binding.checkboxSports;
        if(filter.getDate() != null){
            date.setText(filter.getDate());
        }
        Log.v("dsff",filter.toString());
        ArrayAdapter<CharSequence> sort_adapter = ArrayAdapter.createFromResource(getContext(),R.array.sort_order,android.
        R.layout.simple_spinner_item);
        sort.setAdapter(sort_adapter);
        sort.setSelection(sort_adapter.getPosition(filter.getSortOrder()));
        fashion.setChecked(filter.isFashion());
        isArts = filter.isArts();
        isFashion = filter.isFashion();
        isSports = filter.isSports();
        arts.setChecked(filter.isArts());
        sports.setChecked(filter.isSports());
    }

   // @OnClick ({R.id.checkbox_arts,R.id.checkbox_sports,R.id.checkbox_fashion,R.id.filter_save,sort_dateValue})
    public  void onCheck(View v){
        switch(v.getId()){
            case checkbox_arts:
                if(arts.isChecked()) {
                    isArts = true;
                }
                else{
                    isArts = false;
                }
                break;
            case checkbox_fashion:
                if(fashion.isChecked()) {
                    isFashion = true;
                }
                else{
                    isFashion = false;
                }
                break;
            case checkbox_sports:
                if(sports.isChecked()){
                    isSports = true;
                }
                else{
                    isSports=false;
                }
                break;
            case filter_save:
                onFilterSave(v);
                dismiss();
                break;
            case sort_dateValue:
                createDateFragment();
                break;

        }
    }

    public void onFilterSave(View v){
        listener = (SearchActivity)getActivity();
        filter.setSortOrder(sort.getSelectedItem().toString());

        filter.setNews(news);
        filter.setArts(isArts);
        filter.setFashion(isFashion);
        filter.setSports(isSports);
        filter.setDate(mDate);
        listener.onSaveFilter(filter);

    }
    public void createDateFragment(){
        DialogFragment f = new DatePickerFragmnet();
        f.show(getActivity().getSupportFragmentManager(),"fdsf");
    }

    public static class DatePickerFragmnet extends DialogFragment implements DatePickerDialog.OnDateSetListener{
        int year;
        int month;
        int day;

        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
               String val = String.valueOf(year);
            if(month < 9){
                val = val + "0"+String.valueOf(month+1);
            }
            else{
                val = val + String.valueOf(month+1);
            }
            if(day < 10){
                val = val + "0"+String.valueOf(day);
            }
            else{
                val = val + String.valueOf(day);
            }

            mDate = val;
            settext(val);
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),this,year,month,day);
            return datePickerDialog;
        }
    }

    public  static void settext(String val){
        date.setText(val);
    }
}
