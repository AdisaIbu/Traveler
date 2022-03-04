package adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.traveler.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ViewPagerPostImageAdapter extends RecyclerView.Adapter<ViewPagerPostImageAdapter.ImageSlider> {

    List <Uri> uriList;
    private Context context;

    public ViewPagerPostImageAdapter(Context context, List<Uri> uriList) {
        this.context=context;
        this.uriList = uriList;
    }

    @NonNull
    @Override
    public ImageSlider onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_pager_city, parent, false);
        return new ImageSlider(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageSlider holder, int position) {

     //   holder.imageView.setImageURI(uriList.get(position));

        Picasso.get().load(uriList.get(position))
                .placeholder(android.R.drawable.stat_sys_download)
                .error(R.drawable.postcity)
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        Log.d("uriList", "getItemCount: " + uriList.size());
        return uriList.size();
    }

    public class ImageSlider extends RecyclerView.ViewHolder{
        public ImageView imageView;


        public ImageSlider(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.view_pager_imageview);
        }
    }
}
