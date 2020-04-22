package com.mybclym.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.List;

public class CrimeListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private List<Crime> mCrimes;
    private CrimeAdapter adapter;
    private TextView empty_text;

    private static final int POLICE_VIEWTYPE = 1;
    private boolean mSubtitle_visible;
    private static final String SAVED_SUBTITLE_VISIBLE = "Subtitle visible";

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitle_visible);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TEST", "Fragment CrimeList onCreate");
        mCrimes = CrimeLab.get(getActivity()).getCrimes();
        setHasOptionsMenu(true);
        updateSubtitle();
        // if (adapter != null) adapter.setCrimes(mCrimes);

    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
        Log.d("TEST", "Fragment CrimeList onResume");
        adapter.notifyDataSetChanged();
        updateSubtitle();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        Log.d("TEST", "onCreateOptionMenu");
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);
        MenuItem subtitle_item = menu.findItem(R.id.menu_show_subtitle);
        if (mSubtitle_visible) subtitle_item.setTitle(R.string.hide_subtitle);
        else subtitle_item.setTitle(R.string.show_subtitle);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_new_crime:
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getID(), 0);
                startActivity(intent);
                return true;
            case R.id.menu_show_subtitle:
                mSubtitle_visible = !mSubtitle_visible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void updateSubtitle() {
        Log.d("TEST", "updateSubtitle");
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        int crimeCount = crimeLab.getCrimes().size();
        String subtitle = getResources().getQuantityString(R.plurals.numberOfCrimes, crimeCount, crimeCount);
        if (!mSubtitle_visible) subtitle = null;
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("TEST", "Fragment CrimeList onCreateView");
        if (savedInstanceState != null) {
            mSubtitle_visible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }
        View v;
        v = inflater.inflate(R.layout.fragment_crimelist, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerview_crimesList);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return v;
    }

    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();
        if (adapter == null) {
            adapter = new CrimeAdapter(crimes);
            mRecyclerView.setAdapter(adapter);
        } else {
            adapter.setCrimes(crimes);
            adapter.notifyDataSetChanged();
        }
        updateSubtitle();
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeAdapter.CrimeViewHolder> {
        List<Crime> mCrimeList;
        int mCrime_position;

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

        public void setCrimes(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @Override
        public int getItemViewType(int position) {
            if (mCrimeList.get(position).isPoliceRequires()) return POLICE_VIEWTYPE;
            return 0;
        }

        private class CrimeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private ImageView image_isSolved;
            private TextView textview_title, textview_date;
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
                Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getID(), getAdapterPosition());
                startActivity(intent);
            }
        }

        public void replaceData(List<Crime> newCrimes) {
            DiffUtil.DiffResult diff = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return 0;
                }

                @Override
                public int getNewListSize() {
                    return 0;
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return false;
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return false;
                }
            });
        }
    }


}
