package com.mybclym.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.DateFormat;
import java.util.Date;
import java.util.UUID;

public class CrimeFragment extends Fragment {
    private Crime mCrime;
    EditText edittext_title;
    Button btn_date;
    CheckBox checkbox_isSolved;
    private static final String ARGS_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE = "DialogDAte";
    private static final int REQUEST_DATE = 0;

    protected static CrimeFragment newInstance(UUID id) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARGS_CRIME_ID, id);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TEST", "Fragment Crime onCreate");
        UUID mID = (UUID) getArguments().getSerializable(ARGS_CRIME_ID);
        mCrime = CrimeLab.get().getCrime(mID);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("TEST", "Fragment Crime onResume");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("TEST", "Fragment Crime onCreateView");
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        edittext_title = (EditText) v.findViewById(R.id.crimeFragment_edittext_title);
        edittext_title.setText(mCrime.getTitle());
        edittext_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btn_date = (Button) v.findViewById(R.id.crimeFragment_date_btn);
        btn_date.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(mCrime.getDate()));
        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(getFragmentManager(), DIALOG_DATE);
            }
        });

        checkbox_isSolved = (CheckBox) v.findViewById(R.id.crimeFragment_checkbox_isSolved);
        checkbox_isSolved.setChecked(mCrime.isSolved());
        checkbox_isSolved.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == REQUEST_DATE) {
            Date mDate = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_CRIME_DATE);
            btn_date.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(mDate));
            mCrime.setDate(mDate);
        }
    }
}
