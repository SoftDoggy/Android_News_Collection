package com.example.administrator.yyjapplication;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //private TextView mTextMessage;
   // private TextView detail1,detail2,detail3,detail4,detail5,detail6,detail7,detail8,detail9;
    private ListView list;
    //private List<info> mlistInfo = new ArrayList<info>();
    //private SQLiteDatabase db ;
    //private Cursor cursor;
//    private DBManager dbManager;
//    private ListView listView;
    private WebView wv;
    private List<String> data=new ArrayList<String>();
    private List<String> detailUrl=new ArrayList<String>();


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mTextMessage = (TextView) findViewById(R.id.detail);
//        detail1 = (TextView) findViewById(R.id.detail1);
//        detail2 = (TextView) findViewById(R.id.detail2);
//        detail3 = (TextView) findViewById(R.id.detail3);
//        detail4 = (TextView) findViewById(R.id.detail4);
//        detail5 = (TextView) findViewById(R.id.detail5);
//        detail6 = (TextView) findViewById(R.id.detail6);
//        detail7 = (TextView) findViewById(R.id.detail7);
        //detail8 = (TextView) findViewById(R.id.detail8);
       // detail9 = (TextView) findViewById(R.id.detail9);
        wv=(WebView)findViewById(R.id.webView);
        wv.setVisibility(View.INVISIBLE);
        WebSettings webSettings=wv.getSettings();
        webSettings.setJavaScriptEnabled(true);
        //webSettings.setPluginEnable(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        //wv.setVisibility(View.INVISIBLE);
        list=(ListView) findViewById(R.id.list);

        //dbManager = new DBManager(this);

        SQLiteDatabase db=openOrCreateDatabase("test.db", Context.MODE_PRIVATE, null);
        db.execSQL("DROP TABLE IF EXISTS news");
        db.execSQL("DROP TABLE IF EXISTS person");
        db.execSQL("CREATE TABLE person (_id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, age SMALLINT)");
        db.execSQL("insert into person(name, age) values('炸死特', 4)");
        try {
            db.execSQL(DB(1));
            db.execSQL(DB(2));
        }catch (Exception e){
            e.printStackTrace();
        }
        //System.out.println("test2");
        Cursor cursor=db.rawQuery("select * from news", null);
        //Cursor cursor = getContentResolver().query(People.CONTENT_URI, null, null, null, null);

        //startManagingCursor(cursor);
//        startManagingCursor(cursor);



        int i=1;
        while (cursor.moveToNext()) {
            if(i<2){
                i++;
                continue;
            }
            //int personid = cursor.getInt(0); //获取第一列的值,第一列的索引从0开始
            //String name = cursor.getString(1);
            String name=cursor.getString(cursor.getColumnIndex("title"));
            data.add(name);
            String detail=cursor.getString(cursor.getColumnIndex("detail"));
            detailUrl.add(detail);

            //System.out.println("test");
            //int age = cursor.getInt(2);//获取第三列的值
            //detail1.setText(name);
            //new ListAdapter;
        }
        //listview的 适配器
        ListAdapter listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,data);
        //ListAdapter lis =  new ArrayAdapter<>()
        list.setAdapter(listAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                list.setVisibility(View.INVISIBLE);
                wv.setVisibility(View.VISIBLE);
                //wv.loadUrl("https://www.baidu.com");
                wv.loadUrl(detailUrl.get(i));
                wv.setWebViewClient(new WebViewClient(){
//                    @Override
//                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                        // TODO Auto-generated method stub
//                        //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
//                        view.loadUrl("http://www.baidu.com");
//                        return true;
//                    }
                    @Override
                    public void onReceivedSslError(WebView view,
                                                   SslErrorHandler handler, SslError error) {
                        handler.proceed();
                    }
                });
                //Android默认浏览器
//                Uri uri = Uri.parse("http://www.baidu.com");
//                Intent it = new Intent(Intent.ACTION_VIEW, uri);
//                startActivity(it);
//                System.out.println("i:"+i);
//                System.out.println("l:"+l);
            }
        });
        //NewMessageNotification.notify(this,"myNotification",1);
        cursor.close();
        db.close();

        //BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        //navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //开启软件时打开通知
        notification(new View(this.getApplicationContext()));
    }
    //设置返回键功能，如果webview打开就退出，如果没有就退出软件
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if ((keyCode == KeyEvent.KEYCODE_BACK) && wv.canGoBack()) {
//            wv.goBack();
//            return true;
//        }
         if(wv.getVisibility()==View.VISIBLE){
            wv.setVisibility(View.INVISIBLE);
            list.setVisibility(View.VISIBLE);
            return true;
        }
        //return true;
        return super.onKeyDown(keyCode, event);
    }
    //通知
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void notification(View view){
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder =
                new Notification.Builder(this)
                        .setSmallIcon(R.drawable.app_icon)
                        //.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.appIcon))
                        .setContentTitle("热点新闻")
                        .setContentText(data.get(0))
                        .setAutoCancel(true)
                        .setTicker("Eiffai news");
        Intent resultIntent = new Intent(this, MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this,0,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);
//        Notification notification=builder.build();
//        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        manager.notify(0, builder.build());
        builder.setContentText(data.get(1));
        manager.notify(1, builder.build());
        builder.setContentText(data.get(2));
        manager.notify(2, builder.build());

    }
    //此处插入数据库创建表的sql语句
    private String DB(int i){
        String str="CREATE TABLE `news` (\n" +
                "  `id` int(11) NOT NULL,\n" +
                "  `title` varchar(50) NOT NULL,\n" +
                "  `author` varchar(50) NOT NULL,\n" +
                "  `date` date NOT NULL,\n" +
                "  `detail` longtext NOT NULL,\n" +
                "  `picture` text NOT NULL,\n" +
                "  `star` tinyint(1) NOT NULL,\n" +
                "  `class` varchar(50) NOT NULL\n" +
                "); \n";
        String str2="INSERT INTO `news` (`id`, `title`, `author`, `date`, `detail`, `picture`, `star`, `class`) VALUES\n" +
                "(5, '延边战鲁能前领队下课 作为昔日老总曾跪地谢球迷', 'yyj', '2017-05-14', 'http://bdnews.xinmin.cn/baidunews-eco/content/31037761', '', 0, '体育'),\n" +
                "(6, '苏宁发布战恒大海报:从 头 越 欲掀翻中超领头羊', 'yyj', '2017-05-14', 'http://sports.cnhubei.com/2017/0518/358153.shtml', '', 0, '体育'),\n" +
                "(7, '破五万！詹姆斯再迎里程碑 又一个最年轻先生get！', 'yyj', '2017-05-14', 'http://sports.eastday.com/a/170518170325541911926.html', '', 0, '体育'),\n" +
                "(8, '跳水全国冠军赛：张家齐获女子个人全能亚军', 'yyj', '2017-05-14', 'http://bdnews.xinmin.cn/baidunews-eco/content/31037900', '', 0, '体育'),\n" +
                "(11, '中国外交部长会见韩国特使', 'yyj', '2017-05-14', 'http://news.sina.cn/gn/2017-05-18/detail-ifyfkqiv6513492.d.html?vt=4&amp;pos=8&amp;wm=8017_0001&amp;cid=56261&amp;HTTPS=1', '', 0, '军事'),\n" +
                "(12, '自升式海上风电安装平台在广州交付', 'yyj', '2017-05-14', 'http://news.k618.cn/tech/201705/t20170518_11403356.html', '', 0, '军事'),\n" +
                "(13, '美海军陆战队将建立“3D打印微型工厂”变革后', 'yyj', '2017-05-14', 'http://www.itmsc.cn/archives/view-173129-1.html', '', 0, '军事'),\n" +
                "(14, '“未来一年”，中国为什么“不在黄岩岛填海造', 'yyj', '2017-05-14', 'http://www.toutiao.com/a6421321724578497025/', '', 0, '军事'),\n" +
                "(15, '内蒙古那吉林场森林火灾火场东北线明火已扑灭', 'yyj', '2017-05-14', 'http://sports.chinanews.com/sh/2017/05-18/8227859.shtml', '', 0, '国内'),\n" +
                "(16, '中共教育部党组关于李永智同志免职的通知', 'yyj', '2017-05-14', 'http://news.xinhuanet.com/politics/2017-05/18/c_1120996948.htm', '', 0, '国内'),\n" +
                "(17, '国资委公布一批央企任免', 'yyj', '2017-05-14', 'http://news.xinhuanet.com/politics/2017-05/18/c_1120996930.htm', '', 0, '国内'),\n" +
                "(18, '安监总局要求做好夏季防暑降温工作', 'yyj', '2017-05-14', 'http://bdnews.xinmin.cn/baidunews-eco/content/31037946', '', 0, '国内'),\n" +
                "(19, '湖北来凤县获财政部2000万财政管理绩效考核奖', 'yyj', '2017-05-14', 'http://bdnews.xinmin.cn/baidunews-eco/content/31037941', '', 0, '国内'),\n" +
                "(20, '即时新闻', 'yyj', '2017-05-14', 'http://guonei.news.baidu.com/n?cmd=4&class=internews&pn=1', '', 0, '国际'),\n" +
                "(21, '全球青少年每年死亡数超120万 悲剧多可避免', 'yyj', '2017-05-14', 'http://china.caixin.com/2017-05-18/101092149.html#gocomment', '', 0, '国际'),\n" +
                "(22, '菲律宾宣布拒绝欧盟附加条件援助资金', 'yyj', '2017-05-14', 'http://bdnews.xinmin.cn/baidunews-eco/content/31037864', '', 0, '国际'),\n" +
                "(23, '特朗普说愿与朝鲜通过接触实现和平', 'yyj', '2017-05-14', 'http://news.xinhuanet.com/world/2017-05/18/c_1120996613.htm', '', 0, '国际'),\n" +
                "(24, 'RCEP部长级会议将举行 助推谈判尽早结束', 'yyj', '2017-05-14', 'http://finance.chinanews.com/cj/2017/05-18/8227757.shtml', '', 0, '国际'),\n" +
                "(25, '敏感皮肤太磨人？汤臣倍健胶原蛋白粉帮你摆脱', 'yyj', '2017-05-14', 'http://beauty.yxlady.com/201705/1447831.shtml', '', 0, '女人'),\n" +
                "(26, '有选择恐惧症怎么办？ 如何克服选择恐惧症', 'yyj', '2017-05-14', 'http://eladies.sina.com.cn/feel/xinli/2017-05-18/1703/doc-ifyfkqks4278872.shtml', '', 0, '女人'),\n" +
                "(27, '2017吉弗尼（生物护肤品）品牌新品媒体见面会', 'yyj', '2017-05-14', 'http://www.mshishang.com/a/20170518/239427.html', '', 0, '女人'),\n" +
                "(28, '俄罗斯金刚芭比恕我欣赏不来', 'yyj', '2017-05-14', 'http://fashion.sina.cn/newbd/j_baidu.d.html?docId=fyfkqks4263461&amp;wm=3170_9999', '', 0, '女人'),\n" +
                "(29, '特供永久玻尿酸 隆鼻？', 'yyj', '2017-05-14', 'http://eladies.sina.cn/newbd/j_baidu.d.html?docId=fyfecvz1145232&amp;wm=3170_9999', '', 0, '女人'),\n" +
                "(30, '《啪啪S舞》MV首发 王蓉化身舞蹈精灵秀好身材', 'yyj', '2017-05-14', 'http://ent.qq.com/a/20170518/045981.htm', '', 0, '娱乐'),\n" +
                "(31, '模仿王菲？蔡淳佳帅气抵京直言：我就是我', 'yyj', '2017-05-14', 'http://e.gmw.cn/2017-05/18/content_24523316.htm', '', 0, '娱乐'),\n" +
                "(32, '《梦幻佳期》正式改档6月9日', 'yyj', '2017-05-14', 'http://ent.sina.cn/film/chinese/2017-05-18/detail-ifyfkqks4285089.d.html?vt=4&amp;pos=12&amp;HTTPS=1', '', 0, '娱乐'),\n" +
                "(33, '《逆路》5月19日全国公映', 'yyj', '2017-05-14', 'http://ent.sina.cn/film/chinese/2017-05-18/detail-ifyfkqks4284821.d.html?vt=4&amp;pos=12&amp;HTTPS=1', '', 0, '娱乐'),\n" +
                "(34, '网红徐大宝戛纳电影节穿国旗装犯法吗 徐大宝如何', 'yyj', '2017-05-14', 'http://www.91danji.com/news/az189036.html', '', 0, '娱乐'),\n" +
                "(35, '澳大利亚时装周模特走秀 满屏大长腿看花眼', 'yyj', '2017-05-14', 'http://bdnews.xinmin.cn/baidunews-eco/content/31037735', '', 0, '时尚'),\n" +
                "(36, '玩转色彩，感受巴黎时装周装逼指南', 'yyj', '2017-05-14', 'http://www.myzaker.com/article/591d2d4a1bc8e0b647000000/', '', 0, '时尚'),\n" +
                "(37, '带着纯净的爱，去纯净的新西兰', 'yyj', '2017-05-14', 'http://www.myzaker.com/article/591c7ca91bc8e03f01000050/', '', 0, '时尚'),\n" +
                "(38, '凉鞋界的小黑裙 一字带到底有多美', 'yyj', '2017-05-14', 'http://mp.weixin.qq.com/s?timestamp=1461818201&src=3&ver=1&signature=mYuUbGEG1QEbvo5DOS2m0w-GppKl3hfBonCLjw6iPjMEiDLm2LZQSA4mwGi2gDPLH8aWBaMn1jfyNXeG8eI*DhbrzcgN4yRNpJNVQu2laFg1Mv*62Bj93wDE-UgpP5D4cNnA53KpzYSjki5ZekH2O30nxSta4BlOFIW-bz5jupo=', '', 0, '时尚'),\n" +
                "(39, '现在就流行这种“不正经”的牛仔裤', 'yyj', '2017-05-14', 'http://mp.weixin.qq.com/s?__biz=MjM5NzUzNDg2MA==&mid=2651365140&idx=1&sn=9488c8532953c407c3ce4fbfd6cee743&3rd=MzA3MDU4NTYzMw==&scene=6#rd', '', 0, '时尚'),\n" +
                "(40, '这条路轿车要跪，SUV都可能驾驭不了？', 'yyj', '2017-05-14', 'http://www.12gang.com/article_23387.html', '', 0, '汽车'),\n" +
                "(41, '谁说轿车没人爱 紧凑型轿车人气不减', 'yyj', '2017-05-14', 'http://auto.nbd.com.cn/articles/2017-05-18/1107255.html', '', 0, '汽车'),\n" +
                "(42, '2017年4月C级轿车销量排行榜', 'yyj', '2017-05-14', 'http://auto.163.com/17/0518/08/CKN3LEHF000884ML.html', '', 0, '汽车'),\n" +
                "(43, '买辆13万的轿车获赔39万 咋回事？', 'yyj', '2017-05-14', 'http://news.sina.cn/sh/2017-05-18/detail-ifyfkqwe0085319.d.html?vt=4&pos=11&cid=56264&HTTPS=1', '', 0, '汽车'),\n" +
                "(44, '农行山东分行营业部互联网+车展营销硕', 'yyj', '2017-05-14', 'http://news.163.com/17/0518/14/CKNMSLEQ00014AED.html', '', 0, '汽车'),\n" +
                "(45, '被《上古卷轴合集》挡子弹救一命玩家 B社送他大..', 'yyj', '2017-05-14', 'http://news.pconline.com.cn/925/9255208.html', '', 0, '游戏'),\n" +
                "(46, '难以置信！老板竟给8技能须弥宠打高隐', 'yyj', '2017-05-14', 'http://xyq.17173.com/content/05182017/083928271.shtml', '', 0, '游戏'),\n" +
                "(47, '《亮剑》手游公布 国内首款抗日题材手游', 'yyj', '2017-05-14', 'http://news.pconline.com.cn/925/9255207.html', '', 0, '游戏'),\n" +
                "(48, 'PC版荒野大镖客?《狂野西部OL》计划夏季首测', 'yyj', '2017-05-14', 'http://news.17173.com/content/05182017/165511753.shtml', '', 0, '游戏'),\n" +
                "(49, '空姐玩台服王荣耀者被骂坑 晒照后大神抢着带', 'yyj', '2017-05-14', 'http://news.17173.com/content/05182017/150634674_1.shtml', '', 0, '游戏'),\n" +
                "(50, '即时新闻', 'yyj', '2017-05-14', 'http://shehui.news.baidu.com/n?cmd=4&class=socianews&pn=1', '', 0, '社会'),\n" +
                "(51, '湖北襄阳：男子微缩景观寄乡愁 复原老街巷(图', 'yyj', '2017-05-14', 'http://news.jxnews.com.cn/system/2017/05/18/016138842.shtml', '', 0, '社会'),\n" +
                "(52, '8旬母亲锁癫痫儿子进铁笼 喂饭换尿布照顾50年', 'yyj', '2017-05-14', 'http://news.jxnews.com.cn/system/2017/05/18/016138828.shtml', '', 0, '社会'),\n" +
                "(53, '近二十年全国考古成果首博开展', 'yyj', '2017-05-14', 'http://beijing.jinghua.cn/20170518/f299181.shtml', '', 0, '社会'),\n" +
                "(54, '陕西自贸区知识产权工作正式启动(图)', 'yyj', '2017-05-14', 'http://www.sipo.gov.cn/dtxx/gn/2017/201705/t20170518_1311111.html', '', 0, '社会'),\n" +
                "(55, '互联网新闻', 'yyj', '2017-05-14', 'http://tech.baidu.com/n?cmd=1&class=internet&pn=1&from=tab', '', 0, '科技'),\n" +
                "(56, 'ofo携手联合国启动“一公里计划” 全球布局领先..', 'yyj', '2017-05-14', 'http://news.cnair.com/c/201705/79014.html', '', 0, '科技'),\n" +
                "(57, '阿里第四财季营收385.79亿元 净利为104.4亿元', 'yyj', '2017-05-14', 'http://tech.sina.com.cn/i/2017-05-18/doc-ifyfkqks4288303.shtml', '', 0, '科技'),\n" +
                "(58, '超越日货!雷军推广小米电饭煲：京东莫名躺枪', 'yyj', '2017-05-14', 'http://digi.hsw.cn/system/2017/0518/78128.shtml', '', 0, '科技'),\n" +
                "(59, '创新,泰康在线发展之魂 ', 'yyj', '2017-05-14', 'http://insurance.cnfol.com/baoxiandongtai/20170518/24742648.shtml', '', 0, '科技'),\n" +
                "(60, '前4月全国新设外商投资企业9726家 同比增17...', 'yyj', '2017-05-14', 'http://ku.m.chinanews.com/wapapp/baidu/cj/2017/05-18/8227615.shtml', '', 0, '财经'),\n" +
                "(61, '午评：沪指跌0.19%3100点得而复失 环保股逆..', 'yyj', '2017-05-14', 'http://finance.ce.cn/rolling/201705/18/t20170518_22970923.shtml', '', 0, '财经'),\n" +
                "(62, '2016年进口质量安全风险信息9.83万起 同比降..', 'yyj', '2017-05-14', 'http://ku.m.chinanews.com/wapapp/baidu/cj/2017/05-18/8227456.shtml', '', 0, '财经'),\n" +
                "(63, '湖北省首家民营银行众邦银行正式揭牌营业', 'yyj', '2017-05-14', 'http://www.cnhan.com/baidunews-eco/app/56/824/600909.html', '', 0, '财经'),\n" +
                "(64, '收评：沪指跌0.46% 资金谨慎情绪依然高企', 'yyj', '2017-05-14', 'http://finance.ce.cn/rolling/201705/18/t20170518_22980856.shtml', '', 0, '财经');\n" +
                "\n";
        if(i==1)
            return str;
        else if (i==2)
            return str2;
        return str2;
    }

}
