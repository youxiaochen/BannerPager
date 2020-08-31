package you.chen.bannerlibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import androidx.viewpager2.widget.ViewPager2Compat;

import java.util.ArrayList;
import java.util.List;

/**
 * create by you 2019-02
 */
public class BannerPager extends FrameLayout {
    /**
     * 无限循环时附加的数量
     */
    static final int SCROLL_PROXY_MAX = 50;
    static final int SCROLL_PROXY_LEFT = 10;
    /**
     * 默认切换一次动画间隔
     */
    private static final int DEFAULT_DELAY = 4000;

    private ViewPager2 mViewPager2;
    private BannerAdapter mAdapter;
    private ProxyPageChangeCallback pageChangeCallback;
    //轮播开关
    private boolean runningSwitch;
    //自动切换
    private boolean autoRunning = true;
    //页面是否正在翻
    private boolean isPageTurning;
    //是否附着到window
    private boolean hasAttachedToWindow;
    //切换的间隔时间
    private int runningTime = DEFAULT_DELAY;

    private RecyclerView.AdapterDataObserver observer;

    private Runnable autoRunnable = new Runnable() {
        @Override
        public void run() {
            turnPage();
        }
    };

    public BannerPager(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public BannerPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BannerPager(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs) {
        int orientation = 0;
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BannerPager);
            orientation = a.getInt(R.styleable.BannerPager_bannerOrientation, orientation);
            a.recycle();
        }

        mViewPager2 = new ViewPager2(context);
        mViewPager2.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mViewPager2.setOffscreenPageLimit(1);
        mViewPager2.setOrientation(orientation == 0 ? ViewPager2.ORIENTATION_HORIZONTAL : ViewPager2.ORIENTATION_VERTICAL);
        ViewPager2Compat.setItemViewCacheSize(mViewPager2, 0);
        ViewPager2Compat.setHasFixedSize(mViewPager2, true);

        pageChangeCallback = new ProxyPageChangeCallback();
        observer = new BannerIndicatorObserver();
        addView(mViewPager2);
    }

    /**
     * banner页面变化
     */
    private void update() {
        //Log.i("BannerPager", "update");
        pageChangeCallback.mLastSelectPosition = -1;
        checkCurrentItem();
        if (this.listeners != null) {
            for (OnBannerChangeListener listener : this.listeners) {
                if (listener != null) {
                    listener.onChanged(mAdapter.getBannerItemCount());
                }
            }
        }
    }

    /**
     * 矫正当前位置,并延时滑动下一次
     */
    private void checkCurrentItem() {
        if (autoRunning && hasAttachedToWindow && mAdapter != null && mAdapter.canAutoScroll()) {
            int currentPosition = mViewPager2.getCurrentItem();
            if (currentPosition < SCROLL_PROXY_LEFT || currentPosition >= mAdapter.getBannerItemCount() + SCROLL_PROXY_LEFT) {
                int toPosition = mAdapter.getBannerPosition(currentPosition) + SCROLL_PROXY_LEFT;
                //Log.i("BannerPager", "BannerPager checkPosition " + toPosition);
                mViewPager2.setCurrentItem(toPosition, false);
            }
            //Log.i("BannerPager", "scrollToNextItem " + mViewPager2.getCurrentItem());
            if (runningSwitch) {
                //Log.i("BannerPager", "postDelayed...");
                postDelayed(autoRunnable, runningTime);
            }
        }
    }

    /**
     * 翻到下一个item
     */
    private void turnPage() {
        //Log.i("BannerPager","after turnPage ");
        if (autoRunning && runningSwitch && !isPageTurning && hasAttachedToWindow && mAdapter != null && mAdapter.canAutoScroll()) {
            int currentItem = mViewPager2.getCurrentItem();
            //Log.i("BannerPager","turnPage " + (currentItem + 1));
            mViewPager2.setCurrentItem(currentItem + 1);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        hasAttachedToWindow = true;
        mViewPager2.registerOnPageChangeCallback(pageChangeCallback);
        startAutoRunning();
    }

    @Override
    protected void onDetachedFromWindow() {
        hasAttachedToWindow = false;
        mViewPager2.unregisterOnPageChangeCallback(pageChangeCallback);
        stopAutoRunning();
        super.onDetachedFromWindow();
    }

    public final void setAdapter(@NonNull BannerAdapter adapter) {
        if (adapter == null) return;
        if (mAdapter != null) {
            mAdapter.unregisterAdapterDataObserver(observer);
        }
        mAdapter = adapter;
        mAdapter.registerAdapterDataObserver(observer);
        pageChangeCallback.mLastSelectPosition = -1;
        mViewPager2.setAdapter(mAdapter);
        if (mAdapter.canAutoScroll()) {
            mViewPager2.setCurrentItem(SCROLL_PROXY_LEFT, false);
        }
    }

    public final BannerAdapter getAdapter() {
        return mAdapter;
    }

    /**
     * 开始轮播
     */
    public final void startAutoRunning() {
        runningSwitch = true;
        removeCallbacks(autoRunnable);
        if (autoRunning && hasAttachedToWindow && !isPageTurning) {
            postDelayed(autoRunnable, runningTime);
        }
    }

    //stop
    public final void stopAutoRunning() {
        removeCallbacks(autoRunnable);
        runningSwitch = false;
    }

    public final int getCurrentBannerItem() {
        int position = mViewPager2.getCurrentItem();
        if (mAdapter != null) {
            return mAdapter.getBannerPosition(position);
        }
        return position;
    }

    public final int getCurrentItem() {
        return mViewPager2.getCurrentItem();
    }

    public final void setAutoRunning(boolean autoRunning) {
        if (this.autoRunning == autoRunning) return;
        this.autoRunning = autoRunning;
        if (autoRunning) {
            startAutoRunning();
        } else {
            stopAutoRunning();
        }
    }

    public final void setRunningTime(int runningTime) {
        if (runningTime < 1000) return;
        this.runningTime = runningTime;
    }

    public final void setItemViewCacheSize(int cacheSize) {
        ViewPager2Compat.setItemViewCacheSize(mViewPager2, cacheSize);
    }

    public final void setHasFixedSize(boolean hasFixedSize) {
        ViewPager2Compat.setHasFixedSize(mViewPager2, hasFixedSize);
    }

    public final void setOrientation(@ViewPager2.Orientation int orientation) {
        mViewPager2.setOrientation(orientation);
    }

    public final int getOrientation() {
        return mViewPager2.getOrientation();
    }

    public final void setPageTransformer(@Nullable ViewPager2.PageTransformer transformer) {
        mViewPager2.setPageTransformer(transformer);
    }

    private List<OnBannerChangeListener> listeners;

    public void addOnBannerChangeListener(OnBannerChangeListener listener) {
        if (this.listeners == null) {
            this.listeners = new ArrayList<>();
        }
        this.listeners.add(listener);
        if (listener != null && mAdapter != null) {
            listener.onChanged(mAdapter.getBannerItemCount());
        }
    }

    public void removeOnBannerChangeListener(OnBannerChangeListener listener) {
        if (this.listeners != null) {
            this.listeners.remove(listener);
        }
    }

    /**
     * banner change listener
     */
    public interface OnBannerChangeListener {

        void onChanged(int itemCount);

        void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

        void onPageSelected(int position);

        void onPageScrollStateChanged(int state);
    }

    //Simple
    public abstract static class SimpleBannerChangeListener implements OnBannerChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    /**
     * 轮播指示
     */
    private class BannerIndicatorObserver extends RecyclerView.AdapterDataObserver {
        @Override
        public void onChanged() {
            update();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            update();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            update();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            update();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            update();
        }
    }

    private class ProxyPageChangeCallback extends ViewPager2.OnPageChangeCallback {

        int mLastSelectPosition = -1;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //Log.i("BannerPager", "onPageScrolled " + position +" offset " + positionOffset + " offsetPixels " + positionOffsetPixels);
            if (listeners != null) {
                for (OnBannerChangeListener listener : listeners) {
                    if (listener != null) {
                        listener.onPageScrolled(position, positionOffset, positionOffsetPixels);
                    }
                }
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (mAdapter != null) {
                int bannerPosition = mAdapter.getBannerPosition(position);
                if (bannerPosition != mLastSelectPosition) {
                    mLastSelectPosition = bannerPosition;
                    if (listeners != null) {
                        for (OnBannerChangeListener listener : listeners) {
                            if (listener != null) {
                                listener.onPageSelected(bannerPosition);
                            }
                        }
                    }
                }
            } else {
                mLastSelectPosition = -1;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            //Log.i("BannerPager", "onPageScrollStateChanged " + state);
            if (state == ViewPager2.SCROLL_STATE_IDLE) {
                isPageTurning = false;
                checkCurrentItem();
            } else {
                isPageTurning = true;
                removeCallbacks(autoRunnable);
            }
            if (listeners != null) {
                for (OnBannerChangeListener listener : listeners) {
                    if (listener != null) {
                        listener.onPageScrollStateChanged(state);
                    }
                }
            }
        }
    }

}
