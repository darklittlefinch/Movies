package com.elliemoritz.movies.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.elliemoritz.movies.dao.MovieDao;
import com.elliemoritz.movies.database.MovieDatabase;
import com.elliemoritz.movies.pojo.movie.Movie;

import java.util.List;

public class FavouriteMoviesViewModel extends AndroidViewModel {

    private final MovieDao movieDao;

    public FavouriteMoviesViewModel(@NonNull Application application) {
        super(application);
        movieDao = MovieDatabase.getInstance(application).movieDao();
    }

    public LiveData<List<Movie>> getMovies() {
        return movieDao.getAllFavouriteMovies();
    }
}
