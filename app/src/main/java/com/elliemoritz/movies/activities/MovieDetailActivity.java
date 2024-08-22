package com.elliemoritz.movies.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.elliemoritz.movies.R;
import com.elliemoritz.movies.adapters.ReviewsAdapter;
import com.elliemoritz.movies.adapters.TrailersAdapter;
import com.elliemoritz.movies.dao.MovieDao;
import com.elliemoritz.movies.database.MovieDatabase;
import com.elliemoritz.movies.pojo.movie.Movie;
import com.elliemoritz.movies.pojo.movie.Poster;
import com.elliemoritz.movies.viewModels.MovieDetailViewModel;

import io.reactivex.rxjava3.schedulers.Schedulers;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String EXTRA_MOVIE = "movie";

    private MovieDetailViewModel viewModel;

    private TrailersAdapter trailersAdapter;
    private ReviewsAdapter reviewsAdapter;

    private ImageView imageViewDetailPoster;
    private TextView textViewTitle;
    private TextView textViewYear;
    private TextView textViewDescription;
    private RecyclerView recyclerViewTrailers;
    private RecyclerView recyclerViewReviews;
    private ImageView imageViewStar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        initViewModel();
        initAdapters();
        initViews();

        Movie movie = (Movie) getIntent().getSerializableExtra(EXTRA_MOVIE);
        setMovieAttributes(movie);
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(MovieDetailViewModel.class);
    }

    private void initAdapters() {
        trailersAdapter = new TrailersAdapter();
        trailersAdapter.setOnTrailerClickListener(trailer -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(trailer.getUrl()));
            startActivity(intent);
        });

        reviewsAdapter = new ReviewsAdapter();
    }

    private void initViews() {
        imageViewDetailPoster = findViewById(R.id.imageViewDetailPoster);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewYear = findViewById(R.id.textViewYear);
        textViewDescription = findViewById(R.id.textViewDescription);
        imageViewStar = findViewById(R.id.imageViewStar);

        recyclerViewTrailers = findViewById(R.id.recyclerViewTrailers);
        recyclerViewTrailers.setAdapter(trailersAdapter);
        recyclerViewTrailers.setLayoutManager(new LinearLayoutManager(this));

        recyclerViewReviews = findViewById(R.id.recyclerViewReviews);
        recyclerViewReviews.setAdapter(reviewsAdapter);
        recyclerViewReviews.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setMovieAttributes(Movie movie) {
        setPoster(movie.getPoster());
        textViewTitle.setText(movie.getName());
        textViewYear.setText(String.valueOf(movie.getYear()));
        textViewDescription.setText(movie.getDescription());
        setTrailers(movie.getId());
        setReviews(movie.getId());
        setFavouriteMovie(movie);
    }

    private void setPoster(Poster poster) {
        Glide.with(this)
                .load(poster.getUrl())
                .into(imageViewDetailPoster);
    }

    private void setTrailers(int movieId) {
        viewModel.loadTrailers(movieId);
        viewModel.getTrailers().observe(
                this,
                trailers -> trailersAdapter.setTrailers(trailers)
        );
    }

    private void setReviews(int movieId) {
        viewModel.loadReviews(movieId);
        viewModel.getReviews().observe(
                this,
                reviews -> reviewsAdapter.setReviews(reviews)
        );
    }

    private void setFavouriteMovie(Movie movie) {
        Drawable starOff = ContextCompat.getDrawable(this, android.R.drawable.star_big_off);
        Drawable starOn = ContextCompat.getDrawable(this, android.R.drawable.star_big_on);

        viewModel.getFavouriteMovie(movie.getId()).observe(this, movieFromDb -> {
            if (movieFromDb == null) {
                imageViewStar.setImageDrawable(starOff);
                imageViewStar.setOnClickListener(view -> {
                    viewModel.insertMovie(movie);
                    Toast.makeText(
                            this,
                            getString(R.string.addFavouriteMovie, movie.getName()),
                            Toast.LENGTH_SHORT
                    ).show();
                });
            } else {
                imageViewStar.setImageDrawable(starOn);
                imageViewStar.setOnClickListener(view -> {
                    viewModel.removeMovie(movie.getId());
                    Toast.makeText(
                            this,
                            getString(R.string.removeFavouriteMovie, movie.getName()),
                            Toast.LENGTH_SHORT
                    ).show();
                });
            }
        });
    }

    public static Intent newIntent(Context context, Movie movie) {
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra(EXTRA_MOVIE, movie);
        return intent;
    }
}