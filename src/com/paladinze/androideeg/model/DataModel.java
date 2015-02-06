package com.paladinze.androideeg.model;

import java.util.ArrayList;

import com.neurosky.thinkgear.TGDevice;
import com.neurosky.thinkgear.TGEegPower;
import com.paladinze.androideeg.R;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class DataModel extends Application {
	String mTgState;
	Boolean mConnected;
	int mTgSignalStrength;
	int mTgAttention;
	int mTgMeditation;
	int mTgBlink;
	int mTgRawData;
	
	int mTgEegPowerDelta; //1-3hz
	int mTgEegPowerTheta; //4-7hz
	int mTgEegPowerAlpha1; //8-9hz
	int mTgEegPowerAlpha2; //10-12hz
	int mTgEegPowerBeta1; //13-17hz
	int mTgEegPowerBeta2; //18-30hz
	int mTgEegPowerGamma1; //31-40hz
	int mTgEegPowerGamma2; //41-50hz
	TGEegPower mTgEegPower;
	
	//EEG device elements
	BluetoothAdapter mBtAdapter;	
	TGDevice mTgDevice;
	
	//constructor
	public DataModel() {
		mConnected = false;
		mBtAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBtAdapter != null) {
			mTgDevice = new TGDevice(mBtAdapter, mHandler);
		} else {
        	Toast.makeText(this, "Bluetooth not available", Toast.LENGTH_LONG).show();
        	return;
		}
	}

	//Listener Interface
	public interface Listener {
		public void onConnectionChange(boolean connected);
	}
	
	private Listener mListener = null;
	public void registerListener (Listener listener) {
		mListener = listener;
	}
	
	public void changeState() {
		if (mListener != null)
			mListener.onConnectionChange(mConnected);
	}
	
	
	
	
	
	//setter and getter methods

	public Boolean getConnected() {
		return mConnected;
	}
		
	
	public int getTgEegPowerDelta() {
		return mTgEegPowerDelta;
	}
	public void setTgEegPowerDelta(int mTgEegPowerDelta) {
		this.mTgEegPowerDelta = mTgEegPowerDelta;
	}
	public int getTgEegPowerTheta() {
		return mTgEegPowerTheta;
	}
	public void setTgEegPowerTheta(int mTgEegPowerTheta) {
		this.mTgEegPowerTheta = mTgEegPowerTheta;
	}
	public int getTgEegPowerAlpha1() {
		return mTgEegPowerAlpha1;
	}
	public void setTgEegPowerAlpha1(int mTgEegPowerAlpha1) {
		this.mTgEegPowerAlpha1 = mTgEegPowerAlpha1;
	}
	public int getTgEegPowerAlpha2() {
		return mTgEegPowerAlpha2;
	}
	public void setTgEegPowerAlpha2(int mTgEegPowerAlpha2) {
		this.mTgEegPowerAlpha2 = mTgEegPowerAlpha2;
	}
	public int getTgEegPowerBeta1() {
		return mTgEegPowerBeta1;
	}
	public void setTgEegPowerBeta1(int mTgEegPowerBeta1) {
		this.mTgEegPowerBeta1 = mTgEegPowerBeta1;
	}
	public int getTgEegPowerBeta2() {
		return mTgEegPowerBeta2;
	}
	public void setTgEegPowerBeta2(int mTgEegPowerBeta2) {
		this.mTgEegPowerBeta2 = mTgEegPowerBeta2;
	}
	public int getTgEegPowerGamma1() {
		return mTgEegPowerGamma1;
	}
	public void setTgEegPowerGamma1(int mTgEegPowerGamma1) {
		this.mTgEegPowerGamma1 = mTgEegPowerGamma1;
	}
	public int getTgEegPowerGamma2() {
		return mTgEegPowerGamma2;
	}
	public void setTgEegPowerGamma2(int mTgEegPowerGamma2) {
		this.mTgEegPowerGamma2 = mTgEegPowerGamma2;
	}
	public TGEegPower getTgEegPower() {
		return mTgEegPower;
	}
	public void setTgEegPower(TGEegPower mTgEegPower) {
		this.mTgEegPower = mTgEegPower;
	}


	public String getTgState() {
		return mTgState;
	}
	public void setTgState(String mTgState) {
		this.mTgState = mTgState;
	}
	public int getTgSignalStrength() {
		return mTgSignalStrength;
	}
	public void setTgSignalStrength(int mTgSignalStrength) {
		this.mTgSignalStrength = mTgSignalStrength;
	}
	public int getTgAttention() {
		return mTgAttention;
	}
	public void setTgAttention(int mTgAttention) {
		this.mTgAttention = mTgAttention;
	}
	public int getTgMeditation() {
		return mTgMeditation;
	}
	public void setTgMeditation(int mTgMeditation) {
		this.mTgMeditation = mTgMeditation;
	}
	public int getTgBlink() {
		return mTgBlink;
	}
	public void setTgBlink(int mTgBlink) {
		this.mTgBlink = mTgBlink;
	}
	public int getTgRawData() {
		return mTgRawData;
	}
	public void setTgRawData(int mTgRawData) {
		this.mTgRawData = mTgRawData;
	}

	/*************************************************
	 * EEG Data Acquisition
	 *************************************************/
		
		public void connectDevice() {
			if(mTgDevice.getState() != TGDevice.STATE_CONNECTING && mTgDevice.getState() != TGDevice.STATE_CONNECTED)
				mTgDevice.connect(true);   
		}	
	     
	    public void closeDevice() {
	    	mTgDevice.close();
	    }
		
		
		final Handler mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case TGDevice.MSG_STATE_CHANGE:
					switch (msg.arg1) {
					case TGDevice.STATE_IDLE:
						mConnected = false;
						changeState();
						mTgState = getString(R.string.state_idle);
						break;
					case TGDevice.STATE_CONNECTING:
						mConnected = false;
						changeState();
						mTgState = getString(R.string.state_connecting);
						break;		                    
					case TGDevice.STATE_CONNECTED:
						mConnected = true;
						changeState();
						mTgState = getString(R.string.state_connected);
						mTgDevice.start();
						break;
					case TGDevice.STATE_DISCONNECTED:
						mConnected = false;
						changeState();
						mTgState = getString(R.string.state_disconnected);
						break;
					case TGDevice.STATE_NOT_FOUND:
						mConnected = false;
						changeState();
						mTgState = getString(R.string.state_not_found);
						break;
					case TGDevice.STATE_NOT_PAIRED:
						mConnected = false;
						changeState();
						mTgState = getString(R.string.state_not_paried);
					}
					break;
				case TGDevice.MSG_POOR_SIGNAL:
					mTgSignalStrength = msg.arg1;
					break;
				case TGDevice.MSG_ATTENTION:
					mTgAttention = msg.arg1;
					break;
				case TGDevice.MSG_MEDITATION:
					mTgMeditation = msg.arg1;
					break;
				case TGDevice.MSG_BLINK:
					mTgBlink = msg.arg1;
					break;
				case TGDevice.MSG_RAW_DATA:
					mTgRawData = msg.arg1;
					break;
				case TGDevice.MSG_EEG_POWER:
					TGEegPower temp = (TGEegPower) msg.obj;
					mTgEegPowerDelta = temp.delta;
					mTgEegPowerTheta = temp.theta;
					mTgEegPowerAlpha1 = temp.lowAlpha;
					mTgEegPowerAlpha2 = temp.highAlpha;
					mTgEegPowerBeta1 = temp.lowBeta;
					mTgEegPowerBeta2 = temp.highBeta;
					mTgEegPowerGamma1 = temp.lowGamma;
					mTgEegPowerGamma2 = temp.midGamma;
					break;
				case TGDevice.MSG_RAW_MULTI: //not available in Mindwave mobile
					break;
				case TGDevice.MSG_HEART_RATE: //not available in Mindwave mobile
					break;
				case TGDevice.MSG_LOW_BATTERY:
					Toast.makeText(getApplicationContext(), "Replace headset battery!", Toast.LENGTH_SHORT).show();
					break;
				default:
					mTgBlink = 0;
					break;
				}
		
			}
		};		
	
	
}