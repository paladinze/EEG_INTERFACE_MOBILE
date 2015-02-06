package com.paladinze.androideeg.fragments;

import java.util.ArrayList;

import com.paladinze.androideeg.R;
import com.paladinze.androideeg.model.DataModel;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import com.neurosky.thinkgear.*;

public class PresentPlotFragment extends Fragment {

	
	TextView mTextState;
	TextView mTextSignalStrength;
	TextView mTextAttention;
	TextView mTextMeditation;
	TextView mTextBlink;
	TextView mTextRawData;
	TextView mTextEegPowerAlpha;
	TextView mTextEegPowerBeta;
	TextView mTextEegPowerTheta;
	
	DataModel TgDataModel;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TgDataModel =  (DataModel) getActivity().getApplication();
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_present_plot, container, false);
		
		mTextState = (TextView) view.findViewById(R.id.status);
		mTextSignalStrength = (TextView) view.findViewById(R.id.signal_strength);
		mTextAttention = (TextView) view.findViewById(R.id.attention);
		mTextMeditation = (TextView) view.findViewById(R.id.meditation);
		mTextBlink = (TextView) view.findViewById(R.id.blink);
		mTextRawData = (TextView) view.findViewById(R.id.raw_data);
		mTextEegPowerAlpha = (TextView) view.findViewById(R.id.eeg_power_alpha);
		mTextEegPowerBeta = (TextView) view.findViewById(R.id.eeg_power_beta);
		mTextEegPowerTheta = (TextView) view.findViewById(R.id.eeg_power_theta);
		
		
		Thread textThread = new Thread() {
			@Override
			public void run() {
				try{
					while (!isInterrupted()) {
						Thread.sleep(1);
						//Thread.sleep(1);
						if (getActivity() == null)
							return;
						
						getActivity().runOnUiThread(new Runnable() {
							@Override
							public void run() {
					      		mTextState.setText(TgDataModel.getTgState());
					    		mTextSignalStrength.setText("Sigal Strength:" + TgDataModel.getTgSignalStrength());
					    		mTextAttention.setText("Attention:" + TgDataModel.getTgAttention());
					    		mTextMeditation.setText("Meditation:" + TgDataModel.getTgMeditation());
					    		mTextRawData.setText("Raw data:" + TgDataModel.getTgRawData());
					    		mTextBlink.setText("Blink:" + TgDataModel.getTgBlink());
					    		mTextEegPowerAlpha.setText("Alpha:" + TgDataModel.getTgEegPowerAlpha1());
					    		mTextEegPowerBeta.setText("Beta:" + TgDataModel.getTgEegPowerBeta1());
					    		mTextEegPowerTheta.setText("Theta:" + TgDataModel.getTgEegPowerTheta());
							}
						});
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		textThread.start();
		return view;
	}
	
}


