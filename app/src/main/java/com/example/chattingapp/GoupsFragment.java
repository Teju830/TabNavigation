package com.example.chattingapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GoupsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GoupsFragment extends Fragment {
        private ListView listView;
        private DatabaseReference groupRef;
        private View groupFragView;
        private ArrayAdapter<String>arrayAdapter;
        private ArrayList<String>arrayList=new ArrayList<>();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GoupsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GoupsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GoupsFragment newInstance(String param1, String param2) {
        GoupsFragment fragment = new GoupsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        groupFragView= inflater.inflate(R.layout.fragment_goups, container, false);
      groupRef= FirebaseDatabase.getInstance().getReference().child("Group");
      initialiseFields();
      RetrieveAndDisplayGroups();
      listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

          @Override
          public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              String currentGroupname=adapterView.getItemAtPosition(i).toString();
              Intent groupchtaIntent=new Intent(getContext(),GroupChatActivity.class);
              groupchtaIntent.putExtra("groupName",currentGroupname);
              startActivity(groupchtaIntent);

          }
      });
        return groupFragView;
    }

    private void RetrieveAndDisplayGroups() {

    groupRef.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            Set<String>set=new HashSet<>();
            Iterator iterator=snapshot.getChildren().iterator();
     while (iterator.hasNext()){
         set.add(((DataSnapshot)iterator.next()).getKey());
     }
     arrayList.clear();
     arrayList.addAll(set);
     arrayAdapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {


        }
    });

    }

    private void initialiseFields() {
    listView=(ListView)groupFragView.findViewById(R.id.list_view);
    arrayAdapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,arrayList);
    listView.setAdapter(arrayAdapter);

    }
}