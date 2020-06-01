package com.mybclym.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import com.google.android.material.button.MaterialButton;


import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static java.text.DateFormat.getDateTimeInstance;

public class CrimeFragment extends Fragment {
    private Crime mCrime;
    EditText edittext_title;
    MaterialButton btn_date, btn_share_report, btn_choose_suspect, btn_call_suspect;
    CheckBox checkbox_isSolved;
    private static final String ARGS_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE = "DialogDAte";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_CONTACT = 1;


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
        mCrime = CrimeLab.get(getActivity()).getCrime(mID);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("TEST", "Fragment Crime onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.get(getActivity()).updateCrime(mCrime);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("TEST", "Fragment Crime onCreateView");
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        btn_share_report = v.findViewById(R.id.share_btn);
        btn_call_suspect = v.findViewById(R.id.phone_suspect_btn);
        btn_choose_suspect = v.findViewById(R.id.crime_suspect_btn);
        edittext_title = (EditText) v.findViewById(R.id.crimeFragment_edittext_title);
        btn_date = (MaterialButton) v.findViewById(R.id.crimeFragment_date_btn);
        checkbox_isSolved = (CheckBox) v.findViewById(R.id.crimeFragment_checkbox_isSolved);


        btn_share_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
                i = Intent.createChooser(i, getString(R.string.crime_report));
                startActivity(i);
            }
        });


        final Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        btn_choose_suspect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(i, REQUEST_CONTACT);
            }
        });
        PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager.resolveActivity(i, packageManager.MATCH_DEFAULT_ONLY) == null) {
            btn_choose_suspect.setEnabled(false);
        }


        btn_call_suspect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + mCrime.getNumber()));
                startActivity(intent);
            }
        });


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


        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(getFragmentManager(), DIALOG_DATE);
            }
        });


        checkbox_isSolved.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });

        updateUI();
        return v;
    }

    private void updateUI() {

        checkbox_isSolved.setChecked(mCrime.isSolved());
        btn_date.setText(java.text.DateFormat.getDateTimeInstance(java.text.DateFormat.LONG, java.text.DateFormat.SHORT).format(mCrime.getDate()));
        edittext_title.setText(mCrime.getTitle());
        if (mCrime.getSuspect() != null) btn_choose_suspect.setText(mCrime.getSuspect());
        btn_call_suspect.setVisibility(mCrime.getNumber() == null ? View.INVISIBLE : View.VISIBLE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != Activity.RESULT_OK) return;
        switch (requestCode) {
            case REQUEST_DATE:
                Date mDate = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_CRIME_DATE);
                btn_date.setText(getDateTimeInstance(java.text.DateFormat.LONG, java.text.DateFormat.SHORT).format(mDate));
                mCrime.setDate(mDate);
                break;
            case REQUEST_CONTACT:
                if (data != null) {
                    Uri contact_uri = data.getData();
                    String[] queryfields = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
                    Cursor c = getActivity().getContentResolver().query(contact_uri, new String[]{ContactsContract.Contacts._ID,
                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                            ContactsContract.CommonDataKinds.Phone.NUMBER,
                            ContactsContract.RawContacts.ACCOUNT_TYPE}, null, null, null);
                    try {
                        if (c.getCount() == 0) return;
                        c.moveToFirst();
                        String suspect = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        String phone = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        mCrime.setSuspect(suspect);
                        mCrime.setNumber(phone);
                        btn_call_suspect.setVisibility(View.VISIBLE);
                        updateUI();
                    } finally {
                        c.close();
                    }
                }
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public String getCrimeReport() {
        String solvedString = null;
        if (mCrime.isSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);
        }
        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();
        String suspect = mCrime.getSuspect();
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect, mCrime.getSuspect());
        }
        return getString(R.string.crime_report, mCrime.getTitle(), dateString, solvedString, suspect);
    }
}
