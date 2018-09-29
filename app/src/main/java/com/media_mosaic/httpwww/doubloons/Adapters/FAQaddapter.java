package com.media_mosaic.httpwww.doubloons.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.media_mosaic.httpwww.doubloons.Data_Model.FAQ_model;
import com.media_mosaic.httpwww.doubloons.R;

import java.util.List;


public class FAQaddapter extends RecyclerView.Adapter<FAQaddapter.ProductViewHolder> {
    private Context mCtx;
    private List<FAQ_model> productList;
    boolean flag=true;
    public FAQaddapter(Context mCtx, List<FAQ_model> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    public FAQaddapter.ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.faq_item, null);
        return new FAQaddapter.ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder( final  FAQaddapter.ProductViewHolder holder, int position) {

         FAQ_model product = productList.get(position);
         holder.textViewTitle.setText(product.getTitle());
         holder.textViewShortDesc.setText(Html.fromHtml(product.getShortdesc()));
         holder.show_faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag){
                    flag=false;
                    holder.show_hide_data.setImageResource(R.drawable.drop_up);
                    holder.faq_data.setVisibility(View.VISIBLE);
                }
                else {
                    flag=true;
                    holder.show_hide_data.setImageResource(R.drawable.drop_down);
                    holder.faq_data.setVisibility(View.GONE);
                }

            }
        });


    }


    @Override
    public int getItemCount() {
        return productList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle, textViewShortDesc, textViewRating, textViewPrice;
        ImageView show_hide_data;
        LinearLayout faq_data,show_faq;
        public ProductViewHolder(View itemView) {
            super(itemView);
            textViewShortDesc= itemView.findViewById(R.id.textViewShortDesc);
            textViewTitle= itemView.findViewById(R.id.textViewTitle);
            show_hide_data= itemView.findViewById(R.id.show_hide_data);
            faq_data=itemView.findViewById(R.id.faq_data);
            show_faq=itemView.findViewById(R.id.show_faq);

        }
    }
}