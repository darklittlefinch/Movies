package com.elliemoritz.movies.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.elliemoritz.movies.R;
import com.elliemoritz.movies.pojo.review.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {

    private static final String TYPE_POSITIVE = "Позитивный";
    private static final String TYPE_NEGATIVE = "Негативный";
    private static final String TYPE_NEUTRAL = "Нейтральный";

    private List<Review> reviews = new ArrayList<>();

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.review_item,
                parent,
                false
        );
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviews.get(position);

        holder.textViewAuthorName.setText(review.getAuthor());
        holder.textViewReview.setText(review.getReview());

        int backgroundColor = getBackgroundColor(holder.itemView, review.getType());
        holder.linearLayoutReview.setBackgroundColor(backgroundColor);
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    private int getBackgroundColor(View view, String type) {
        int backgroundId;

        if (type.equals(TYPE_POSITIVE)) {
            backgroundId = R.color.green;
        } else if (type.equals(TYPE_NEGATIVE)) {
            backgroundId = R.color.red;
        } else if (type.equals(TYPE_NEUTRAL)) {
            backgroundId = R.color.yellow;
        } else {
            backgroundId = R.color.graphite_light;
        }

        return ContextCompat.getColor(view.getContext(), backgroundId);
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewAuthorName;
        private final TextView textViewReview;
        private final LinearLayout linearLayoutReview;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAuthorName = itemView.findViewById(R.id.textViewAuthorName);
            textViewReview = itemView.findViewById(R.id.textViewReview);
            linearLayoutReview = itemView.findViewById(R.id.linearLayoutReview);
        }
    }
}
