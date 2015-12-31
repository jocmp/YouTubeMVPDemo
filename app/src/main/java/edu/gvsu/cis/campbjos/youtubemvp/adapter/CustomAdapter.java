/*
* Copyright (C) 2014 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package edu.gvsu.cis.campbjos.youtubemvp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import edu.gvsu.cis.campbjos.youtubemvp.R;
import edu.gvsu.cis.campbjos.youtubemvp.YouTubeMvpApplication;
import edu.gvsu.cis.campbjos.youtubemvp.model.PlaylistItem;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
  private static final String TAG = "CustomAdapter";

  private List<PlaylistItem> mDataSet;

  // BEGIN_INCLUDE(recyclerViewSampleViewHolder)

  /**
   * Initialize the dataset of the Adapter.
   *
   * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
   */
  public CustomAdapter(List<PlaylistItem> dataSet) {
    mDataSet = dataSet;
  }
  // END_INCLUDE(recyclerViewSampleViewHolder)

  // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
  // Create new views (invoked by the layout manager)
  @Override
  public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
    // Create a new view.
    View v = LayoutInflater.from(
        viewGroup.getContext()).inflate(R.layout.item_two_line, viewGroup, false);

    return new ViewHolder(v);
  }

  // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
  // Replace the contents of a view (invoked by the layout manager)
  @Override
  public void onBindViewHolder(ViewHolder viewHolder, final int position) {
    // Get element from your dataset at this position and replace the contents of the view
    // with that element
    PlaylistItem.Snippet snippet = mDataSet.get(position).getSnippet();
    viewHolder.setTitle(snippet.getTitle());
    viewHolder.setDescription(snippet.getDescription());
    viewHolder.setImageView(snippet.getThumbnails().getHigh().getUrl());
  }
  // END_INCLUDE(recyclerViewOnCreateViewHolder)

  // Return the size of your dataset (invoked by the layout manager)
  @Override
  public int getItemCount() {
    return mDataSet.size();
  }
  // END_INCLUDE(recyclerViewOnBindViewHolder)

  /**
   * Provide a reference to the type of views that you are using (custom ViewHolder).
   */
  public static class ViewHolder extends RecyclerView.ViewHolder {

    @InjectView(R.id.title)
    TextView titleTextView;
    @InjectView(R.id.description)
    TextView descriptionTextView;
    @InjectView(R.id.imageView)
    ImageView imageView;

    public ViewHolder(View view) {
      super(view);
      ButterKnife.inject(this, view);
    }

    public void setTitle(String title) {
      titleTextView.setText(title);
    }

    public void setDescription(String description) {
      descriptionTextView.setText(description);
    }

    public void setImageView(String url) {
      Picasso.with(YouTubeMvpApplication.getAppContext())
          .load(url)
          .into(imageView);
    }
  }
}
