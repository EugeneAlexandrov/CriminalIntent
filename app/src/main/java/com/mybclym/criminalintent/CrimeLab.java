package com.mybclym.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mybclym.criminalintent.database.CrimeBaseHelper;
import com.mybclym.criminalintent.database.CrimeCursorWrapper;
import com.mybclym.criminalintent.database.CrimeDbSchema.CrimeTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CrimeLab {
    private static CrimeLab sCrimeLab;
    private Context mContext;
    private SQLiteDatabase mSQLiteDatabase;

    private CrimeLab(Context context) {
        mContext = context.getApplicationContext();
        mSQLiteDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();

    }

    public void addCrime(Crime crime) {
        ContentValues cv = getContentValues(crime);
        mSQLiteDatabase.insert(CrimeTable.NAME, null, cv);
    }

    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
            Log.d("TEST", " new CrimeLab");
        }
        return sCrimeLab;
    }

    public List<Crime> getCrimes() {
        List<Crime> crimes = new ArrayList<>();
        CrimeCursorWrapper cursor = queryCrimes(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return crimes;
    }

    public Crime getCrime(UUID id) {
        CrimeCursorWrapper cursor = queryCrimes(CrimeTable.Cols.UUID + " = ?", new String[]{id.toString()});
        try {
            if (cursor.getCount() == 0) return null;
            cursor.moveToFirst();
            return cursor.getCrime();
        } finally {
            cursor.close();
        }
    }

    public void delete_crime(int position) {

    }

    private static ContentValues getContentValues(Crime crime) {
        ContentValues cv = new ContentValues();
        cv.put(CrimeTable.Cols.UUID, crime.getID().toString());
        cv.put(CrimeTable.Cols.TITLE, crime.getTitle());
        cv.put(CrimeTable.Cols.DATE, crime.getDate().getTime());
        cv.put(CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0);
        return cv;
    }

    public void updateCrime(Crime crime) {
        String uuidString = crime.getID().toString();
        ContentValues cv = getContentValues(crime);
        mSQLiteDatabase.update(CrimeTable.NAME, cv, CrimeTable.Cols.UUID + " = ?", new String[]{uuidString});
    }

    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mSQLiteDatabase.query(CrimeTable.NAME, null, whereClause, whereArgs, null, null, null);
        return new CrimeCursorWrapper(cursor);
    }
}
