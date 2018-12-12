package com.example.listviewtest;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class Image extends AppCompatActivity implements ViewPager.OnPageChangeListener{
    private ViewPager vp;
    private LinearLayout ll_point;
    private TextView tv_desc;
    private int[] imageResIds; //存放图片资源id的数组
    private ArrayList<ImageView> imageViews; //存放图片的集合
    private String[] contentDescs; //图片内容描述
    private int lastPosition;
    private boolean isRunning = false; //viewpager是否在自动轮询

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION| View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_image);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();
        initData();
        initAdapter();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            default:
        }
        return true;
    }
    private void initViews() {
        //初始化放小圆点的控件
        ll_point = (LinearLayout) findViewById(R.id.ll_point);
        //初始化ViewPager控件
        vp = (ViewPager) findViewById(R.id.vp);
        //设置ViewPager的滚动监听
        vp.setOnPageChangeListener(this);
        //显示图片描述信息的控件
        tv_desc = (TextView) findViewById(R.id.tv_desc);
    }

    /*
      初始化数据
     */
    private void initData() {
        imageResIds = new int[]{R.drawable.bg_2,R.drawable.bg_3,R.drawable.bg_4,R.drawable.bg_5};
        contentDescs = new String[]{
                "思考人生",
                "“45度仰望",
                "爱心",
                "夕阳"
        };
        imageViews = new ArrayList<>();
        ImageView imageView;
        View pointView;
        for (int i = 0; i < imageResIds.length; i++){
            imageView = new ImageView(this);
            imageView.setBackgroundResource(imageResIds[i]);
            imageViews.add(imageView);

            //加小白点，指示器（这里的小圆点定义在了drawable下的选择器中了，也可以用小图片代替）
            pointView = new View(this);
            pointView.setBackgroundResource(R.drawable.ai); //使用选择器设置背景
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(8, 8);
            if (i != 0){
                //如果不是第一个点，则设置点的左边距
                layoutParams.leftMargin = 10;
            }
            pointView.setEnabled(false); //默认都是暗色的
            ll_point.addView(pointView, layoutParams);
        }
    }

    private void initAdapter() {
        ll_point.getChildAt(0).setEnabled(true); //初始化控件时，设置第一个小圆点为亮色
        tv_desc.setText(contentDescs[0]); //设置第一个图片对应的文字
        lastPosition = 0; //设置之前的位置为第一个
        vp.setAdapter(new MyPagerAdapter());
        //设置默认显示中间的某个位置（这样可以左右滑动），这个数只有在整数范围内，可以随便设置
        vp.setCurrentItem(4000000); //显示4000000这个位置的图片
    }

    //界面销毁时，停止viewpager的轮询
    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRunning = false;
    }

    /*
      自定义适配器，继承自PagerAdapter
     */
    class MyPagerAdapter extends PagerAdapter{

        //返回显示数据的总条数，为了实现无限循环，把返回的值设置为最大整数
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        //指定复用的判断逻辑，固定写法：view == object
        @Override
        public boolean isViewFromObject(View view, Object object) {
            //当创建新的条目，又反回来，判断view是否可以被复用(即是否存在)
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //container  容器  相当于用来存放imageView
            //从集合中获得图片
            int newPosition = position % 4;
            ImageView imageView = imageViews.get(newPosition);
            container.addView(imageView);
            return imageView;
        }

        //销毁条目
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //object:刚才创建的对象，即要销毁的对象
            container.removeView((View) object);
        }
    }

    //--------------以下是设置ViewPager的滚动监听所需实现的方法--------
    //页面滑动
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    //新的页面被选中
    @Override
    public void onPageSelected(int position) {
        //当前的位置可能很大，为了防止下标越界，对要显示的图片的总数进行取余
        int newPosition = position % 4;
        tv_desc.setText(contentDescs[newPosition]);
        //设置小圆点为高亮或暗色
        ll_point.getChildAt(lastPosition).setEnabled(false);
        ll_point.getChildAt(newPosition).setEnabled(true);
        lastPosition = newPosition; //记录之前的点
    }
    @Override
    public void onPageScrollStateChanged(int state) {

    }
}

