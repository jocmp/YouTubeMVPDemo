package edu.gvsu.cis.campbjos.youtubemvp.view;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import edu.gvsu.cis.campbjos.youtubemvp.R;
import edu.gvsu.cis.campbjos.youtubemvp.adapter.CustomAdapter;
import edu.gvsu.cis.campbjos.youtubemvp.adapter.RecyclerItemClickListener;
import edu.gvsu.cis.campbjos.youtubemvp.model.PlaylistItem;
import edu.gvsu.cis.campbjos.youtubemvp.presenter.MainPresenter;
import edu.gvsu.cis.campbjos.youtubemvp.service.YouTubeService;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Bind;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = "MainActivity";
  private RecyclerView.Adapter mAdapter;
  private MainPresenter mPresenter;
  private YouTubeService mYouTubeService;

  @Bind(R.id.recyclerView)
  RecyclerView mRecyclerView;
  @Bind(R.id.swipeRefreshLayout)
  SwipeRefreshLayout mSwipeRefreshLayout;
  @Bind(R.id.circleProgressBar)
  ProgressBar circleProgressBar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    mYouTubeService = new YouTubeService();
    mPresenter = new MainPresenter(this, mYouTubeService);

    mRecyclerView.setLayoutManager(
        new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    mRecyclerView.setAdapter(mAdapter);
    mRecyclerView.addOnItemTouchListener(
        new RecyclerItemClickListener(this, mRecyclerView,
            (View view, int position) -> mPresenter.startYouTubeIntent(position)
        )
    );
    mPresenter.loadPlaylistItems();
    mSwipeRefreshLayout.setOnRefreshListener(mPresenter::loadPlaylistItems);
  }

  public void setLoading(boolean loading) {
    circleProgressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
    mSwipeRefreshLayout.setRefreshing(loading);
  }

  /**
   * Adapter that should be passed by the presenter class.
   * @param adapter supplied from presenter
   */
  public void swapAdapter(Adapter adapter) {
    mRecyclerView.swapAdapter(adapter, false);
  }

  @Override
  protected void onStop() {
    super.onStop();
    mPresenter.unsubscribe();
  }
}
