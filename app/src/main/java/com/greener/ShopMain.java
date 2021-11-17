package com.greener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ShopMain extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<ShopList> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        System.out.println("Shop Main changed");

        Button button1 = view.findViewById(R.id.search_button);
        Button button2 = view.findViewById(R.id.map_view);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.shop_main_frame, new ShopMap()).commit();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchMain.class);
                startActivity(intent);
            }
        });

        view = inflater.inflate(R.layout.shop_main, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.shop_recyclerView);
        recyclerView.setHasFixedSize(true);     //리사이클러뷰 성능 강화

        layoutManager = new GridLayoutManager(getContext(), 2);

        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();

        adapter = new ShopViewAdapter(arrayList, getContext());
        recyclerView.setAdapter(adapter);

        return view;
    }

}