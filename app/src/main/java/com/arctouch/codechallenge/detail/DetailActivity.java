package com.arctouch.codechallenge.detail;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.api.TmdbApi;
import com.arctouch.codechallenge.base.BaseActivity;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.util.MovieImageUrlBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.arctouch.codechallenge.home.HomeActivity.EXTRA_MOVIE_ID;

public class DetailActivity extends BaseActivity {

    private long idMovie;
    private ProgressBar progressBar;
    private TextView tvTitle, tvDate, tvGenres, tvOverview;
    private ImageView ivPoster, ivBackdrop;
    private MovieImageUrlBuilder movieImageUrlBuilder = new MovieImageUrlBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            idMovie = bundle.getInt(EXTRA_MOVIE_ID);

        createLayout();

        api.movie(idMovie, TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    showMovie(response);
                });
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    private void createLayout() {
        progressBar = findViewById(R.id.progressBar);
        tvTitle = findViewById(R.id.tv_detail_title);
        tvDate = findViewById(R.id.tv_detail_date);
        tvGenres = findViewById(R.id.tv_detail_genres);
        tvOverview = findViewById(R.id.tv_detail_overview);
        ivPoster = findViewById(R.id.iv_detail_poster);
        ivBackdrop = findViewById(R.id.iv_detail_backdrop);
    }

    private void showMovie(Movie movie) {
        if (movie.posterPath != null && !movie.posterPath.isEmpty())
            Glide.with(this)
                    .load(movieImageUrlBuilder.buildPosterUrl(movie.posterPath))
                    .apply(new RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                    .into(ivPoster);
        tvTitle.setText(movie.title);
        tvDate.setText(movie.releaseDate);
        tvGenres.setText(TextUtils.join(", ", movie.genres));
        tvOverview.setText(movie.overview);
        if (movie.backdropPath != null && !movie.backdropPath.isEmpty())
            Glide.with(this)
                    .load(movieImageUrlBuilder.buildBackdropUrl(movie.backdropPath))
                    .apply(new RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                    .into(ivBackdrop);
        progressBar.setVisibility(View.GONE);
    }

}
