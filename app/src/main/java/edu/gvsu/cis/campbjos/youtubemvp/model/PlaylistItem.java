package edu.gvsu.cis.campbjos.youtubemvp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Playlist items are instantiated as a list in the {@link PlaylistItemListResponse}.
 * <p>The {@link Snippet} class provides the bulk of the boundary information such as
 * descriptions, titles, and thumbnail images for videos</p>
 *
 * @author Josiah Campbell
 * @version December 2015
 */

public class PlaylistItem {

  @SerializedName("kind")
  @Expose
  private String kind;
  @SerializedName("etag")
  @Expose
  private String etag;
  @SerializedName("id")
  @Expose
  private String id;
  @SerializedName("snippet")
  @Expose
  private Snippet snippet;

  public String getKind() {
    return kind;
  }

  public String getEtag() {
    return etag;
  }

  public String getId() {
    return id;
  }

  public Snippet getSnippet() {
    return snippet;
  }

  public static class Snippet {

    @SerializedName("publishedAt")
    @Expose
    private String publishedAt;
    @SerializedName("channelId")
    @Expose
    private String channelId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("thumbnails")
    @Expose
    private Thumbnails thumbnails;
    @SerializedName("channelTitle")
    @Expose
    private String channelTitle;
    @SerializedName("playlistId")
    @Expose
    private String playlistId;
    @SerializedName("position")
    @Expose
    private Integer position;
    @SerializedName("resourceId")
    @Expose
    private ResourceId resourceId;

    public String getPublishedAt() {
      return publishedAt;
    }

    public String getChannelId() {
      return channelId;
    }

    public String getTitle() {
      return title;
    }

    public String getDescription() {
      return description;
    }

    public Thumbnails getThumbnails() {
      return thumbnails;
    }

    public String getChannelTitle() {
      return channelTitle;
    }

    public String getPlaylistId() {
      return playlistId;
    }

    public Integer getPosition() {
      return position;
    }

    public ResourceId getResourceId() {
      return resourceId;
    }

    public static class Thumbnails {

      @SerializedName("default")
      @Expose
      private Default _default;
      @SerializedName("medium")
      @Expose
      private Medium medium;
      @SerializedName("high")
      @Expose
      private High high;
      @SerializedName("standard")
      @Expose
      private Standard standard;

      public Default getDefault() {
        return _default;
      }

      public Medium getMedium() {
        return medium;
      }

      public High getHigh() {
        return high;
      }

      public Standard getStandard() {
        return standard;
      }

      public static class Default {

        @SerializedName("url")
        @Expose
        private String url;
        @SerializedName("width")
        @Expose
        private Integer width;
        @SerializedName("height")
        @Expose
        private Integer height;

        public String getUrl() {
          return url;
        }

        public Integer getWidth() {
          return width;
        }

        public Integer getHeight() {
          return height;
        }
      }

      public static class High {

        @SerializedName("url")
        @Expose
        private String url;
        @SerializedName("width")
        @Expose
        private Integer width;
        @SerializedName("height")
        @Expose
        private Integer height;

        public String getUrl() {
          return url;
        }

        public Integer getWidth() {
          return width;
        }

        public Integer getHeight() {
          return height;
        }
      }

      public static class Medium {

        @SerializedName("url")
        @Expose
        private String url;
        @SerializedName("width")
        @Expose
        private Integer width;
        @SerializedName("height")
        @Expose
        private Integer height;

        public String getUrl() {
          return url;
        }

        public Integer getWidth() {
          return width;
        }

        public Integer getHeight() {
          return height;
        }
      }

      public static class Standard {

        @SerializedName("url")
        @Expose
        private String url;
        @SerializedName("width")
        @Expose
        private Integer width;
        @SerializedName("height")
        @Expose
        private Integer height;

        public String getUrl() {
          return url;
        }

        public Integer getWidth() {
          return width;
        }

        public Integer getHeight() {
          return height;
        }
      }
    }

    public static class ResourceId {

      @SerializedName("kind")
      @Expose
      private String kind;
      @SerializedName("videoId")
      @Expose
      private String videoId;

      public String getKind() {
        return kind;
      }

      public String getVideoId() {
        return videoId;
      }
    }
  }
}