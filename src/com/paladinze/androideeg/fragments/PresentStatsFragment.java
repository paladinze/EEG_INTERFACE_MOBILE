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

public class PresentStatsFragment extends Fragment {

	
	TextView mTextState;
	TextView mTextSignalStrength;
	TextView mTextAttention;
	TextView mTextMeditation;
	TextView mTextBlink;
	TextView mTextRawData;
	TextView mTextEegPowerDelta;
	TextView mTextEegPowerTheta;
	TextView mTextEegPowerAlpha1;
	TextView mTextEegPowerAlpha2;
	TextView mTextEegPowerBeta1;
	TextView mTextEegPowerBeta2;
	TextView mTextEegPowerGamma1;
	TextView mTextEegPowerGamma2;
	
	DataModel TgDataModel;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TgDataModel =  (DataModel) getActivity().getApplication();
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_present_stats, container, false);
		
		mTextState = (TextView) view.findViewById(R.id.status);
		mTextSignalStrength = (TextView) view.findViewById(R.id.signal_strength);
		mTextAttention = (TextView) view.findViewById(R.id.attention);
		mTextMeditation = (TextView) view.findViewById(R.id.meditation);
		mTextBlink = (TextView) view.findViewById(R.id.blink);
		mTextRawData = (TextView) view.findViewById(R.id.raw_data);
		
		mTextEegPowerDelta = (TextView) view.findViewById(R.id.eeg_power_delta);
		mTextEegPowerTheta = (TextView) view.findViewById(R.id.eeg_power_theta);
		mTextEegPowerAlpha1 = (TextView) view.findViewById(R.id.eeg_power_alpha1);
		mTextEegPowerAlpha2 = (TextView) view.findViewById(R.id.eeg_power_alpha2);
		mTextEegPowerBeta1 = (TextView) view.findViewById(R.id.eeg_power_beta1);
		mTextEegPowerBeta2 = (TextView) view.findViewById(R.id.eeg_power_beta2);		
		mTextEegPowerGamma1 = (TextView) view.findViewById(R.id.eeg_power_gamma1);
		mTextEegPowerGamma2 = (TextView) view.findViewById(R.id.eeg_power_gamma2);
		
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
					    		mTextEegPowerDelta.setText("Delta (1-3hz):" + TgDataModel.getTgEegPowerDelta());
					    		mTextEegPowerTheta.setText("Theta (4-7hz):" + TgDataModel.getTgEegPowerTheta());
					    		mTextEegPowerAlpha1.setText("Alpha (8-9hz):" + TgDataModel.getTgEegPowerAlpha1());
					    		mTextEegPowerAlpha2.setText("Alpha (10-12hz):" + TgDataModel.getTgEegPowerAlpha2());
					    		mTextEegPowerBeta1.setText("Beta (13-17hz):" + TgDataModel.getTgEegPowerBeta1());
					    		mTextEegPowerBeta2.setText("Beta (18-30hz):" + TgDataModel.getTgEegPowerBeta2());
					    		mTextEegPowerGamma1.setText("Gamma (31-40hz):" + TgDataModel.getTgEegPowerGamma1());
					    		mTextEegPowerGamma2.setText("Gamma (41-50hz):" + TgDataModel.getTgEegPowerGamma2());
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


