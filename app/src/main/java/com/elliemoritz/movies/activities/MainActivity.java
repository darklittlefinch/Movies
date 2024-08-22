package com.elliemoritz.movies.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.elliemoritz.movies.adapters.MoviesAdapter;
import com.elliemoritz.movies.R;
import com.elliemoritz.movies.pojo.movie.Movie;
import com.elliemoritz.movies.viewModels.MainViewModel;

public class MainActivity extends AppCompatActivity {

    private MainViewModel viewModel;
    private MoviesAdapter moviesAdapter;
    private RecyclerView recyclerViewMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViewModel();
        initAdapter();
        initViews();

        setMoviesLoading();
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
    }

    private void initAdapter() {
        moviesAdapter = new MoviesAdapter();
        moviesAdapter.setOnReachEndListener(() -> viewModel.loadMovies());
        moviesAdapter.setOnMovieClickListener((movie) -> toMovieDetailActivity(movie));
    }

    private void initViews() {
        recyclerViewMovies = findViewById(R.id.recyclerViewMovies);
        recyclerViewMovies.setAdapter(moviesAdapter);
        recyclerViewMovies.setLayoutManager(new GridLayoutManager(this, 2));
    }

    private void setMoviesLoading() {
        viewModel.getMovies().observe(this, movies -> moviesAdapter.setMovies(movies));
    }

    private void toMovieDetailActivity(Movie movie) {
        Intent intent = MovieDetailActivity.newIntent(this, movie);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.itemFavourites) {
            Intent intent = FavouriteMoviesActivity.newIntent(this);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}