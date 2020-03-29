package com.mybclym.criminalintent;

import android.util.Log;

import androidx.fragment.app.Fragment;

public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("TEST", "CrimeListActivity onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("TEST", "CrimeListActivity onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("TEST", "CrimeListActivity onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("TEST", "CrimeListActivity onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("TEST", "CrimeListActivity onDestroy");
    }
}
