package you.chen.bannerpage;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import you.chen.bannerlibrary.BannerIndicator;
import you.chen.bannerlibrary.BannerPager;
import you.chen.bannerlibrary.transformer.DepthPageTransformer;
import you.chen.bannerlibrary.transformer.ZoomOutPageTransformer;
import you.chen.bannerpage.adapter.TestAdapter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    BannerPager bp_s;
    BannerIndicator bi_s;


    BannerPager bp;
    BannerIndicator bi;
    TestAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bp_s = findViewById(R.id.bp_s);
        bi_s = findViewById(R.id.bi_s);
        TestAdapter adapter_s = new TestAdapter(this);
        adapter_s.setNewDatas(BannerBean.test2());
        bp_s.setAdapter(adapter_s);
        bp_s.setPageTransformer(new DepthPageTransformer());
        bi_s.setupWithBannerPager(bp_s);

        bp = findViewById(R.id.bp);
        bi = findViewById(R.id.bi);
        adapter = new TestAdapter(this);
        bp.setAdapter(adapter);
        bp.setPageTransformer(new ZoomOutPageTransformer());
        bi.setupWithBannerPager(bp);

        findViewById(R.id.bt1).setOnClickListener(this);
        findViewById(R.id.bt2).setOnClickListener(this);
        findViewById(R.id.bt3).setOnClickListener(this);
        findViewById(R.id.bt4).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bp.startAutoRunning();
        bp_s.startAutoRunning();
    }

    @Override
    protected void onPause() {
        super.onPause();
        bp_s.stopAutoRunning();
        bp.stopAutoRunning();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bi_s.setupWithBannerPager(null);
        bi.setupWithBannerPager(null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt1:
                adapter.setNewDatas(BannerBean.test1());
                break;
            case R.id.bt2:
                adapter.setNewDatas(BannerBean.test2());
                break;
            case R.id.bt3:
                adapter.setNewDatas(null);
                break;
            case R.id.bt4:
                Activity2.lanuch(this);
                break;
        }
    }

}
