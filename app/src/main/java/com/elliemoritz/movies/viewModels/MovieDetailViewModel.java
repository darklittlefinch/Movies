package com.elliemoritz.movies.viewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.elliemoritz.movies.api.ApiFactory;
import com.elliemoritz.movies.dao.MovieDao;
import com.elliemoritz.movies.database.MovieDatabase;
import com.elliemoritz.movies.pojo.movie.Movie;
import com.elliemoritz.movies.pojo.review.Review;
import com.elliemoritz.movies.pojo.review.ReviewResponse;
import com.elliemoritz.movies.pojo.trailer.Trailer;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MovieDetailViewModel extends AndroidViewModel {

    private static final String TAG = "MovieDetailViewModel";
    private final MutableLiveData<List<Trailer>> trailers = new MutableLiveData<>();
    private final MutableLiveData<List<Review>> reviews = new MutableLiveData<>();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final MovieDao movieDao;

    public MovieDetailViewModel(@NonNull Application application) {
        super(application);
        movieDao = MovieDatabase.getInstance(application).movieDao();
    }

    public LiveData<List<Trailer>> getTrailers() {
        return trailers;
    }

    public LiveData<List<Review>> getReviews() {
        return reviews;
    }

    public LiveData<Movie> getFavouriteMovie(int movieId) {
        return movieDao.getFavouriteMovie(movieId);
    }

    public void loadTrailers(int movieId) {
        Disposable disposable = ApiFactory.API_SERVICE.loadTrailers(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(trailerResponse -> trailerResponse.getTrailersList().getTrailers())
                .subscribe(
                        trailers::setValue,
                        throwable -> Log.d(TAG, throwable.toString())
                );
        compositeDisposable.add(disposable);
    }

    public void loadReviews(int movieId) {
        Disposable disposable = ApiFactory.API_SERVICE.loadReviews(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(ReviewResponse::getReviews)
                .subscribe(
                        reviews::setValue,
                        throwable -> Log.d(TAG, throwable.toString())
                );
        compositeDisposable.add(disposable);
    }

    public void insertMovie(Movie movie) {
        Disposable disposable = movieDao.insertMovie(movie)
                .subscribeOn(Schedulers.io())
                .doOnTerminate(() -> Log.d(TAG, "Movie with id " + movie.getId()
                        + " was successfully added to favourites"))
                .doOnError((throwable) -> Log.d(TAG, throwable.toString()))
                .subscribe();
        compositeDisposable.add(disposable);
    }

    public void removeMovie(int movieId) {
        Disposable disposable = movieDao.deleteMovie(movieId)
                .subscribeOn(Schedulers.io())
                .doOnTerminate(() -> Log.d(TAG, "Movie with id " + movieId
                        + " was successfully deleted from favourites"))
                .doOnError((throwable) -> Log.d(TAG, throwable.toString()))
                .subscribe();
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
