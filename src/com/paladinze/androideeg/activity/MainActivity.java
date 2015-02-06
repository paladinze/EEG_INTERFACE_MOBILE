package com.paladinze.androideeg.activity;


import java.util.ArrayList;

import android.bluetooth.BluetoothAdapter;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.neurosky.thinkgear.TGDevice;
import com.neurosky.thinkgear.TGEegPower;
import com.paladinze.androideeg.R;
import com.paladinze.androideeg.adapter.SlideMenuListAdapter;
import com.paladinze.androideeg.fragments.ModeAboutFragment;
import com.paladinze.androideeg.fragments.ModeConnectFragment;
import com.paladinze.androideeg.fragments.ModeInteractionFragment;
import com.paladinze.androideeg.fragments.ModeStandardFragment;
import com.paladinze.androideeg.fragments.ModeStimulusFragment;
import com.paladinze.androideeg.fragments.ModeUtilitiesFragment;
import com.paladinze.androideeg.model.DataModel;
import com.paladinze.androideeg.model.DataModel.Listener;
import com.paladinze.androideeg.model.SlideMenuItem;


public class MainActivity extends FragmentActivity implements Listener {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mAppTitle;

	//slide menu elements
	private CharSequence mMenuTitle;
	private String[] slideMenuTitles;
	private TypedArray slideMenuIcons;
	private ArrayList<SlideMenuItem> mSlideMenuItems;
	private SlideMenuListAdapter adapter;

	//EEG device elements
	private DataModel mTgModel;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mTgModel = (DataModel) getApplication();
		mTgModel.registerListener(this);
		setContentView(R.layout.activity_main);
		
		
		//Establish Connection		
		mTgModel.connectDevice();
		
		//get app title
		mAppTitle = getTitle();
		mMenuTitle = getTitle();

		//get menu items names &icons
		slideMenuTitles = getResources().getStringArray(R.array.slide_menu_names);
		slideMenuIcons = getResources().obtainTypedArray(R.array.slide_menu_icons);

		//get menu widgets
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		//populate slider menu
		mSlideMenuItems = new ArrayList<SlideMenuItem>();
		mSlideMenuItems.add(new SlideMenuItem(slideMenuTitles[0], slideMenuIcons.getResourceId(0, 0)));
		mSlideMenuItems.add(new SlideMenuItem(slideMenuTitles[1], slideMenuIcons.getResourceId(1, 0)));
		mSlideMenuItems.add(new SlideMenuItem(slideMenuTitles[2], slideMenuIcons.getResourceId(2, 0)));
		mSlideMenuItems.add(new SlideMenuItem(slideMenuTitles[3], slideMenuIcons.getResourceId(3, 0)));
		mSlideMenuItems.add(new SlideMenuItem(slideMenuTitles[4], slideMenuIcons.getResourceId(4, 0)));
		mSlideMenuItems.add(new SlideMenuItem(slideMenuTitles[5], slideMenuIcons.getResourceId(5, 0)));
		slideMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		//set adapter
		adapter = new SlideMenuListAdapter(getApplicationContext(), mSlideMenuItems);
		mDrawerList.setAdapter(adapter);

		//enable toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		//implement drawer listener
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, 
				R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close ) {
			public void onDrawerClosed(View drawerView) {
				getActionBar().setTitle(mMenuTitle);
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mAppTitle);
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		//display default fragment
		if (savedInstanceState == null) {
			displayFragment(1);
		}
	}
	
	

	//disconnect device
	@Override
	public void onDestroy() {
		mTgModel.closeDevice();
		finish();
		super.onDestroy();

	}
	
	public void onBackPressed() {  
	    finish();
	    System.exit(0);
	}

	//implements slide menu click listener
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			displayFragment(position);
		}
	}

	//inflate and handle top right action bar menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		switch (item.getItemId()) {
			case R.id.action_connect:
				mTgModel.connectDevice();
				invalidateOptionsMenu();
				return true;
			case R.id.action_exit:
				finish();
				System.exit(0);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	
	//hide action bar items when menu is open
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (mTgModel.getConnected()) {
			menu.findItem(R.id.action_connect).setIcon(R.drawable.ic_connect);
		} else {
			menu.findItem(R.id.action_connect).setIcon(R.drawable.ic_connect_grey);
		}
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		//menu.findItem(R.id.action_exit).setVisible(!drawerOpen);
		//menu.findItem(R.id.action_connect).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	//display chosen fragment
	private void displayFragment(int position) {
		Fragment fragment = null;
		switch (position) {
			case 0:
				fragment = new ModeConnectFragment();
				break;
			case 1:
				fragment = new ModeStandardFragment();
				break;
			case 2:
				fragment = new ModeStimulusFragment();
				break;
			case 3:
				fragment = new ModeInteractionFragment();
				break;
			case 4:
				fragment = new ModeUtilitiesFragment();
				break;
			case 5:
				fragment = new ModeAboutFragment();
				break;
			default:
				break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.fragment_container, fragment).commit();

			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			mMenuTitle = slideMenuTitles[position];
			getActionBar().setTitle(mMenuTitle);
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	//only needed for ActionBarDrawerToggle
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}


	@Override
	public void onConnectionChange(boolean mConnected) {
		invalidateOptionsMenu();
	}
	
}

