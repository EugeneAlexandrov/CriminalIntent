package com.mybclym.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.fragment.app.Fragment;

import java.util.UUID;

public class CrimeActivity extends SingleFragmentActivity {
    private static final String EXTRA_CRIME_ID = "com.mybclym.criminalintent.extra_crime_id";

    // из интента ниже мы можем получить id и прикрепить к аргументам фрагмента
    @Override
    protected Fragment createFragment() {
        return CrimeFragment.newInstance((UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID));
    }

    // этот интент создается в crimeListFragment и передает сюда crimeID
    protected static Intent newIntent(Context packagecontext, UUID crime_id) {
        Intent intent = new Intent(packagecontext, CrimeActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crime_id);
        return intent;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("TEST", "CrimeActivity onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("TEST", "CrimeActivity onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("TEST", "CrimeActivity onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("TEST", "CrimeActivity onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("TEST", "CrimeActivity onDestroy");
    }
}
