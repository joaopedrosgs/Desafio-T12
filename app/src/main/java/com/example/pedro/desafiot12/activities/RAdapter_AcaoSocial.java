package com.example.pedro.desafiot12.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pedro.desafiot12.R;
import com.example.pedro.desafiot12.entities.AcaoSocial;
import com.example.pedro.desafiot12.entities.AcoesListEntity;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.BlurTransformation;
import jp.wasabeef.picasso.transformations.gpu.BrightnessFilterTransformation;
import jp.wasabeef.picasso.transformations.gpu.VignetteFilterTransformation;
import shivam.developer.featuredrecyclerview.FeatureRecyclerViewAdapter;

public class RAdapter_AcaoSocial extends FeatureRecyclerViewAdapter<RAdapter_AcaoSocial.CustomRecyclerViewHolder> {

    private List<AcaoSocial> dataList;
    private Context context;

    public RAdapter_AcaoSocial(Context context, AcoesListEntity list) {
        this.dataList = list.getAcoesSociais();
        this.context = context;
    }

    @Override
    public CustomRecyclerViewHolder onCreateFeaturedViewHolder(ViewGroup parent, int viewType) {
        return new CustomRecyclerViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.node_layout, parent, false));
    }

    @Override
    public void onBindFeaturedViewHolder(CustomRecyclerViewHolder holder, final int position) {

        Picasso.with(context)
                .load(dataList.get(position).getImage())
                .transform(new BlurTransformation(context, 6,1))
                .transform(new VignetteFilterTransformation(context, new PointF(0.5f, 0.5f),
                        new float[] { 0.0f, 0.0f, 0.0f }, 0f, 1f))
                .transform(new BrightnessFilterTransformation(context, -0.15f))
                .centerCrop()
                .fit()
                .into(holder.ivBackground);
            // Essa porra toda daqui de cima é pra baixar a imagem, colocar um vignette e dar blur
        holder.tvHeading.setText(dataList.get(position).getName());
        holder.tvDescription.setText(dataList.get(position).getDescription());

        // Aqui setamos o OnClick pra abrir o link que tem no json
        holder.ivBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String link = dataList.get(position).getSite();
                if (link!=null || !link.isEmpty()) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(dataList.get(position).getSite()));
                    context.startActivity(i);
                }

            }
        });
        // Aqui setamos o OnLongClick pra compartilhar o link que tem no json
        holder.ivBackground.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view) {
                String link = dataList.get(position).getSite();
                if (link!=null || !link.isEmpty()){
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "Oi, da uma olhada nessa Ação social aqui, achei ela bem legal: " + link);
                    sendIntent.setType("text/plain");
                    context.startActivity(sendIntent);
                }
                return false;
            }
        });
    }

    @Override
    public int getFeaturedItemsCount() {
        return dataList.size();
    }

    @Override
    public void onSmallItemResize(CustomRecyclerViewHolder holder, int position, float offset) {
        if(position != getFeaturedItemsCount()) {
            holder.tvHeading.setAlpha(offset / 100f);
            holder.tvDescription.setAlpha(offset / 100f);

        }
    }

    @Override
    public void onBigItemResize(CustomRecyclerViewHolder holder, int position, float offset) {
        if(position != getFeaturedItemsCount()) {
            holder.tvHeading.setAlpha(offset / 100f);
            holder.tvDescription.setAlpha(offset / 100f);
        }

    }

    public static class CustomRecyclerViewHolder extends RecyclerView.ViewHolder {

        ImageView ivBackground;
        TextView tvHeading;
        TextView tvDescription;


        public CustomRecyclerViewHolder(View itemView) {
            super(itemView);

            ivBackground = (ImageView) itemView.findViewById(R.id.iv_background);
            tvHeading = (TextView) itemView.findViewById(R.id.tv_heading);
            tvDescription = (TextView) itemView.findViewById(R.id.tv_description);
        }
    }
}
