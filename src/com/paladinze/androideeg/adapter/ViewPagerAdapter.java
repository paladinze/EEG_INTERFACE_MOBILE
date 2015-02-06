package com.paladinze.androideeg.adapter;

import com.paladinze.androideeg.fragments.PresentPlotFragment;
import com.paladinze.androideeg.fragments.PresentStatsFragment;
import com.paladinze.androideeg.fragments.PresentStimulusFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {
	final int mFragmentCount = 2;
	private String mFragmentTitles[] = new String[] { "Stimulus", "EEG Graph" };

	public ViewPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
			case 0:
				PresentStimulusFragment tab1 = new PresentStimulusFragment();
				return tab1;
	
			case 1:
				PresentStatsFragment tab2 = new PresentStatsFragment();
				return tab2;
		}
		return null;
	}

	@Override
	public int getCount() {
		return mFragmentCount;
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		return mFragmentTitles[position];
	}
	
	
}