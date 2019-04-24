package com.arctouch.codechallenge.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.api.TmdbApi;
import com.arctouch.codechallenge.base.BaseActivity;
import com.arctouch.codechallenge.data.Cache;
import com.arctouch.codechallenge.detail.DetailActivity;
import com.arctouch.codechallenge.model.Genre;
import com.arctouch.codechallenge.model.Movie;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class HomeActivity extends BaseActivity implements HomeAdapter.ClickListener {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    public static final String EXTRA_MOVIE_ID = "movie_id";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        this.recyclerView = findViewById(R.id.recyclerView);
        this.progressBar = findViewById(R.id.progressBar);

        api.upcomingMovies(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE, 1L, TmdbApi.DEFAULT_REGION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    showMovieList(response.results);
                });
    }

    @Override public void onItemClickListener(int id) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(EXTRA_MOVIE_ID, id);
        startActivity(intent);
    }

    private void showMovieList(List<Movie> movies) {
        for (Movie movie : movies) {
            movie.genres = new ArrayList<>();
            for (Genre genre : Cache.getGenres()) {
                if (movie.genreIds.contains(genre.id)) {
                    movie.genres.add(genre);
                }
            }
        }

        recyclerView.setAdapter(new HomeAdapter(movies, this));
        progressBar.setVisibility(View.GONE);
    }

}
