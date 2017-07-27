package jenice.wechatdemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jauker.widget.BadgeView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mDatas;  //存放fragment
    private TextView tv_main_chat,tv_main_friends,tv_main_contact;
    private LinearLayout ll_MainChat;
    private ImageView ivTabLine;
    private int mTablineLength;     //指定下划线为屏幕长度的三分之一
    private int mCurrentPageIndex;  //记录当前viewpager的index

    private BadgeView mBadgeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化下划线的宽度
        initTabLine();
        //初始化viewPager
        initView();
    }

    /**
     * 初始化viewPager，并完成数据填写以及适配器的定义，并为viewpager设置适配器。
     */
    private void initView(){
        mViewPager = (ViewPager) findViewById(R.id.viewPager_main);
        tv_main_chat = (TextView) findViewById(R.id.tv_main_section_1);
        tv_main_friends = (TextView) findViewById(R.id.tv_main_section_2);
        tv_main_contact = (TextView) findViewById(R.id.tv_main_section_3);
        ll_MainChat = (LinearLayout) findViewById(R.id.ll_main_chat);

        mDatas = new ArrayList<Fragment>();

        //初始化fragment，并将其加入到存放fragment的list之中
        ChatFragment chatFragment = new ChatFragment();
        FriendsFragment friendsFragment = new FriendsFragment();
        ContactFragment contactFragment = new ContactFragment();
        mDatas.add(chatFragment);
        mDatas.add(friendsFragment);
        mDatas.add(contactFragment);

        //FragmentPagerAdapter 重写方法
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()){
            @Override
            public Fragment getItem(int position) {
                return mDatas.get(position);
            }
            @Override
            public int getCount() {
                return mDatas.size();
            }
        };

        //为ViewPager设置适配器
        mViewPager.setAdapter(mAdapter);

        //当viewPager改变时，改变字体颜色和下划线位置
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //实现滑动的动画效果
            //原理如下：滑动过程中offset的值不断改变，即tabline的marginleft的值不断改变，极端比例即可。
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) ivTabLine.getLayoutParams();

                if (mCurrentPageIndex == 0 && position == 0){   //0--->1
                    lp.leftMargin = (int) ((positionOffset + mCurrentPageIndex) * mTablineLength);
                }else if(mCurrentPageIndex == 1 && position == 0){  //1---->0
                    lp.leftMargin = (int) ((mCurrentPageIndex  + positionOffset - 1) * mTablineLength);
                }else if(mCurrentPageIndex == 1 && position == 1) {   //1--->2
                    lp.leftMargin = (int) ((positionOffset + mCurrentPageIndex) * mTablineLength);
                }else if(mCurrentPageIndex ==2 && position == 1){   //2-->1
                    lp.leftMargin = (int) ((mCurrentPageIndex  + positionOffset - 1) * mTablineLength);
                }
                ivTabLine.setLayoutParams(lp);
            }

            //页面滑动结束的事件
            @Override
            public void onPageSelected(int position) {
                resetTextView();
                switch (position){
                    case 0:
                        //设置消息提示数
                        if(mBadgeView!=null){
                            ll_MainChat.removeView(mBadgeView);
                        }
                        mBadgeView = new BadgeView(MainActivity.this);
                        mBadgeView.setBadgeCount(7);
                        ll_MainChat.addView(mBadgeView);

                        tv_main_chat.setTextColor(getResources().getColor(R.color.mainColor));
                        break;
                    case 1:
                        tv_main_friends.setTextColor(getResources().getColor(R.color.mainColor));
                        break;
                    case 2:
                        tv_main_contact.setTextColor(getResources().getColor(R.color.mainColor));
                        break;
                }
                mCurrentPageIndex = position;   //滑动结束后改变viewpager的index值
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void resetTextView() {
        tv_main_chat.setTextColor(Color.BLACK);
        tv_main_friends.setTextColor(Color.BLACK);
        tv_main_contact.setTextColor(Color.BLACK);
    }

    //获取屏幕的宽度，以便计算下划线的长度
    private void initTabLine(){
        ivTabLine = (ImageView) findViewById(R.id.iv_tabline);
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        mTablineLength = outMetrics.widthPixels / 3;
        //为下划线设置宽度
        ViewGroup.LayoutParams lp = ivTabLine.getLayoutParams();
        lp.width = mTablineLength;
        ivTabLine.setLayoutParams(lp);
    }
}

