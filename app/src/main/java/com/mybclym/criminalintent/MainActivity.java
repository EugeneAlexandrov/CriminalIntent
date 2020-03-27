package com.mybclym.criminalintent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
            FragmentManager fm=getSupportFragmentManager();
            Fragment fragment=fm.findFragmentById(R.id.container);
            if(fragment==null){
                fragment=new CrimeFragment();
                fm.beginTransaction().add(R.id.container, fragment).commit();
            }

        }
    }
}
