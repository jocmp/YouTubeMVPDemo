package edu.gvsu.cis.campbjos.youtubemvp.presenter;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.widget.Toast;

import edu.gvsu.cis.campbjos.youtubemvp.YouTubeMvpApplication;
import edu.gvsu.cis.campbjos.youtubemvp.adapter.CustomAdapter;
import edu.gvsu.cis.campbjos.youtubemvp.model.PlaylistItem;
import edu.gvsu.cis.campbjos.youtubemvp.model.PlaylistItemListResponse;
import edu.gvsu.cis.campbjos.youtubemvp.service.YouTubeService;
import edu.gvsu.cis.campbjos.youtubemvp.view.MainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Presenter class for MVP triad.
 *
 * @author Josiah Campbell
 * @version December 2015
 */
public class MainPresenter {

  private static final String TAG = "MainPresenter";
  private static final String INTENT_URL = "https://www.youtube.com/watch?v=";
  private static final String DEFAULT_PLAYLIST
      = "UUAPurJWGIUtvlul3mApdQRw"; // GVSU YouTube Channel
  private MainActivity mView;
  private YouTubeService mYouTubeService;
  private Adapter mAdapter;
  private List<PlaylistItem> playlistItems;
  private String playlistId;
  private String key;
  private String nextPageToken;
  private CompositeSubscription mCompositeSubscription;

  public MainPresenter(MainActivity activity, YouTubeService youTubeService) {
    mView = activity;
    mYouTubeService = youTubeService;
    playlistItems = new ArrayList<>();
    mCompositeSubscription = new CompositeSubscription();
    playlistId = DEFAULT_PLAYLIST;
    nextPageToken = "";
    mAdapter = new CustomAdapter(playlistItems);
    try {
      key = YouTubeMvpApplication.getManifestValue("YOUTUBE_API_KEY");
    } catch (PackageManager.NameNotFoundException e) {
      Log.d(TAG, "Key not found");
    }
  }

  /**
   * Load items from YouTube API using RxJava.
   */
  public void loadPlaylistItems() {
    mCompositeSubscription.add(
        mYouTubeService.getApi()
            .getPlayListItemsList(playlistId, nextPageToken, key)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<PlaylistItemListResponse>() {
              @Override
              public void onCompleted() {
                /* An Observable calls this method after it has called onNext for the final time,
                 * if it has not encountered any errors.
                 */
                mView.swapAdapter(mAdapter);
                mView.setLoading(false);
              }

              @Override
              public void onError(Throwable error) {
                /* An Observable calls this method to indicate that it has failed to
                 * generate the expected data or has encountered some other error.
                 * This stops the Observable and it will
                 * not make further calls to onNext or onCompleted.
                 */
                Log.d(TAG, "We got an error: " + error);
                mView.setLoading(false);
              }

              @Override
              public void onNext(PlaylistItemListResponse playlistItemListResponse) {
                /* An Observable calls this method whenever the Observable emits an item.
                 * This method takes as a parameter the item emitted by the Observable.
                 */
                playlistItems.clear();
                playlistItems.addAll(playlistItemListResponse.getPlaylistItems());
              }
            })
    );
  }

  public void setPlaylistId(String playlistId) {
    this.playlistId = playlistId;
  }

  /**
   * Intent to launch YouTube app based on {@link PlaylistItem} video ID.
   * @param position Position of {@link PlaylistItem} in dataset
   */
  public void startYouTubeIntent(int position) {
    Intent intent = new Intent(Intent.ACTION_VIEW,
        Uri.parse(
            INTENT_URL + playlistItems.get(position).getSnippet().getResourceId().getVideoId()));
    mView.startActivity(intent);
  }

  /**
   * Prevent memory leaks by un-subscribing from all subscriptions.
   * This method should be called at some point in the Android activity/fragment lifecycle.
   */
  public void unsubscribe() {
    mCompositeSubscription.unsubscribe();
  }
}
