package it.polito.mad.insane.lab2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by miche on 08/04/2016.
 */
public class DishesRecyclerAdapter extends RecyclerView.Adapter<DishesRecyclerAdapter.DishesViewHolder>
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
        View view = this.mInflater.inflate(R.layout.dish_list_item,parent, false); // create the view starting from XML layout file
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

    }

    @Override
    public int getItemCount()
    {
        return this.mData.size();
    }

    public class DishesViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView dishPhoto;
        private TextView dishID;
        private TextView dishName;
        private TextView dishDesc;
        private TextView dishPrice;
        private TextView dishAvailabQty;
        private int position;
        private Dish currentDish;


        public DishesViewHolder(View itemView)
        {
            super(itemView);
            this.dishPhoto = (ImageView) itemView.findViewById(R.id.dish_photo);
            this.dishID = (TextView) itemView.findViewById(R.id.dish_ID);
            this.dishName = (TextView) itemView.findViewById(R.id.dish_name);
            this.dishDesc =  (TextView) itemView.findViewById(R.id.disc_description);
            this.dishPrice = (TextView) itemView.findViewById(R.id.dish_price);
            this.dishAvailabQty = (TextView) itemView.findViewById(R.id.dish_availab_qty);
        }

        public void setData(Dish current, int position )
        {
            this.position = position;
            this.currentDish = current;
            //this.dishPhoto.setImageURI(current.getPhoto_name());




        }
    }
}
