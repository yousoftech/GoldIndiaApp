package com.example.rahulr.goldindiaapp.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rahulr.goldindiaapp.Activity.Login;
import com.example.rahulr.goldindiaapp.R;

import static com.example.rahulr.goldindiaapp.Activity.Login.mysp;

public class TabActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    TabLayout tabLayout;
    Boolean cust=true;
    int t=0,p=0;
    int tabposition;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        //cust=false;
        preferences=getSharedPreferences(mysp,MODE_PRIVATE);
        String cu=preferences.getString("cust",null);
        if(cu==null)
        {
            cu="0";
        }
        if(cu.equals("1"))
        {
            cust=true;
        }
        else
        {
            cust=false;
        }
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout=(TabLayout)findViewById(R.id.tabs);
        createtab(tabLayout);
        /*tabLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tabLayout.getSelectedTabPosition()==0)
                {
                    tabLayout.getTabAt(0).select();
                }else if(tabLayout.getSelectedTabPosition()==1)
                {
                    tabLayout.getTabAt(1).select();
                }
                else
                {
                    tabLayout.getTabAt(0).select();
                }
            }
        });
*/
        //tabLayout.setScrollPosition(tabLayout.getSelectedTabPosition(),0f,true);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (cust) {
                    if (tabposition != 1) {
                        //int tabposition = tabLayout.getSelectedTabPosition();
                        if (tabLayout.getSelectedTabPosition() != position) {
                            tabLayout.getTabAt(position).select();
                            mViewPager.setCurrentItem(position);
                        }
                    }
                    if (tabLayout.getSelectedTabPosition() != mViewPager.getCurrentItem()) {
                        tabLayout.getTabAt(position).select();
                    }

                }
            }

            @Override
            public void onPageSelected(int position) {
                int i=0;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                int i=0;
            }
        });

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if(cust) {
                    tabposition = tabLayout.getSelectedTabPosition();
                    mViewPager.setCurrentItem(tabLayout.getSelectedTabPosition());
                    // tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).select();
                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                int i=0;
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int i=0;
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    private void createtab(TabLayout tabLayout) {

        tabLayout.addTab(tabLayout.newTab().setText("Card"));
        if(cust)
        {
            tabLayout.addTab(tabLayout.newTab().setText("Sell"));
        }
        else
        {
            //tabLayout.addTab(tabLayout.newTab().setText("Sell"));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tab, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            AlertDialog.Builder dialogBuilder=new AlertDialog.Builder(TabActivity.this);
            LayoutInflater inflater=TabActivity.this.getLayoutInflater();
            dialogBuilder.setMessage("Are you sure you want to logout");
            dialogBuilder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences.Editor editor = getSharedPreferences(mysp, MODE_PRIVATE).edit();
                    editor.putString("role", null);
                    editor.putString("uguid",null);
                    editor.apply();
                    Intent i=new Intent(TabActivity.this, Login.class);
                    startActivity(i);
                    finish();
                }
            });
            dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) { }
            });
            AlertDialog a=dialogBuilder.create();
            a.show();

        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_tab, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**sections
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the /tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            //return PlaceholderFragment.newInstance(position + 1);\
            if(position==0)
            {
                return new searchdata();
            }
            else if(position==1)
            {
                return new sellrecord();
            }
            else{
                return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }
    }
}
