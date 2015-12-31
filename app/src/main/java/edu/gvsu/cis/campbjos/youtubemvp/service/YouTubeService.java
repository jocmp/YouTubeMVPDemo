package edu.gvsu.cis.campbjos.youtubemvp.service;

import com.google.gson.JsonObject;

import edu.gvsu.cis.campbjos.youtubemvp.model.PlaylistItemListResponse;

import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Java implementation of YouTube Data API.
 * <p>While there is a Java library available, I've used the JSON web implementation
 * in order to illustrate the use of Retrofit and RxJava</p>
 *
 * @author Josiah Campbell
 * @version December 2015
 */
public class YouTubeService {

  private static YouTubeService instance;
  private static YouTubeApi mYouTubeApi;
  private static final String YOUTUBE_API_URL = "https://www.googleapis.com/youtube/v3";

  /**
   * Public constructor for {@link YouTubeService}.
   */
  public YouTubeService() {
    RestAdapter restAdapter = new RestAdapter.Builder()
        .setEndpoint(YOUTUBE_API_URL)
        .build();
    mYouTubeApi = restAdapter.create(YouTubeApi.class);
  }

  public YouTubeApi getApi() {
    return mYouTubeApi;
  }

  /**
   * Interface to retrieve data from YouTube Data service.
   */
  public interface YouTubeApi {
    /* Retrofit will return any object, but Gson will parse JSON responses for us.
       The result is an observable object that can be subscribed to in our
       Presenter class.
     */
    @GET("/playlistItems?part=snippet&maxResults=10")
    Observable<PlaylistItemListResponse> getPlayListItemsList(
                                @Query("playlistId") String playlistId,
                                @Query("nextPageToken") String pageToken,
                                @Query("key") String key);
  }
}