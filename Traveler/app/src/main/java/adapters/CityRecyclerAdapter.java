package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.traveler.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import listeners.OnCityClickedListener;
import model.City;

public class CityRecyclerAdapter extends RecyclerView.Adapter<CityRecyclerAdapter.ViewHolder> {

    private Context context;
    private final List<City> cities;
    private final OnCityClickedListener onCityClickedListener;


    public CityRecyclerAdapter(Context context, List<City> cities, OnCityClickedListener cityClickedListener) {
        this.context = context;
        this.cities = cities;
        this.onCityClickedListener = cityClickedListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.city_row, parent, false);


        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        City city = cities.get(position);

        String imageUrl;

        holder.cityName.setText(city.getName());
        holder.memories.setText(city.getMemories());
        imageUrl = city.getImageUrls().get(0);

            Picasso.get().load(imageUrl)
                    .placeholder(android.R.drawable.stat_sys_download)
                    .error(R.drawable.postcity)
                    .into(holder.cityImage);






    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView cityName;
        public TextView memories;
        public ImageView cityImage;
        OnCityClickedListener onCityClickedListener;


        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);

            context = ctx;

            cityName = itemView.findViewById(R.id.cityNameTvRow);
            memories = itemView.findViewById(R.id.memoriesTvRow);
            cityImage = itemView.findViewById(R.id.cityImageRow);
            this.onCityClickedListener = CityRecyclerAdapter.this.onCityClickedListener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            City currentCity = cities.get(getAdapterPosition());
            onCityClickedListener.onCityClicked(currentCity);
        }
    }
}
