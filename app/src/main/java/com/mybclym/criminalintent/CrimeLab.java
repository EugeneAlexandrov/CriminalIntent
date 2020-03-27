package com.mybclym.criminalintent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CrimeLab {
    private static CrimeLab sCrimeLab;
    private List<Crime> mCrimeList;
    private Map<UUID, Crime> mHashMapCrimes;

    private CrimeLab() {
        mCrimeList = new ArrayList<>();
        mHashMapCrimes = new HashMap<>();
        for (int i = 0; i < 30; i++) {
            Crime mCrime = new Crime();
            mCrime.setTitle("Crime #" + i + "id: " + mCrime.getID());
            mCrime.setSolved(i % 3 == 0);
            mCrimeList.add(mCrime);
            mHashMapCrimes.put(mCrime.getID(), mCrime);
        }
    }

    public static CrimeLab get() {
        if (sCrimeLab == null) return new CrimeLab();
        return sCrimeLab;
    }

    public List<Crime> getCrimes() {
        return mCrimeList;
    }

    public Crime getCrime(UUID id) {
        return mHashMapCrimes.get(id);
    }
}
