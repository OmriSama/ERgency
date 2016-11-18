package com.team3.ergency;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.team3.ergency.helper.LaunchManager;

import java.util.Set;

import static android.R.attr.start;
import static android.R.color.transparent;
import static com.team3.ergency.R.id.dots;
import static com.team3.ergency.R.layout.intro_page_three;
import static com.team3.ergency.R.layout.intro_page_two;
import static com.team3.ergency.R.string.join_now_button;

public class MainActivity extends AppCompatActivity {

    private ViewPager view_pager;
    private ViewPagerAdapter view_pager_adapter;

    private int[] layouts;
    private TextView[] dots;
    private LinearLayout dots_layout;
    private LaunchManager launchManager;

    private ImageButton arrow_button;
    private Button join_now_button;
    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            addBottomDots(position);
            if (position == layouts.length - 1) {
                join_now_button.setVisibility(View.GONE);
            } else {
                join_now_button.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        launchManager = new LaunchManager(this);
        // Skip to homepage if user already filled out a profile
        Log.d("Skip screen", launchManager.isProfileFilled()+"");
        if (launchManager.isProfileFilled()) {
            Intent i = new Intent(this, HomepageActivity.class);
            startActivity(i);
            finish();
        }
        else {
            // Skip to personal information form if intro pages already displayed to user
            if (launchManager.isIntroDisplayed()) {
                Intent i = new Intent(this, PersonalInformation.class);
                startActivity(i);
                finish();
            }
            else {
                launchManager.setIntroDisplayed(true);
            }
        }

        /** Set status bar as transparent*/
        if(Build.VERSION.SDK_INT>=21)
        {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        setContentView(R.layout.activity_main);

        view_pager = (ViewPager)findViewById(R.id.viewpager);
        dots_layout = (LinearLayout)findViewById(R.id.dots);

        arrow_button = (ImageButton)findViewById(R.id.arrow_next);
        join_now_button = (Button)findViewById(R.id.join_now);

        layouts = new int[]{R.layout.intro_page_one, R.layout.intro_page_two, R.layout.intro_page_three};

        addBottomDots(0);
        changeStatusBarColor();

        view_pager_adapter = new ViewPagerAdapter();
        view_pager.setAdapter(view_pager_adapter);
        view_pager.addOnPageChangeListener(viewListener);

        join_now_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, PersonalInformation.class);
                startActivity(i);
                finish();
            }
        });

        arrow_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int current = getItem(+1);
                if(current<layouts.length)
                {
                    view_pager.setCurrentItem(current);
                }
                else
                {
                    Intent i = new Intent(MainActivity.this, PersonalInformation.class);
                    startActivity(i);
                    finish();
                }
            }
        });
    }

    private void addBottomDots(int position) {
        dots = new TextView[layouts.length];
        int[] colorActive = getResources().getIntArray(R.array.dot_active);
        int[] colorInactive = getResources().getIntArray(R.array.dot_inactive);
        dots_layout.removeAllViews();

        for(int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorInactive[position]);
            dots_layout.addView(dots[i]);
        }

        if(dots.length > 0)
            dots[position].setTextColor(colorActive[position]);
    }

    private int getItem(int i)
    {
        return view_pager.getCurrentItem() + i;
    }

    private void changeStatusBarColor()
    {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public class ViewPagerAdapter extends PagerAdapter
    {
        private LayoutInflater layoutInflater;

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutInflater.inflate(layouts[position],container,false);
            container.addView(v);
            return v;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View v =(View)object;
            container.removeView(v);
        }
    }

}
