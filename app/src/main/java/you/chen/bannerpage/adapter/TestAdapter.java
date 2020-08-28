package you.chen.bannerpage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import you.chen.bannerlibrary.BannerAdapter;
import you.chen.bannerlibrary.BannerViewHolder;
import you.chen.bannerpage.BannerBean;
import you.chen.bannerpage.R;

public class TestAdapter extends BannerAdapter<BannerViewHolder> {

    List<BannerBean> bannerBeanList = new ArrayList<>();

    Context context;

    LayoutInflater inflater;

    public TestAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setNewDatas(List<BannerBean> list) {
        bannerBeanList.clear();
        if (list != null && !list.isEmpty()) {
            bannerBeanList.addAll(list);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getBannerItemCount() {
        return bannerBeanList.size();
    }

    @Override
    public int getBannerItemViewType(int position) {
        return bannerBeanList.get(position).type;
    }

    @Override
    public void onBindBannerViewHolder(BannerViewHolder holder, int bannerPosition) {
        BannerBean bean = bannerBeanList.get(bannerPosition);
        if (holder.getItemViewType() == 0) {
            TestViewHolder1 vh = (TestViewHolder1) holder;
            vh.tv1.setText(bean.des);
            Glide.with(context).load(bean.url).into(vh.iv1);
        } else {
            TestViewHolder2 vh = (TestViewHolder2) holder;
            vh.tv2.setText(bean.des);
            Glide.with(context).load(bean.url).into(vh.iv2);
        }
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item;
        if (viewType == 0) {
            item = inflater.inflate(R.layout.banner_item1, parent, false);
        } else {
            item = inflater.inflate(R.layout.banner_item2, parent, false);
        }
        final BannerViewHolder vh = viewType == 0 ? new TestViewHolder1(item) : new TestViewHolder2(item);
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = getBannerPosition(vh.getAdapterPosition());
                Toast.makeText(context, "click position " + position, Toast.LENGTH_SHORT).show();
            }
        });
        return vh;
    }

}
