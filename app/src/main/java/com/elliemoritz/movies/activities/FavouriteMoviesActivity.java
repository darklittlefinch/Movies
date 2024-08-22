package com.elliemoritz.movies.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.elliemoritz.movies.R;
import com.elliemoritz.movies.adapters.MoviesAdapter;
import com.elliemoritz.movies.pojo.movie.Movie;
import com.elliemoritz.movies.viewModels.FavouriteMoviesViewModel;

public class FavouriteMoviesActivity extends AppCompatActivity {
    private FavouriteMoviesViewModel viewModel;
    private MoviesAdapter moviesAdapter;
    private RecyclerView recyclerViewFavouriteMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_movies);

        initViewModel();
        initAdapter();
        initViews();

        setMoviesLoading();
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(FavouriteMoviesViewModel.class);
    }

    private void initAdapter() {
        moviesAdapter = new MoviesAdapter();

        moviesAdapter.setOnMovieClickListener(this::toMovieDetailActivity);
    }

    private void initViews() {
        recyclerViewFavouriteMovies = findViewById(R.id.recyclerViewFavouriteMovies);
        recyclerViewFavouriteMovies.setAdapter(moviesAdapter);
        recyclerViewFavouriteMovies.setLayoutManager(new GridLayoutManager(this, 2));
    }

    private void setMoviesLoading() {
        viewModel.getMovies().observe(this, movies -> moviesAdapter.setMovies(movies));
    }

    private void toMovieDetailActivity(Movie movie) {
        Intent intent = MovieDetailActivity.newIntent(this, movie);
        startActivity(intent);
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, FavouriteMoviesActivity.class);
    }
}