package you.chen.bannerlibrary;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public abstract class BannerAdapter<VH extends BannerViewHolder> extends RecyclerView.Adapter<VH> {

    public static final int DEF_MIN_AUTO_RUNNING_COUNT = 2;
    //自动轮播时的最小数量
    private final int minAutoRunningCount;

    public BannerAdapter() {
        this(DEF_MIN_AUTO_RUNNING_COUNT);
    }

    public BannerAdapter(int minAutoRunningCount) {
        this.minAutoRunningCount = minAutoRunningCount;
    }

    @Override
    public final void onBindViewHolder(@NonNull VH vh, int position) {
        int bannerPosition = getBannerPosition(position);
        vh.bannerPosition = bannerPosition;
        onBindBannerViewHolder(vh, vh.bannerPosition);
    }

    @Override
    public final int getItemCount() {
        int bannerItemCount = getBannerItemCount();
        return bannerItemCount >= minAutoRunningCount ? bannerItemCount + BannerPager.SCROLL_PROXY_MAX : bannerItemCount;
    }

    public abstract int getBannerItemCount();

    public abstract void onBindBannerViewHolder(VH vh, int bannerPosition);

    public int getBannerItemViewType(int position) {
        return 0;
    }

    public long getBannerItemId(int bannerPosition, int position) {
        return RecyclerView.NO_ID;
    }

    /**
     * 计算实际位置
     * @param position
     * @return
     */
    public final int getBannerPosition(int position) {
        int itemCount = getBannerItemCount();
        if (itemCount < minAutoRunningCount) return position;
        int subCount = BannerPager.SCROLL_PROXY_LEFT % itemCount;
        if (subCount  == 0) {
            return position % itemCount;
        }
        int startPosition = itemCount - subCount;
        if (position < subCount) {
            return startPosition + position;
        }
        return (position - subCount) % itemCount;
    }

    public final boolean canAutoScroll() {
        return getBannerItemCount() >= minAutoRunningCount;
    }

    @Override
    public final int getItemViewType(int position) {
        return getBannerItemViewType(getBannerPosition(position));
    }

    @Override
    public final long getItemId(int position) {
        return getBannerItemId(getBannerPosition(position), position);
    }

    @Override
    public final void onBindViewHolder(@NonNull VH holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

}
