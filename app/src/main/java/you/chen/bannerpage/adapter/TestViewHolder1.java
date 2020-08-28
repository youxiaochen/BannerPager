package you.chen.bannerpage.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import you.chen.bannerlibrary.BannerViewHolder;
import you.chen.bannerpage.R;

public class TestViewHolder1 extends BannerViewHolder {

    TextView tv1;

    ImageView iv1;

    public TestViewHolder1(View itemView) {
        super(itemView);
        tv1 = itemView.findViewById(R.id.tv1);
        iv1 = itemView.findViewById(R.id.iv1);
    }

}