package com.example.listviewtest;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.provider.Contacts;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.stetho.Stetho;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.R.attr.label;
import static com.example.listviewtest.R.id.TP4;
import static com.example.listviewtest.R.layout.activity_music;
import static com.example.listviewtest.R.layout.listview;
import static com.example.listviewtest.R.layout.popup;
import static com.example.listviewtest.R.layout.popupadd;
import static com.example.listviewtest.R.layout.popupm;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private DrawerLayout mDrawerLayout;
    /*private String[] player = {"勒布朗","詹姆斯","欧文","乐福","史密斯","汤普森","德拉维多瓦","弗莱","香波特",
                               "莫兹戈夫","小莫","杰弗森","邓台","琼斯","泰伦卢","麦克雷","韦德",
                              "科沃尔","奥斯曼","小南斯","罗斯"};*/
    private List<Player> playerList = new ArrayList<>();
    private Player[] players = {new Player("詹姆斯", R.drawable.icon_head1),
            new Player("欧文", R.drawable.icon_header2),
            new Player("乐福", R.drawable.icon_header3),
            new Player("史密斯", R.drawable.icon_image),
            new Player("汤普森", R.drawable.icon_header4),
            new Player("香波特", R.drawable.icon_header5),
            new Player("科沃尔", R.drawable.icon_head1)};
    private long mExitTime = System.currentTimeMillis();
    private SearchView searchView;
    private SwipeRefreshLayout swipeRefresh;
    private Animation animation;
    private LayoutAnimationController controller;
    private PopupWindow mpopuowindow;
    private PopupWindow menupopup,addpopup;
    private IntentFilter intentFilter;
    private NetworkChangReceiver networkChangReceiver;
    private android.support.design.widget.FloatingActionButton flo;
    private EditText edgirl;
    public static final int TAKE_PHOTO=1;
    private Uri imageUri;
    private CircleImageView iconimage;
    private ImageView picturet;
    private View view1,view2,view3;
    private ViewPager viewPager;
    private List<View> viewList;
    private Toolbar toolbar;
    private Vibrator vibrator;
    private DownloadService.DownloadBinder downloadBinder;
    private ServiceConnection connection =new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downloadBinder = (DownloadService.DownloadBinder) service;
          }
        @Override
        public void onServiceDisconnected(ComponentName name) {
          }
        };
    boolean isChang=false;
    private Toolbar cl;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }

        setContentView(R.layout.activity_main);
     /*   initView();
        initData();
        initEvent();*/
        initplayers();
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
        final Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(this);
        Button trends = (Button) findViewById(R.id.trends);
        trends.setOnClickListener(this);
        final Button Image = (Button) findViewById(R.id.Image);
        Image.setOnClickListener(this);
        Button music = (Button) findViewById(R.id.music);
        music.setOnClickListener(this);
        Button near = (Button) findViewById(R.id.near);
        near.setOnClickListener(this);
        Button shop = (Button) findViewById(R.id.shop);
        shop.setOnClickListener(this);
        Button reader = (Button) findViewById(R.id.reader);
        reader.setOnClickListener(this);
        ImageView add=(ImageView) findViewById(R.id.add);
        add.setOnClickListener(this);
        TextView chang=(TextView) findViewById(R.id.chang);
        chang.setOnClickListener(this);
        Button takePhoto=(Button) findViewById(R.id.take_photo);
        takePhoto.setOnClickListener(this);
        CircleImageView iconimage=(CircleImageView) findViewById(R.id.icon_image);
        iconimage.setOnClickListener(this);
        /*ImageView picturet=(ImageView) findViewById(R.id.picturet);
        picturet.setOnClickListener(this);*/
        Button pagerc=(Button) findViewById(R.id.pagerc);
        pagerc.setOnClickListener(this);ImageView imagetab2=(ImageView) findViewById(R.id.imagetab2);
        imagetab2.setOnClickListener(this);
        ImageView imagetab1=(ImageView) findViewById(R.id.imagetab1);
        imagetab1.setOnClickListener(this);
        Button TP3=(Button) findViewById(R.id.TP3);
        TP3.setOnClickListener(this);
        TextView texttab=(TextView) findViewById(R.id.texttab);
        texttab.setOnClickListener(this);
        Button friend=(Button)  findViewById(R.id.friend);
        friend.setOnClickListener(this);
        TextView attention=(TextView) findViewById(R.id.Attention);
        attention.setOnClickListener(this);
        TextView fans=(TextView) findViewById(R.id.Fans);
        fans.setOnClickListener(this);
        TextView visitors=(TextView) findViewById(R.id.Visitors);
        visitors.setOnClickListener(this);
        TextView say=(TextView) findViewById(R.id.Say);
        say.setOnClickListener(this);
        Button collection=(Button)  findViewById(R.id.Collection);
        collection.setOnClickListener(this);
        Button history=(Button)  findViewById(R.id.History);
        history.setOnClickListener(this);
        Button gift=(Button)  findViewById(R.id.Gift);
        gift.setOnClickListener(this);
        Button remind=(Button)  findViewById(R.id.Remind);
        remind.setOnClickListener(this);
        Button house=(Button)  findViewById(R.id.black_house);
        house.setOnClickListener(this);
        Button help=(Button)  findViewById(R.id.Help);
        help.setOnClickListener(this);
        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        Toolbar toolbattab2=(Toolbar) findViewById(R.id.toolbartab2);
        Toolbar toolbartab3=(Toolbar) findViewById(R.id.toolbartab3);
        Toolbar toolbartab4=(Toolbar) findViewById(R.id.toolbartab4);
        edgirl=(EditText) findViewById(R.id.edgirl);
        viewPager=(ViewPager) findViewById(R.id.viewPage);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        FloatingActionButton flo=(FloatingActionButton) findViewById(R.id.flo);
        flo.setOnClickListener(this);
        Intent intent=new Intent(MainActivity.this,DownloadService.class);
        startService(intent);
        bindService(intent,connection,BIND_AUTO_CREATE);
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        final NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        final ActionBar actionBar = getSupportActionBar();
        setSupportActionBar(toolbartab4);
        setSupportActionBar(toolbattab2);
        setSupportActionBar(toolbartab3);
        setSupportActionBar(toolbar);
        /*if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.id.imagetab1);
        }*/

        TabHost tab = (TabHost) findViewById(R.id.tabhost);
        tab.setup();
        TabHost.TabSpec specOne = tab.newTabSpec("选项卡一");
        specOne.setIndicator(composeLayout("消息", R.drawable.tab_ico_message_off));
        specOne.setContent(R.id.tab1);
        tab.addTab(specOne);
        TabHost.TabSpec specTwo = tab.newTabSpec("选项卡二");
        specTwo.setIndicator(composeLayout("好友", R.drawable.tab_ico_me_off));
        specTwo.setContent(R.id.tab2);
        tab.addTab(specTwo);
        TabHost.TabSpec specThree = tab.newTabSpec("选项卡三");
        specThree.setIndicator(composeLayout("动态", R.drawable.tab_ico_found_off));
        specThree.setContent(R.id.tab3);
        tab.addTab(specThree);
        TabHost.TabSpec specFour = tab.newTabSpec("选项卡四");
        specFour.setIndicator(composeLayout("我的", R.drawable.tab_ico_home_off));
        specFour.setContent(R.id.tab4);
        tab.addTab(specFour);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                mDrawerLayout.closeDrawers();
                switch (item.getItemId()) {
                    case R.id.call:
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        startActivity(intent);
                }
                switch (item.getItemId()) {
                    case R.id.friends:
                        Intent intent = new Intent();
                        intent.setAction(intent.ACTION_VIEW);
                        intent.setData(Contacts.People.CONTENT_URI);
                        startActivity(intent);
                }
                switch (item.getItemId()) {
                    case R.id.location:
                        Uri uri = Uri.parse("geo:38.899533,-77.036476");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                }
                switch (item.getItemId()) {
                    case R.id.mail:
                        Uri uri = Uri.parse("mailto:1309750283@qq.com");
                        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                        startActivity(intent);
                }
                switch (item.getItemId()) {
                    case R.id.setting:
                        Intent intent = new Intent(Settings.ACTION_SETTINGS);
                        startActivity(intent);
                }
                return true;
            }
        });
        initplayers();
        final PlayerAdapter adapter = new PlayerAdapter(MainActivity.this, R.layout.player_item, playerList);
        final ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        animation = new AlphaAnimation(0f, 1f);
        animation.setDuration(200);
        controller = new LayoutAnimationController(animation, 1f);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
        listView.setLayoutAnimation(controller);
        adapter.notifyDataSetChanged();
      /*final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                MainActivity.this, android.R.layout.simple_list_item_1, player
        );
        final ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);*/
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.blue);
        swipeRefresh.setSize(SwipeRefreshLayout.DEFAULT);
        swipeRefresh.setProgressBackgroundColor(R.color.yellow);
        swipeRefresh.setProgressViewEndTarget(true, 140);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(MainActivity.this, "需要长按才能进去！", Toast.LENGTH_LONG).show();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("通知");
                builder.setMessage("你现在将查看ta的资料");
                builder.setPositiveButton("是的", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.this, means.class);
                        startActivity(intent);
                        /*overridePendingTransition(R.anim.disappear_top_left_in,
                                R.anim.disappear_top_left_out);*/
                    }
                });
                builder.setNegativeButton("不了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setCancelable(false);
                builder.show();
                return false;
            }
        });
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshPlayers();
            }
            private void refreshPlayers() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                initplayers();
                                adapter.notifyDataSetChanged();
                                swipeRefresh.setRefreshing(false);
                                Toast.makeText(MainActivity.this, "刷新成功", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }).start();
            }
        });
        intentFilter =new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangReceiver=new NetworkChangReceiver();
        registerReceiver(networkChangReceiver,intentFilter);
        TabLayout tabLayout=(TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("消息"));
        tabLayout.addTab(tabLayout.newTab().setText("关注"));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addTab(tabLayout.newTab().setText("消息"));
        tabLayout.addTab(tabLayout.newTab().setText("关注"));
        LayoutInflater inflater=getLayoutInflater();
        view1=inflater.inflate(R.layout.tab,null);
        view2=inflater.inflate(R.layout.tab2,null);
        viewList=new ArrayList<View>();
        viewList.add(view1);
        viewList.add(view2);
        PagerAdapter pagerAdapter=new PagerAdapter() {
            @Override
            public boolean isViewFromObject(View arg0,Object arg1){
                return arg0==arg1;
            }
            @Override
            public int getCount() {
                return viewList.size();
            }
            @Override
            public void destroyItem(ViewGroup container,int position,Object object){
                container.removeView(viewList.get(position));
            }
            @Override
            public Object instantiateItem(ViewGroup container, final int position){
                container.addView(viewList.get(position));
                final Button pagerb=(Button) findViewById(R.id.pagerb);
                final EditText asearch=(EditText) findViewById(R.id.asearch);
                Button dope = (Button) findViewById(R.id.dope);
                Button dopel = (Button) findViewById(R.id.dopel);
                Button dopej = (Button) findViewById(R.id.dopej);
                pagerb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String str= asearch.getText().toString();
                        if (TextUtils.isEmpty(str)){
                            Toast.makeText(MainActivity.this,"请输入关键字进行搜索",Toast.LENGTH_SHORT).show();

                        }else {
                            Toast.makeText(MainActivity.this,"你输入的是"+str,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dope.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(MainActivity.this,SecondActivity.class);
                        startActivity(intent);
                    }
                });
                dopel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(MainActivity.this,SecondActivity.class);
                        startActivity(intent);
                    }
                });
                dopej.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(MainActivity.this,SecondActivity.class);
                        startActivity(intent);
                    }
                });
                return viewList.get(position);
            }

        };
        viewPager.setAdapter(pagerAdapter);
        imagetab1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                vibrator.vibrate(20);
                showmenupopup(v);
                return false;
            }
        });
        imagetab2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                vibrator.vibrate(20);
                showmenupopup(v);
                return false;
            }
        });
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        unregisterReceiver(networkChangReceiver);
        String inputText=edgirl.getText().toString();
        unbindService(connection);
        save(inputText);
    }
    class NetworkChangReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {
                Toast.makeText(context, "当前已经连接网络", Toast.LENGTH_SHORT).show();
            } else {
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("通知");
                builder.setMessage("您当前处于断网状态，请连接网络后重试");
                builder.setPositiveButton("连接", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_SETTINGS);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("重试", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setCancelable(false);
                builder.show();
            }
        }
    }
        private void initplayers() {
            playerList.clear();
            for (int i = 0; i < 50; i++) {
                Random random = new Random();
                int index = random.nextInt(players.length);
                playerList.add(players[index]);
            }
        }
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case android.R.id.home:
                    mDrawerLayout.openDrawer(GravityCompat.START);
                    break;
                case R.id.weather:
                    Intent intent = new Intent(MainActivity.this, weather.class);
                    PendingIntent pi = PendingIntent.getActivity(MainActivity.this, 0, intent, 0);
                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    Notification notification = new NotificationCompat.Builder(this)
                            .setContentTitle("通知")
                            .setContentText("天气更新啦！")
                            .setWhen(System.currentTimeMillis())
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon_logo))
                            .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory
                                    .decodeResource(getResources(), R.drawable.bg_5)))
                            .setPriority(NotificationCompat.PRIORITY_MAX)
                            .setContentIntent(pi)
                            .setDefaults(NotificationCompat.DEFAULT_ALL)
                            .setLights(Color.WHITE, 1000, 1000)
                            .setAutoCancel(true)
                            .build();
                    manager.notify(1, notification);
                    Intent intent2 = new Intent(MainActivity.this, weather.class);
                    startActivity(intent2);
                    Toast.makeText(MainActivity.this, "看下通知栏吧。", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.login:
                    String url = "https://share.weiyun.com/5ZBEbR7";
                    downloadBinder.startDownload(url);
                    break;
                case R.id.web_view:
                    Intent intent1 = new Intent(MainActivity.this, webview.class);
                    startActivity(intent1);
                    break;
                case R.id.updata:
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                    builder1.setTitle("更新日志");
                    builder1.setMessage("此次更新更换了主界面的部分布局，定制了每个tab的toolbar，" +
                            "加入了有意思的夜间模式和长按头像切换账号功能，优化了过度动画和美化了部分布局！");
                    builder1.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder1.setCancelable(false);
                    builder1.show();
                    break;
                default:
            }
           return true;
        }
    public void onRequestPermissionsResult(int requestCode,String[] permissions,
                                          int[] grantResults){
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "拒绝权限将无法使用程序", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    /*@Override
     public boolean onCreateOptionsMenu(Menu menu) {
         getMenuInflater().inflate(R.menu.main, menu);
         getMenuInflater().inflate(R.menu.search, menu);
         MenuItem menuItem = menu.findItem(R.id.search);
         searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
         searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
             @Override
             public boolean onQueryTextSubmit(String query) {
                 Toast t = Toast.makeText(MainActivity.this, query, Toast.LENGTH_SHORT);
                 t.setGravity(Gravity.TOP, 0, 0);
                 t.show();
                 return false;
             }
                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
            MenuItem shareItem = menu.findItem(R.id.share);
            ShareActionProvider shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
            shareActionProvider.setShareIntent(getDefaultIntent());
            return super.onCreateOptionsMenu(menu);
        }
        private Intent getDefaultIntent() {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("image/*");
            return intent;
        }*/

        private void toast(String content) {

            Toast.makeText(getApplicationContext(), content, Toast.LENGTH_SHORT).show();
        }

        public void onBackPressed() {
            if (System.currentTimeMillis() - mExitTime < 800) {
                MainActivity.this.finish();
            } else {
                toast("再按返回键退出！");
                mExitTime = System.currentTimeMillis();
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button1:
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("通知");
                    builder.setMessage("您确定退出吗？");
                    builder.setPositiveButton("是的", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.this.finish();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                    break;
                /*case R.id.dope:
                    Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.unzoom_in,
                        R.anim.unzoom_out);

                    break;
                case R.id.dopel:
                    Intent intent1 = new Intent(MainActivity.this, SecondActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.dopej:
                    Intent intent3 = new Intent(MainActivity.this, SecondActivity.class);
                    startActivity(intent3);
                    break;*/
                case R.id.trends:
                    Intent intent2 = new Intent(MainActivity.this, trends.class);
                    startActivity(intent2);
                    break;
                case R.id.button:
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                    builder1.setTitle("关于");
                    builder1.setMessage("此软件完全出于热爱，" +
                            "历经48天，期间大大小小修改了172个版本，现基本框架已大致完成！");
                    builder1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder1.setCancelable(false);
                    builder1.show();
                    break;
                case R.id.Image:
                    Intent intent4 = new Intent(MainActivity.this, Image.class);
                    startActivity(intent4);
                    break;
                case R.id.music:
                    Intent intent5 = new Intent(MainActivity.this, Music.class);
                    startActivity(intent5);
                    break;
                /*Intent intent=new Intent(Intent.ACTION_VIEW);
                Uri uri=Uri.parse("file:///music.mp3");
                intent.setDataAndType(uri,"audio/mp3");
                startActivity(intent);*/

                case R.id.near:
                    Uri uri = Uri.parse("geo:38.899533,-77.036476");
                    Intent intent6 = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent6);
                    break;
                case R.id.shop:
                    Intent intent7 = new Intent(MainActivity.this, shopping.class);
                    startActivity(intent7);
                    break;
                case R.id.reader:
                    Intent intent8 = new Intent(MainActivity.this, reader.class);
                    startActivity(intent8);
                    break;
                case R.id.add:
                    showaddpopup(v);
                    break;
                case R.id.TP1:
                    WindowManager.LayoutParams mp=getWindow().getAttributes();
                    mp.alpha= (float) 0.5;
                    getWindow().setAttributes(mp);
                    mpopuowindow.dismiss();
                    break;
                case R.id.TP2:
                    WindowManager.LayoutParams mp1=getWindow().getAttributes();
                    mp1.alpha= (float) 1;
                    getWindow().setAttributes(mp1);
                    mpopuowindow.dismiss();
                    break;
                case R.id.TP3:
                    Intent intent9=new Intent("com.example.broadcastbestpractice.FORCE_OFFLINE");
                    sendBroadcast(intent9);
                    break;
                case R.id.TP4:
                    cl.setBackground(getResources().getDrawable(R.drawable.bg_main));
                    mpopuowindow.dismiss();
                    break;
                case R.id.flo:
                    Intent intent13=new Intent(MainActivity.this,Music.class);
                    startActivity(intent13);
                    overridePendingTransition(R.anim.flip_vertical_in, R.anim.flip_vertical_out);
                    break;
                case R.id.take_photo:
                    File outputImage=new File(getExternalCacheDir(),"output_image.jpg");
                    try{
                        if (outputImage.exists()) {
                            outputImage.delete();
                        }
                        outputImage.createNewFile();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    if (Build.VERSION.SDK_INT>=24){
                        imageUri= FileProvider.getUriForFile(MainActivity.this,"com.example.listviewtest.fileprovider",outputImage);
                    }else {
                        imageUri=Uri.fromFile(outputImage);
                    }
                    Intent intent10=new Intent("android.media.action.IMAGE_CAPTURE");
                    intent10.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                    startActivity(intent10);
                    break;
                case R.id.pagerc:
                    EditText bsearch=(EditText) findViewById(R.id.bsearch);
                    bsearch.setOnClickListener(this);
                    String str= bsearch.getText().toString();
                    if (TextUtils.isEmpty(str)){
                        Toast.makeText(MainActivity.this,"请输入关键字进行搜索",Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(MainActivity.this,"你输入的是"+str,Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.chang:
                    showPopupWindow(v);
                    break;
                case R.id.imagetab2:
                    mDrawerLayout.openDrawer(GravityCompat.START);
                    break;
                case R.id.imagetab1:
                    mDrawerLayout.openDrawer(GravityCompat.START);
                    break;
                case R.id.cut:
                    Intent intent11=new Intent(MainActivity.this,Login.class);
                    startActivity(intent11);
                    finish();
                    break;
                case R.id.texttab:
                    showtab(v);
                case R.id.friend:

                    break;
                case R.id.addfriend:
                    int ed_addac=1;
                    if (ed_addac ==1)
                        Toast.makeText(MainActivity.this,"请输入账号或者密码",Toast.LENGTH_SHORT).show();
                    else {
                        Toast.makeText(MainActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.Attention:
                    Toast.makeText(MainActivity.this,"此功能开发中...",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.Fans:
                    Toast.makeText(MainActivity.this,"此功能开发中...",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.Visitors:
                    Toast.makeText(MainActivity.this,"此功能开发中...",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.Say:
                    Toast.makeText(MainActivity.this,"此功能开发中...",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.Collection:
                    Toast.makeText(MainActivity.this,"此功能开发中...",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.History:
                    Toast.makeText(MainActivity.this,"此功能开发中...",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.Gift:
                    Toast.makeText(MainActivity.this,"此功能开发中...",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.Remind:
                    Toast.makeText(MainActivity.this,"此功能开发中...",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.Help:
                    Toast.makeText(MainActivity.this,"此功能开发中...",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.black_house:
                    Toast.makeText(MainActivity.this,"此功能开发中...",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.contacts:
                    Intent intent12=new Intent(MainActivity.this,contacts.class);
                    startActivity(intent12);
                    break;
                default:
                    break;
            }
        }

    private void showPopupWindow(View view) {
            View contentView = LayoutInflater.from(MainActivity.this).inflate(popup, null);
            mpopuowindow = new PopupWindow(contentView,
                    DrawerLayout.LayoutParams.MATCH_PARENT, DrawerLayout.LayoutParams.WRAP_CONTENT, true);
            mpopuowindow.setContentView(contentView);
            mpopuowindow.setAnimationStyle(R.style.popupwindow);
            View rootview = LayoutInflater.from(MainActivity.this).inflate(popup, null);
            mpopuowindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 120);
            WindowManager.LayoutParams mp=getWindow().getAttributes();
            mp.alpha= (float) 0.7;
            getWindow().setAttributes(mp);
            mpopuowindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams mp=getWindow().getAttributes();
                if (mp.alpha !=0.5){
                    mp.alpha=(float) 1;
                }else {
                    mp.alpha=(float) 0.5;
                }
                getWindow().setAttributes(mp);
            }
        });
            TextView TP1 = (TextView) contentView.findViewById(R.id.TP1);
            TP1.setOnClickListener(this);
            TextView TP2 = (TextView) contentView.findViewById(R.id.TP2);
            TP2.setOnClickListener(this);
            TextView TP4 = (TextView) contentView.findViewById(R.id.TP4);
            TP4.setOnClickListener(this);
        }
    private void showmenupopup(View view) {
        View contentView = LayoutInflater.from(MainActivity.this).inflate(popupm, null);
        menupopup = new PopupWindow(contentView,
                DrawerLayout.LayoutParams.WRAP_CONTENT, DrawerLayout.LayoutParams.WRAP_CONTENT, true);
        menupopup.setContentView(contentView);
        menupopup.setAnimationStyle(R.style.popupmwindow);
        View rootview = LayoutInflater.from(MainActivity.this).inflate(popupm, null);
        menupopup.showAtLocation(rootview, Gravity.NO_GRAVITY, 50,230);
            WindowManager.LayoutParams mp=getWindow().getAttributes();
            mp.alpha= (float) 0.7;
            getWindow().setAttributes(mp);
        menupopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams mp=getWindow().getAttributes();
                mp.alpha= (float) 1;
                getWindow().setAttributes(mp);
            }
        });
        TextView cut=(TextView) contentView.findViewById(R.id.cut);
        cut.setOnClickListener(this);
    }
    private void showaddpopup(View view) {
        View contentView = LayoutInflater.from(MainActivity.this).inflate(popupadd, null);
        addpopup = new PopupWindow(contentView,
                DrawerLayout.LayoutParams.WRAP_CONTENT, DrawerLayout.LayoutParams.WRAP_CONTENT, true);
        addpopup.setContentView(contentView);
        addpopup.setAnimationStyle(R.style.popupwindow);
        View rootview = LayoutInflater.from(MainActivity.this).inflate(popupadd, null);
        addpopup.showAtLocation(rootview, Gravity.CENTER,0,0);
        WindowManager.LayoutParams mp=getWindow().getAttributes();
        mp.alpha= (float) 0.7;
        getWindow().setAttributes(mp);
        addpopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams mp=getWindow().getAttributes();
                mp.alpha= (float) 1;
                getWindow().setAttributes(mp);
            }
        });
        EditText edac=(EditText) contentView.findViewById(R.id.ed_addac);
        edac.setOnClickListener(this);
        Button addfriend=(Button) contentView.findViewById(R.id.addfriend);
        addfriend.setOnClickListener(this);
        Button contacts=(Button) contentView.findViewById(R.id.contacts);
        contacts.setOnClickListener(this);
    }
    private void showtab(View view) {
       view=View.inflate(getApplicationContext(),R.layout.tab,null);
    }
    public void save(String inputText){
        FileOutputStream out=null;
        BufferedWriter writer=null;
        try {
            out=openFileOutput("data",Context.MODE_PRIVATE);
            writer=new BufferedWriter(new OutputStreamWriter(out));
            writer.write(inputText);
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                if (writer!=null){
                    writer.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode,int result,Intent data){
        switch (requestCode){
            case TAKE_PHOTO:
                if (requestCode==RESULT_OK){
                    try {
                        Bitmap bitmap=BitmapFactory.decodeStream(getContentResolver()
                        .openInputStream(imageUri));
                        iconimage.setImageBitmap(bitmap);
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }
    public View composeLayout(String s, int i) {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        ImageView iv = new ImageView(this);
        iv.setImageResource(i);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.WRAP_CONTENT,
        LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(95,20, 0, 0);
        layout.addView(iv, lp);
        TextView tv = new TextView(this);
        tv.setGravity(Gravity.CENTER);
        tv.setSingleLine(true);
        tv.setText(s);
        tv.setTextColor(Color.BLACK);
        tv.setTextSize(14);
        tv.setPadding(85, 5, 0, 0);
        layout.addView(tv, new LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.WRAP_CONTENT,
        LinearLayout.LayoutParams.WRAP_CONTENT));
        return layout;
    }
}
    /*private void openApp(String str){
        Intent mainIntent=new Intent(Intent.ACTION_MAIN.null);
        mainIntent.addCategory((Intent.CATEGORY_LAUNCHER);
        PackageManager mPackageManager=getApplicationContext().getPackageManager();
        List<ResolveInfo> mAllApps=mPackageManager.queryIntentActivities(mainIntent,0);
        Collections.sort(mAllApps,new ResolveInfo(.DisplayNameComparator(mPackageManager));
    }*/





