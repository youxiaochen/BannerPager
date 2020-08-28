package you.chen.bannerlibrary;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class BannerViewHolder extends RecyclerView.ViewHolder {

    int bannerPosition = RecyclerView.NO_POSITION;

    public BannerViewHolder(View itemView) {
        super(itemView);
    }

    public final int getBannerPosition() {
        return bannerPosition;
    }

}
