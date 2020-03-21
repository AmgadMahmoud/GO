package net.gogetout.go;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edwin on 15/02/2015.
 */
public class EventGuestsFragment extends Fragment {

    private RecyclerView RV;
    private PeopleAdapter peopleAdapter;
    List<PeopleElements> peopleList;
    
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_event_guests,container,false);

        peopleList = new ArrayList<>();
        peopleList = getData();


        RV = (RecyclerView) v.findViewById(R.id.friendsList);
            /*LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);


            RV.setLayoutManager(mLayoutManager);
*/
        RV.addItemDecoration(new SlidingTabStrip.SimpleDividerItemDecoration(
                getActivity()
        ));
        peopleAdapter = new PeopleAdapter(getActivity(), peopleList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        RV.setLayoutManager(layoutManager);
        RV.setAdapter(peopleAdapter);


        return v;
    }


    public static List<PeopleElements> getData() {

        List<PeopleElements> data = new ArrayList<>();
        String[] names = {"Girl", "Boy", "Guy", "Man","Girl", "Boy", "Guy", "Man","Girl", "Boy", "Guy", "Man","Girl", "Boy", "Guy", "Man"};
        String[] images = {"Girl", "Boy", "Guy", "Man","Girl", "Boy", "Guy", "Man","Girl", "Boy", "Guy", "Man","Girl", "Boy", "Guy", "Man"};
        for (int i = 0; i < names.length && i < images.length; i++) {
            PeopleElements current = new PeopleElements();
            current.name = names[i];
            current.imageURL = images[i];
            data.add(current);
        }
        return data;
    }

}