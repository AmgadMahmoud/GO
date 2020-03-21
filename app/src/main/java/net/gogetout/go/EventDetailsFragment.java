package net.gogetout.go;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Edwin on 15/02/2015.
 */
public class EventDetailsFragment extends Fragment {

    TextView dateView, timeView, locationView, descriptionView,
            uploaderView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_event_details,container,false);

        dateView = (TextView) v.findViewById(R.id.ev_date);
        timeView = (TextView) v.findViewById(R.id.ev_time);
        locationView = (TextView) v.findViewById(R.id.ev_location);
        descriptionView = (TextView) v.findViewById(R.id.ev_description);
        uploaderView = (TextView) v.findViewById(R.id.personName);
        //goButton = (Button) v.findViewById(R.id.going);


        return v;
    }
}
