package com.mybclym.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String EXTRA_CRIME_ID = "com.mybclym.criminalintent.extra_crime_id";
    private static final String EXTRA_CRIME_POSITION = "com.mybclym.criminalintent.extra_crime_position";
    Button viewpager_btn_first, viewpager_btn_last;
    ViewPager crime_viewpager;
    List<Crime> mCrimeList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);
        mCrimeList = CrimeLab.get(this).getCrimes();
        crime_viewpager = (ViewPager) findViewById(R.id.crime_viewpager);
        viewpager_btn_first = (Button) findViewById(R.id.viewpager_btn_first);
        viewpager_btn_last = (Button) findViewById(R.id.viewpager_btn_last);
        viewpager_btn_last.setOnClickListener(this);
        viewpager_btn_first.setOnClickListener(this);
        crime_viewpager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                Log.d("TEST", String.valueOf(position));
                viewpager_btn_first.setEnabled(crime_viewpager.getCurrentItem() != 0);
                viewpager_btn_last.setEnabled(crime_viewpager.getCurrentItem() != mCrimeList.size() - 1);
                return CrimeFragment.newInstance(mCrimeList.get(position).getID());
            }

            @Override
            public int getCount() {
                return mCrimeList.size();
            }


        });
        crime_viewpager.setCurrentItem(getIntent().getIntExtra(EXTRA_CRIME_POSITION, 0));

    }

    protected static Intent newIntent(Context packagecontext, UUID crime_id, int position) {
        Intent intent = new Intent(packagecontext, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crime_id);
        intent.putExtra(EXTRA_CRIME_POSITION, position);
        return intent;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.viewpager_btn_first:
                crime_viewpager.setCurrentItem(0);
                break;
            case R.id.viewpager_btn_last:
                crime_viewpager.setCurrentItem(mCrimeList.size() - 1);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fragment_crime, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete_crime:
                CrimeLab.get(this).delete_crime((UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID));
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
