package nl.hasan.movieapp.adapter;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import nl.hasan.movieapp.fragments.MoviesFragment;
import nl.hasan.movieapp.fragments.ShowsFragment;

public class FragmentAdapter extends FragmentStateAdapter {


    public FragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new MoviesFragment();
            default:
                return new ShowsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
