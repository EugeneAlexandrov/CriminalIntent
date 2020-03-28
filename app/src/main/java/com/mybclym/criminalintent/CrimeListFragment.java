package com.mybclym.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.List;

public class CrimeListFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private List<Crime> mCrimes;
    private CrimeAdapter adapter;
    private static final int POLICE_VIEWTYPE = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TEST", "Fragment onCreate");
        mCrimes = CrimeLab.get().getCrimes();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v;
        v = inflater.inflate(R.layout.fragment_crimelist, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerview_crimesList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (adapter == null) adapter = new CrimeAdapter(mCrimes);
        else ;
        mRecyclerView.setAdapter(adapter);
        return v;
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeAdapter.CrimeViewHolder> {
        List<Crime> mCrimeList;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimeList = crimes;
        }

        @NonNull
        @Override
        public CrimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getActivity()).inflate((viewType == POLICE_VIEWTYPE ? R.layout.recyclerview_item_policerequires : R.layout.recyclerview_item), parent, false);
            return new CrimeViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull CrimeViewHolder holder, int position) {
            Crime crime = mCrimeList.get(position);
            holder.bind(crime);
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (mCrimes.get(position).isPoliceRequires()) return POLICE_VIEWTYPE;
            return 0;
        }

        private class CrimeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            ImageView image_isSolved;
            TextView textview_title, textview_date;
            private Crime mCrime;

            public CrimeViewHolder(@NonNull View itemView) {
                super(itemView);
                image_isSolved = (ImageView) itemView.findViewById(R.id.recyclerview_item_isSolved_image);
                textview_title = (TextView) itemView.findViewById(R.id.recyclerview_item_title_textview);
                textview_date = (TextView) itemView.findViewById(R.id.recyclerview_item_date_textview);
                itemView.setOnClickListener(this);
            }

            private void bind(Crime crime) {
                mCrime = crime;
                textview_date.setText(DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM).format(mCrime.getDate()));
                textview_title.setText(mCrime.getTitle());
                image_isSolved.setVisibility(mCrime.isSolved() ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onClick(View v) {
                Intent intent = CrimeActivity.newIntent(getActivity(), mCrime.getID());
                startActivity(intent);
            }
        }
    }


}
