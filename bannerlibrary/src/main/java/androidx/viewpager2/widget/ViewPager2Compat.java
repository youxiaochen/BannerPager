package androidx.viewpager2.widget;


import androidx.recyclerview.widget.RecyclerView;

/**
 * create by you 2019-02
 */
public final class ViewPager2Compat {

    private ViewPager2Compat() {}

    public static RecyclerView getRecyclerView(ViewPager2 viewPager2) {
        return viewPager2.mRecyclerView;
    }

    public static void setItemViewCacheSize(ViewPager2 viewPager2, int cacheSize) {
        viewPager2.mRecyclerView.setItemViewCacheSize(cacheSize);
    }

    public static void setHasFixedSize(ViewPager2 viewPager2, boolean hasFixedSize) {
        viewPager2.mRecyclerView.setHasFixedSize(hasFixedSize);
    }

}
