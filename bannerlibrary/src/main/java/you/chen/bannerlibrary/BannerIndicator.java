package you.chen.bannerlibrary;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * create by you 2019-02
 */
public class BannerIndicator extends View {

    @IntDef({ORIENTATION_HORIZONTAL, ORIENTATION_VERTICAL})
    public @interface Orientation {
    }

    public static final int ORIENTATION_HORIZONTAL = 0;
    public static final int ORIENTATION_VERTICAL = 1;

    /**
     * 水平与垂直布局
     */
    private int orientation;
    /**
     * 默认宽度, drawable未指定高度时的
     */
    private static final int DEF_WIDTH = 30;
    /**
     * 默认间隙
     */
    private int indicatorMargin = 50;
    /**
     * 选中与未选中时的图标
     */
    private Drawable indicatorDrawable, indicatorSelectDrawable;
    /**
     * 标记的数量与当前索引
     */
    private int indicatorCount, selectPosition;
    /**
     * 图标宽高与select时的宽高
     */
    private int drawableWidth, drawableHeight;
    private int selectDrawableWidth, selectDrawableHeight;
    //单元的宽度与长度
    private int unitWidth, unitHeight;
    /**
     * 图标画中心时的偏移
     */
    private int centerXOff, centerYoff;
    private int centerSelectXOff, centerSelectYoff;

    public BannerIndicator(Context context) {
        super(context);
        init(context, null);
    }

    public BannerIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BannerIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs) {
        Drawable drawable = null;
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BannerIndicator);
            indicatorMargin = a.getDimensionPixelOffset(R.styleable.BannerIndicator_indicatorMargin, indicatorMargin);
            drawable = a.getDrawable(R.styleable.BannerIndicator_indicatorDrawable);
            orientation = a.getInt(R.styleable.BannerIndicator_indicatorOrientation, ORIENTATION_HORIZONTAL);
            a.recycle();
        }
        initDrawable(drawable);
    }

    private void initDrawable(Drawable drawable) {
        if (drawable instanceof StateListDrawable) {
            StateListDrawable listDrawable = (StateListDrawable) drawable;
            indicatorDrawable = listDrawable.getCurrent();
            listDrawable.setState(new int[]{android.R.attr.state_selected});
            indicatorSelectDrawable = listDrawable.getCurrent();
        } else {
            indicatorSelectDrawable = null;
            indicatorDrawable = null;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (indicatorCount <= 0 || indicatorDrawable == null || indicatorSelectDrawable == null) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        selectDrawableWidth = indicatorSelectDrawable.getIntrinsicWidth();
        if (selectDrawableWidth < 0) selectDrawableWidth = DEF_WIDTH;
        selectDrawableHeight = indicatorSelectDrawable.getIntrinsicHeight();
        if (selectDrawableHeight < 0) selectDrawableHeight = DEF_WIDTH;

        drawableWidth = indicatorDrawable.getIntrinsicWidth();
        if (drawableWidth < 0) drawableWidth = DEF_WIDTH;
        drawableHeight = indicatorDrawable.getIntrinsicHeight();
        if (drawableHeight < 0) drawableHeight = DEF_WIDTH;

        unitWidth = Math.max(selectDrawableWidth, drawableWidth);
        unitHeight = Math.max(selectDrawableHeight, drawableHeight);
        centerXOff = (unitWidth - drawableWidth) >> 1;
        centerYoff = (unitHeight - drawableHeight) >> 1;
        centerSelectXOff = (unitWidth - selectDrawableWidth) >> 1;
        centerSelectYoff = (unitHeight - selectDrawableHeight) >> 1;


        int width, height;
        if (orientation == ORIENTATION_HORIZONTAL) {
            width = getPaddingLeft() + getPaddingRight() + unitWidth * indicatorCount + (indicatorCount - 1) * indicatorMargin;
            height = getPaddingTop() + getPaddingBottom() + unitHeight;
        } else {
            width = getPaddingLeft() + getPaddingRight() + unitWidth;
            height = getPaddingTop() + getPaddingBottom() + unitHeight * indicatorCount + (indicatorCount - 1) * indicatorMargin;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (indicatorCount <= 0 || indicatorDrawable == null || indicatorSelectDrawable == null) return;
        int left = getPaddingLeft();
        int top = getPaddingTop();
        if (orientation == ORIENTATION_HORIZONTAL) {
            //画水平时的
            for (int i = 0; i < indicatorCount; i++) {
                if (i == selectPosition) {
                    int selectLeft = left + centerSelectXOff;
                    int selectTop = top + centerSelectYoff;
                    indicatorSelectDrawable.setBounds(selectLeft, selectTop, selectLeft + selectDrawableWidth, selectTop + selectDrawableHeight);
                    indicatorSelectDrawable.draw(canvas);
                } else {
                    int bleft = left + centerXOff;
                    int btop = top + centerYoff;
                    indicatorDrawable.setBounds(bleft, btop, bleft + drawableWidth, btop + drawableHeight);
                    indicatorDrawable.draw(canvas);
                }
                left += (unitWidth + indicatorMargin);
            }
        } else {
            //画垂直时的
            for (int i = 0; i < indicatorCount; i++) {
                if (i == selectPosition) {
                    int selectLeft = left + centerSelectXOff;
                    int selectTop = top + centerSelectYoff;
                    indicatorSelectDrawable.setBounds(selectLeft, selectTop, selectLeft + selectDrawableWidth, selectTop + selectDrawableHeight);
                    indicatorSelectDrawable.draw(canvas);
                } else {
                    int bleft = left + centerXOff;
                    int btop = top + centerYoff;
                    indicatorDrawable.setBounds(bleft, btop, bleft + drawableWidth, btop + drawableHeight);
                    indicatorDrawable.draw(canvas);
                }
                top += (unitHeight + indicatorMargin);
            }
        }
    }

    public void setCount(int indicatorCount) {
        if (indicatorCount < 0) indicatorCount = 0;
        if (this.indicatorCount == indicatorCount) return;
        this.indicatorCount = indicatorCount;
        requestLayout();
        invalidate();
    }

    public void setSelectPosition(int selectPosition) {
        if (selectPosition >= indicatorCount) return;
        this.selectPosition = selectPosition;
        postInvalidate();
    }

    public final void setOrientation(@Orientation int orientation) {
        if (this.orientation == orientation) return;
        this.orientation = orientation;
        requestLayout();
        postInvalidate();
    }

    public final void setImageDrawable(Drawable drawable) {
        initDrawable(drawable);
        requestLayout();
        postInvalidate();
    }

    public final void setImageResource(@DrawableRes int resId) {
        setImageDrawable(getContext().getResources().getDrawable(resId));
    }

    private BannerPager bannerPager;
    private BannerPager.OnBannerChangeListener onBannerChangeListener;

    /**
     * 关联BannerPager, 界面销毁的时候适当的设置null
     * @param bannerPager
     */
    public void setupWithBannerPager(@Nullable BannerPager bannerPager) {
        if (this.bannerPager != null) {
            if (this.onBannerChangeListener != null) {
                this.bannerPager.removeOnBannerChangeListener(this.onBannerChangeListener);
            }
        }
        if (bannerPager != null) {
            this.bannerPager = bannerPager;
            if (this.onBannerChangeListener == null) {
                this.onBannerChangeListener = new BannerPager.SimpleBannerChangeListener() {
                    @Override
                    public void onChanged(int itemCount) {
                        setCount(itemCount);
                    }

                    @Override
                    public void onPageSelected(int position) {
                        setSelectPosition(position);
                    }
                };
            }
            this.bannerPager.addOnBannerChangeListener(this.onBannerChangeListener);
        } else {
            this.bannerPager = null;
        }
    }

}
