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

import java.io.Serializable;
import java.util.List;

/**
 * Created by miche on 08/04/2016.
 */
public class DishesRecyclerAdapter extends RecyclerView.Adapter<DishesRecyclerAdapter.DishesViewHolder> implements Serializable
{
    private List<Dish> mData; // actual data to be displayed
    private LayoutInflater mInflater;



    public DishesRecyclerAdapter(Context context, List<Dish> data)
    {
        this.mData = data;
        this.mInflater = LayoutInflater.from(context);
    }



    /**
     * Method called when a ViewHolder Object is created
     * The returned object contains a reference to a view representing the bare structure of the item
     * @param parent
     * @param viewType
     */
    @Override
    public DishesViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        // create the view starting from XML layout file
        View view = this.mInflater.inflate(R.layout.dish_list_item,parent, false);

        DishesViewHolder result = new DishesViewHolder(view); // create the holder
        return result;
    }

    /**
     * Method called after onCreateViewHolder() and it fetches data from the model and properly sets view accordingly
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(DishesViewHolder holder, int position)
    {
        Dish currentObj = this.mData.get(position);
        holder.setData(currentObj, position);
    }

    @Override
    public int getItemCount()
    {
        return this.mData.size();
    }

    public class DishesViewHolder extends RecyclerView.ViewHolder
    {
        private View cardView;
        private ImageView dishPhoto;
        private TextView dishID;
        private TextView dishName;
        private TextView dishDesc;
        private TextView dishPrice;
        private TextView dishAvailabQty;
        private int position;
        private Dish currentDish;

        private android.view.View.OnClickListener cardViewListener = new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                Toast.makeText(v.getContext(),"Cliccato sulla cardView avente dishID: "+currentDish.getID(), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(v.getContext(),EditDish.class);
                i.putExtra("Dish", DishesViewHolder.this.currentDish);
//                i.putExtra("Adapter", DishesRecyclerAdapter.this);
                v.getContext().startActivity(i);
            }
        };

        public DishesViewHolder(View itemView)
        {
            super(itemView);
            this.cardView = itemView;
            this.dishPhoto = (ImageView) itemView.findViewById(R.id.dish_photo);
            this.dishID = (TextView) itemView.findViewById(R.id.dish_ID);
            this.dishName = (TextView) itemView.findViewById(R.id.dish_name);
            this.dishDesc =  (TextView) itemView.findViewById(R.id.dish_description);
            this.dishPrice = (TextView) itemView.findViewById(R.id.dish_price);
            this.dishAvailabQty = (TextView) itemView.findViewById(R.id.dish_availab_qty);

            // set the onClickListener to the View
            this.cardView.setOnClickListener(cardViewListener);
        }

        public void setData(Dish current, int position )
        {
            this.dishID.setText(current.getID());
            this.dishName.setText(current.getName());
            this.dishDesc.setText(current.getDescription());
            this.dishPrice.setText(Double.toString(current.getPrice()));
            this.dishAvailabQty.setText(Integer.toString(current.getAvailability_qty()));
            //TODO: this.dishPhoto.setImageURI(current.getPhoto_name());
            this.position = position;
            this.currentDish = current;

        }

        public int getPos() {
            return position;
        }

        public void setPos(int position) {
            this.position = position;
        }

        public Dish getCurrentDish() {
            return currentDish;
        }

        public void setCurrentDish(Dish currentDish) {
            this.currentDish = currentDish;
        }


    }
}
