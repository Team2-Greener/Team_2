package com.greener;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class StoreDetailAdapter extends FragmentStateAdapter {

    public int Count;

    public StoreDetailAdapter(FragmentActivity fa, int count) {
        super(fa);
        Count = count;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int index = getRealPosition(position);

        if(index==0) return new ViewPage1();
        else if(index==1) return new ViewPage2();
        else if(index==2) return new ViewPage3();
        else return new ViewPage4();
    }

    @Override
    public int getItemCount() {
        return 2000;
    }

    public int getRealPosition(int position) {
        return position;
    }

}