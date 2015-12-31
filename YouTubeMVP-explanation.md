Android is a beautiful, misunderstood platform. On one hand, the Android framework
provides an accessible starting through Java, but drops the ball on user interface architecture.
The iOS platform provides a straight-forward way of dealing with the separation of model data,
presentation and views by incorporating the [Model-View-Controller architecture.](https://developer.apple.com/library/ios/documentation/General/Conceptual/DevPedia-CocoaCore/MVC.html)
Android does not provide a similar stock experience.
By molding the view and presentation layers into one class - the
[Activity](http://developer.android.com/guide/components/activities.html) - Android allows for unwanted spaghetti code to quickly appear.

### MVP: A Design Solution

But this shouldn't completely discourage someone from programming in Android. By
being aware of Android's shortcomings, it becomes easier to find solutions.
Enter Model-View-Presenter, or MVP for short. The MVP architecture allows
developers to separate the model (Plain Old Java Objects) from the presenter
(more on this below) and the Activity class.

In MVP, the Activity class is oblivious to any behavior logic. Instead, the Activity
is our View. It displays boundary data to the user and routes events - such as
a button click or a swipe-to-refresh action - to the presenter class. The presenter acts
on the view and manipulates the model. The Activity knows nothing about the model!

### Toolbelt

Implementing MVP in Android is straightforward, but there are a few tools that help
developers get started. From my experience, there are a few tools I have used:
* __[Retrofit](http://square.github.io/retrofit/)__ is an HTTP client which allows Gson serialization, among others.
* __[Butterknife](http://jakewharton.github.io/butterknife/)__ is a library from Jake Wharton for view injection and binding.
* __[RxJava](https://github.com/ReactiveX/RxJava)__ implements Reactive programming and allows a
useful alternative to [AsyncTasks](http://developer.android.com/reference/android/os/AsyncTask.html) in Android.
* __[Retrolambda](https://github.com/orfjackal/retrolambda)__ provides lambda compatibility for Java 7, which Android targets.

All of these tools can be implemented using the Gradle build tool which is built into Android Studio.

### Example Time

#### M is for Model
Let's say we want to get back a feed from a Grand Valley State University [YouTube channel](https://www.youtube.com/user/gvsu).
In order to do this, we'll need to access the [YouTube Data API](https://developers.google.com/youtube/v3/)
and pull down information in a JSON format. Already we have our model data, which looks something like this:

```json
{
    "kind": "youtube#playlistItemListResponse",
    "etag": "\"kuL0kDMAqRo3pU7O0pwlO-Lfzp4/x4w4rOlPlwRV4Tip_QtcDcuXgKE\"",
    "nextPageToken": "CAUQAA",
    "pageInfo": {
        "totalResults": 825,
        "resultsPerPage": 5
    },
    "items": [{
                "kind": "youtube#playlistItem",
                "etag": "\"kuL0kDMAqRo3pU7O0pwlO-Lfzp4/PDRLMMmLcPNuIZa7NgIiBA6XN3Q\"",
                "id": "UUvqnQ-qv0k7G_gOvEVw8hj__fFtWz2lGK",
                "snippet": {
                    "publishedAt": "2015-12-18T15:47:12.000Z",
                    "channelId": "UCAPurJWGIUtvlul3mApdQRw",
                    "title": "2015 Fall Arts Celebration - Holiday Celebration",
                    "description": "Stille Nacht: A Celebration of Holiday Music from Europe",
                    "thumbnails": {
                        "default": {
                            "url": "https://i.ytimg.com/vi/2ArGL_lJWnI/default.jpg",
                            "width": 120,
                            "height": 90
                        }
                      }
                    }
                  }
    ]
  }
}
```
Already we're face with a plethora of POJOs we need to create: one for the
`playlistItemListResponse`, another for `playlistItem`, `snippet` and so on. The
easiest solution I've found is to automate this part of the process by using a schema
converter: http://www.jsonschema2pojo.org/. This allows us to generate precise POJOs that
even have Gson annotations. Sweet!

#### Service API
We have our model classes, but how do we retrieve the data from the API in Java?
The easiest way is to create a service class to handle the API.
```java
public class YouTubeService {

  private static YouTubeService instance;
  private static YouTubeApi mYouTubeApi;
  private static final String YOUTUBE_API_URL = "https://www.googleapis.com/youtube/v3";

  public YouTubeService() {
    RestAdapter restAdapter = new RestAdapter.Builder()
        .setEndpoint(YOUTUBE_API_URL)
        .build();
    mYouTubeApi = restAdapter.create(YouTubeApi.class);
  }
  // ...
```
Using Retrofit, we can create an interface that will handle the YouTube Data API's endpoints:

```java
public interface YouTubeApi {
  @GET("/playlistItems?part=snippet&maxResults=10")
  Observable<PlaylistItemListResponse> getPlayListItemsList(
                              @Query("playlistId") String playlistId,
                              @Query("nextPageToken") String pageToken,
                              @Query("key") String key);
}
```

#### P is for Presenter

With our model classes defined, we can focus on the Presenter. The presenter should
be stateless, so we'll pass in a copy of our activity and the YouTube service class:
```java
public MainPresenter(MainActivity activity, YouTubeService youTubeService) {
  mView = activity;
  mYouTubeService = youTubeService;
  // More constructor information ...
}
```
The presenter class will handle any interactions with the API, any modifications of the model data,
and receive user commands from the view. For example, if a user swipes-to-refresh,

#### V is for View
When we create the Activity, we need a instance of the service and the presenter, and
then add in any other view information, such as a list to display our YouTube results.
```java
public class MainActivity extends AppCompatActivity {

  // Class and instance variables here ...

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.inject(this);
    mYouTubeService = new YouTubeService();
    mPresenter = new MainPresenter(this, mYouTubeService);
    // Set up the RecyclerView here ...
  }
```
Now if we want to handle a swipe-to-refresh event, all we need to do is call the
presenter class from the Activity:
```java
@Override
protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_main);
  ButterKnife.inject(this);
  mYouTubeService = new YouTubeService();
  mPresenter = new MainPresenter(this, mYouTubeService);
  // Set up the RecyclerView here ...
  mSwipeRefreshLayout.setOnRefreshListener(mPresenter::loadPlaylistItems);
}
```
And in the presenter class:
```java
public void loadPlaylistItems() {
   mCompositeSubscription.add(
       mYouTubeService.getApi()
           .getPlayListItemsList(playlistId, nextPageToken, key)
           .subscribeOn(Schedulers.newThread())
           .observeOn(AndroidSchedulers.mainThread())
           .subscribe(new Observer<PlaylistItemListResponse>() {
             @Override
             public void onCompleted() {
               mView.swapAdapter(mAdapter);
               mView.setLoading(false);
             }

             @Override
             public void onError(Throwable error) {
               Log.d(TAG, "We got an error: " + error);
             }

             @Override
             public void onNext(PlaylistItemListResponse playlistItemListResponse) {
               playlistItems.clear();
               playlistItems.addAll(playlistItemListResponse.getPlaylistItems());
             }
           })
   );
 }
```
The presenter can issue information to the view such as `setLoading(false)` which
the view (Activity) will set accordingly, but at no point does the view need to know
_why_ the method `setLoading` is being called. This is the beauty of MVP: [separation of concerns](http://fernandocejas.com/2014/09/03/architecting-android-the-clean-way/).

And that's it! If you want to learn more about MVP in Android, I recommend reading Fernandos Cejas' [Architecting Android](http://fernandocejas.com/2014/09/03/architecting-android-the-clean-way/)
article, or Antonio Leiva's [MVP for Android](http://antonioleiva.com/mvp-android/).
For information on RxJava, Dan Lew's [Grokking RxJava](http://blog.danlew.net/2014/09/15/grokking-rxjava-part-1/) series is an invaluable resource.

The example code from this article is open-source and accessible on GitHub. Download it, get an API key and enjoy.
