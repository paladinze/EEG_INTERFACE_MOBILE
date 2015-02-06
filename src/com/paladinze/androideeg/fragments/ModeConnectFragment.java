package com.paladinze.androideeg.fragments;

import com.paladinze.androideeg.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class ModeConnectFragment extends Fragment {
	
	public ModeConnectFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_mode_connect, container, false);
         
        return rootView;
    }
}
