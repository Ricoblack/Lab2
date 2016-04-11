package it.polito.mad.insane.lab2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
        private View cardView;
        private TextView bookingID;
        private TextView bookingTime;
        private TextView bookingDishNum;
        private TextView bookingNote;
        private int position;
        private Booking currentBooking;
        private ImageView imageView;

        private android.view.View.OnClickListener cardViewListener = new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                Toast.makeText(v.getContext(),"Cliccato sulla cardView", Toast.LENGTH_LONG).show();
                Intent i = new Intent(v.getContext(),ViewBooking.class);
                i.putExtra("Booking", BookingViewHolder.this.currentBooking);
                i.putExtra("pos",position);
                v.getContext().startActivity(i);
            }
        };

        private android.view.View.OnClickListener imageViewListener = new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                //Toast.makeText(v.getContext(),"La posizione è "+Integer.toString(position), Toast.LENGTH_LONG).show();
                Intent i = new Intent(v.getContext(),HomeRestaurateur.class);
                i.putExtra("pos",position);
                v.getContext().startActivity(i);
            }
        };


        public BookingViewHolder(View itemView)
        {
            super(itemView);
            this.cardView = itemView;
            this.imageView = (ImageView) itemView.findViewById(R.id.delete_booking_button);
            this.bookingID = (TextView) itemView.findViewById(R.id.title_card_pren);
            this.bookingTime = (TextView) itemView.findViewById(R.id.hour);
            this.bookingDishNum = (TextView) itemView.findViewById(R.id.num_booking);
            //this.bookingNote = (TextView) itemView.findViewById(R.id.note_booking);

            // set the onClickListener to the View
            this.cardView.setOnClickListener(cardViewListener);

            //set the onClickListner to the ImgView
            this.imageView.setOnClickListener(imageViewListener);

        }

        public void setData(Booking current, int position)
        {
            this.bookingID.setText(current.getID());
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
            String date = dateFormat.format(current.getDate_time().getTime());
            this.bookingTime.setText(date);
            this.bookingDishNum.setText(Integer.toString(current.getDishes().size()));
            //this.bookingNote.setText(current.getNote());
            this.position = position;
            this.currentBooking = current;

        }


        public int getPos() {
            return position;
        }

        public void setPos(int position) {
            this.position = position;
        }

        public Booking getCurrentBooking() {
            return currentBooking;
        }

        public void setCurrentBooking(Booking currentBooking) {
            this.currentBooking = currentBooking;
        }
    }
}
