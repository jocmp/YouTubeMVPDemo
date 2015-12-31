package edu.gvsu.cis.campbjos.youtubemvp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Response object from the YouTube Data API.
 * <p>Note that Gson will parse inner classes so long as they are static.</p>
 * @author Josiah Campbell
 * @version December 2015
 */
public class PlaylistItemListResponse {

  @SerializedName("kind")
  @Expose
  private String kind;
  @SerializedName("etag")
  @Expose
  private String etag;
  @SerializedName("nextPageToken")
  @Expose
  private String nextPageToken;
  @SerializedName("pageInfo")
  @Expose
  private PageInfo pageInfo;
  @SerializedName("items")
  @Expose
  private List<PlaylistItem> items = new ArrayList<>();

  public String getKind() {
    return kind;
  }

  public String getEtag() {
    return etag;
  }

  public String getNextPageToken() {
    return nextPageToken;
  }

  public PageInfo getPageInfo() {
    return pageInfo;
  }

  public List<PlaylistItem> getPlaylistItems() {
    return items;
  }

  public static class PageInfo {

    @SerializedName("totalResults")
    @Expose
    private Integer totalResults;
    @SerializedName("resultsPerPage")
    @Expose
    private Integer resultsPerPage;

    public Integer getTotalResults() {
      return totalResults;
    }

    public Integer getResultsPerPage() {
      return resultsPerPage;
    }
  }
}