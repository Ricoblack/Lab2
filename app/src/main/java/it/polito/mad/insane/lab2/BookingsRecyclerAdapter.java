package it.polito.mad.insane.lab2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by miche on 06/04/2016.
 */
public class BookingsRecyclerAdapter extends RecyclerView.Adapter<BookingsRecyclerAdapter.BookingViewHolder>
{
    private List<Booking> mData; // actual data to be displayed
    private LayoutInflater mInflater;

    public BookingsRecyclerAdapter(Context context, List<Booking> data)
    {
        this.mData = data;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public BookingsRecyclerAdapter.BookingViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = mInflater.inflate(R.layout.booking_list_item, parent, false);
        BookingViewHolder holder = new BookingViewHolder(view);
        return  holder;
    }

    @Override
    public void onBindViewHolder(BookingsRecyclerAdapter.BookingViewHolder holder, int position)
    {
        Booking currentObj = mData.get(position);
        holder.setData(currentObj,position);
    }

    @Override
    public int getItemCount()
    {
        return this.mData.size();
    }


    /* Our Holder Class */
    class BookingViewHolder extends RecyclerView.ViewHolder
    {
        // attributi della card view
        int position;
        Booking currentBooking;
        TextView book_id;

        public BookingViewHolder(View itemView)
        {
            super(itemView);
            // bisogna riempire gli attributi della cardView
            this.book_id = (TextView) itemView.findViewById(R.id.title_card_pren);

        }

        public void setData(Booking current, int position)
        {
            this.book_id.setText(current.getBook_id());
            this.position = position;
            this.currentBooking = current;

        }


    }
}
