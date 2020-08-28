package you.chen.bannerpage.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import you.chen.bannerlibrary.BannerViewHolder;
import you.chen.bannerpage.R;

public class TestViewHolder2 extends BannerViewHolder {

    TextView tv2;

    ImageView iv2;

    public TestViewHolder2(View itemView) {
        super(itemView);
        tv2 = itemView.findViewById(R.id.tv2);
        iv2 = itemView.findViewById(R.id.iv2);
    }

}
