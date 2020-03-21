package net.gogetout.go;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;


public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    List<Events> data = Collections.emptyList();
    private Context context;
    private ClickListener clickListener;
    public EventsAdapter(Context context, List<Events> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    public void delete(int position) {

        data.remove(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_ev_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Events current = data.get(position);
        holder.title.setText(current.getTitle());
        holder.date.setText(current.getDate());
        holder.time.setText(current.getTime());
        holder.location.setText(current.getLocation());
        //holder.evImage.setImageResource(current.getImageURL());
        Picasso.with(context).load(current.getImageURL()).into(holder.evImage);
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setClickListener(ClickListener clickListener){
        this.clickListener = clickListener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title,description,date,time,location;
        ImageView evImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            title = (TextView) itemView.findViewById(R.id.title);
            //description=(TextView) itemView.findViewById(R.id.description);
            date=(TextView) itemView.findViewById(R.id.date);
            time=(TextView) itemView.findViewById(R.id.time);
            location=(TextView) itemView.findViewById(R.id.location);
            evImage = (ImageView) itemView.findViewById(R.id.circlerUserImage);
            /*icon.setOnClickListener(this);*/
        }



        @Override
        public void onClick(View v) {

            if(clickListener!=null){
                clickListener.itemClicked(v,getPosition());
            }

        }
    }


    public interface ClickListener{
        public void itemClicked(View view, int position);
    }
}
