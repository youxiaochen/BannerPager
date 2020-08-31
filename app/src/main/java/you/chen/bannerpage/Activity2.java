package you.chen.bannerpage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import you.chen.bannerlibrary.BannerIndicator;
import you.chen.bannerlibrary.BannerPager;
import you.chen.bannerpage.adapter.TestAdapter;

public class Activity2 extends AppCompatActivity {

    RecyclerView rv;
    LinearLayoutManager lm;
    Test2Adapter adapter;

    public static void lanuch(Context context) {
        context.startActivity(new Intent(context, Activity2.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);
        rv = findViewById(R.id.rv);
        lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);
        adapter = new Test2Adapter(this);
        rv.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        RecyclerView.ViewHolder vh = rv.findViewHolderForAdapterPosition(0);
        if (vh instanceof BannerTestViewHolder) {
            ((BannerTestViewHolder) vh).bp.startAutoRunning();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        RecyclerView.ViewHolder vh = rv.findViewHolderForAdapterPosition(0);
        if (vh instanceof BannerTestViewHolder) {
            ((BannerTestViewHolder) vh).bp.stopAutoRunning();
        }
    }

    private class Test2Adapter extends RecyclerView.Adapter {

        LayoutInflater inflater;
        Context context;

        public Test2Adapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View item;
            if (viewType == 0) {
                item = inflater.inflate(R.layout.item_text, parent, false);
                return new TextViewHolder(item);
            } else {
                item = inflater.inflate(R.layout.public_bannerpager, parent, false);
                return new BannerTestViewHolder(item);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder.getItemViewType() == 0) {
                TextViewHolder vh = (TextViewHolder) holder;
                vh.tv.setText("current position " + position);
            } else {
                BannerTestViewHolder vh = (BannerTestViewHolder) holder;
                if (vh.bp.getAdapter() == null) {
                    TestAdapter adapter = new TestAdapter(context);
                    adapter.setNewDatas(BannerBean.test2());
                    vh.bp.setAdapter(adapter);
                }
            }
        }

        @Override
        public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
            super.onViewAttachedToWindow(holder);
            if (holder.getItemViewType() == 1) {
                BannerTestViewHolder vh = (BannerTestViewHolder) holder;
                vh.bi.setupWithBannerPager(vh.bp);
            }
        }

        @Override
        public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
            super.onViewDetachedFromWindow(holder);
            if (holder.getItemViewType() == 1) {
                BannerTestViewHolder vh = (BannerTestViewHolder) holder;
                vh.bi.setupWithBannerPager(null);
            }
        }

        @Override
        public int getItemCount() {
            return 50;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) return 1;
            return super.getItemViewType(position);
        }

    }

    private class TextViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        public TextViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv);
        }

    }

    private class BannerTestViewHolder extends RecyclerView.ViewHolder {

        BannerPager bp;
        BannerIndicator bi;

        public BannerTestViewHolder(@NonNull View itemView) {
            super(itemView);
            bp = itemView.findViewById(R.id.bp);
            bi = itemView.findViewById(R.id.bi);
            bp.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
            bi.setOrientation(BannerIndicator.ORIENTATION_HORIZONTAL);
        }

    }

}
