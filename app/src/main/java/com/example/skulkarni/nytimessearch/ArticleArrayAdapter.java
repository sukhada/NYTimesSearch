package com.example.skulkarni.nytimessearch;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.text.TextUtils;

import com.example.skulkarni.nytimessearch.activities.ArticleActivity;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by skulkarni on 8/13/16.
 */
public class ArticleArrayAdapter extends RecyclerView.Adapter<ArticleArrayAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView articleImage;
        TextView articleTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            articleImage = (ImageView) itemView.findViewById(R.id.ivImage);
            articleTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        }
    }

    private List<Article> mArticles;
    private Context mContext;

    public Context getmContext() {
        return mContext;
    }

    public ArticleArrayAdapter(Context context, List<Article> articles) {
        mContext = context;
        mArticles = articles;
    }

    @Override
    public ArticleArrayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View articleView = inflater.inflate(R.layout.item_article_result, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(articleView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ArticleArrayAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        final Article article = mArticles.get(position);

        // Set item views based on your views and data model
        TextView textView = viewHolder.articleTitle;
        textView.setText(article.getHeadline());
        ImageView imageView = viewHolder.articleImage;
        if (article.getThumbnail().length() > 0) {
            Picasso.with(getmContext()).load(article.getThumbnail()).into(imageView);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getmContext(), ArticleActivity.class);
                i.putExtra("article", article);
                getmContext().startActivity(i);
            }
        });
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    public void update(List<Article> articles) {
        mArticles = articles;
    }
}

