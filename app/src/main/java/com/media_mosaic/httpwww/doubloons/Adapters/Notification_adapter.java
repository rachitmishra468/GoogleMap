package com.media_mosaic.httpwww.doubloons.Adapters;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.media_mosaic.httpwww.doubloons.Data_Model.Notification_model;
import com.media_mosaic.httpwww.doubloons.MyApplication;
import com.media_mosaic.httpwww.doubloons.R;
import java.util.List;
public class Notification_adapter  extends RecyclerView.Adapter<Notification_adapter.ProductViewHolder> {
    ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();
    private Context mCtx;
    private List<Notification_model> notification_models;
    public Notification_adapter(Context mCtx, List<Notification_model> productList) {
        this.mCtx = mCtx;
        this.notification_models = productList;
    }

    @Override
    public Notification_adapter.ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.notification_item, null);
        return new Notification_adapter.ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Notification_adapter.ProductViewHolder holder, int position) {
        imageLoader = MyApplication.getInstance().getImageLoader();
        Notification_model product = notification_models.get(position);
        holder.notification_title.setText(product.getTitle());
        holder.notification_dis.setText(product.getDescription());
        holder.notification_date.setText(product.getCreated());
    }


    @Override
    public int getItemCount() {
        int i=notification_models.size();
        return notification_models.size();
    }
    class ProductViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        TextView notification_title,notification_dis,notification_date;
        public ProductViewHolder(View itemView) {
            super(itemView);
            notification_title=itemView.findViewById(R.id.notification_title);
            notification_dis=itemView.findViewById(R.id.notification_dis);
            notification_date=itemView.findViewById(R.id.notification_date);
        }
    }
}