package j.com.giphysearch.ui;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import j.com.giphysearch.GifActivity;
import j.com.giphysearch.R;
import j.com.giphysearch.entity.Gif;
import j.com.giphysearch.utils.GifHelper;

public class GifAdapter extends RecyclerView.Adapter<GifAdapter.GifViewHolder> {

    private List<Gif> gifs;
    private Context context;
    private GifHelper gifHelper;
    private ClickListener listener;

    public GifAdapter(Context context) {
        this.context = context;
    }

    public void setOnClickListener(ClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public GifAdapter.GifViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.gif_item, viewGroup, false);
        return new GifViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GifAdapter.GifViewHolder viewHolder, int i) {
        if (gifs != null) {
            Gif gif = gifs.get(i);
            String gifTitle = gif.getTitle();
            if (gifTitle.equals("")) {
                viewHolder.title.setVisibility(View.INVISIBLE);
            } else {
                viewHolder.title.setText(gifTitle);
            }
//            int height = (Integer.parseInt(gif.getImages().getFixed_height().getHeight() == null ?
//                    "0" : gif.getImages().getFixed_height().getHeight()));
//            if (height > 0) {
//                viewHolder.imageView.getLayoutParams().height = height;
//            }
            viewHolder.progressBar.setVisibility(View.VISIBLE);

            //Loading and setting gif to imageView
            Glide.with(context).asGif().load(gif.getImages().getFixed_height().getUrl()).listener(new RequestListener<GifDrawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                    viewHolder.progressBar.setVisibility(View.GONE);
                    return false;
                }

                @Override
                public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                    viewHolder.progressBar.setVisibility(View.GONE);
                    return false;
                }
            }).into(viewHolder.imageView);

            //Setting OnClickListeners for buttons and imageView
            viewHolder.saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gifHelper = new GifHelper(context, gif);
                    gifHelper.startSavingGif(false);
                }
            });
            viewHolder.shareBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gifHelper = new GifHelper(context, gif);
                    gifHelper.startSavingGif(true);
                }
            });
            viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onGifImageClick(gif);
                    }
                    Intent intent = new Intent(context, GifActivity.class);
                    intent.putExtra("gif_id", gif.getId());
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (gifs != null) {
            return gifs.size();
        } else {
            return 0;
        }
    }

    //This method is used for setting new data from LiveData
    public void setData(List<Gif> gifs) {
        this.gifs = gifs;

        this.notifyDataSetChanged();
    }

    public List<Gif> getGifs() {
        return gifs;
    }


    //ViewHolder class
    class GifViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.gif_image)
        ImageView imageView;

        @BindView(R.id.progress_bar)
        ProgressBar progressBar;

        @BindView(R.id.gif_title)
        TextView title;

        @BindView(R.id.share_btn)
        Button shareBtn;

        @BindView(R.id.save_btn)
        Button saveBtn;

        public GifViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ClickListener {
        void onGifImageClick(Gif gif);
    }
}
