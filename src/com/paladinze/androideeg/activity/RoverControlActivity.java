package com.paladinze.androideeg.activity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import com.paladinze.androideeg.R;
import com.paladinze.androideeg.R.layout;
import com.paladinze.androideeg.model.DataModel;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;

public class RoverControlActivity extends FragmentActivity {

	//Debug
	private static final String TAG = "TestActivity";
	
	//UUID for serial port profile
    private static final UUID SERIAL_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); 

    //Member Varaiables
    private BluetoothDevice mDevice;

	private BluetoothAdapter mBtAdapter;
	private Handler mHandler;
	private DataModel TgDataModel;
	
	private ConnectThread mConnectThread;
	private ConnectedThread mConnectedThread;
	private String input_num = "0";
	private int mState;
    // Constants that indicate the current connection state
    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 3;  // now connected to a remote device	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rover_control_activity);

		mDevice = (BluetoothDevice) getIntent().getParcelableExtra("device");
		mBtAdapter = BluetoothAdapter.getDefaultAdapter();		
		mHandler = (Handler) new Handler();
		TgDataModel = (DataModel) getApplication();		
		
		ListenButtonViews();			
	
		mConnectThread = new ConnectThread(mDevice);
		mConnectThread.start();

	}
	
 
    private void ListenButtonViews() {
        findViewById(R.id.button_forward).setOnTouchListener(mMove_OnTouchListener);
        findViewById(R.id.button_left).setOnTouchListener(mMove_OnTouchListener);
        findViewById(R.id.button_right).setOnTouchListener(mMove_OnTouchListener);
        findViewById(R.id.button_backward).setOnTouchListener(mMove_OnTouchListener);
    }
	//manual control
    OnTouchListener mMove_OnTouchListener = new View.OnTouchListener() {
    	String input_num = "0";
 
    	private Handler mHandler;
	    Runnable mAction = new Runnable() {
	        @Override public void run() {
	        	mConnectedThread.write((input_num ).getBytes());
	            mHandler.postDelayed(this, 50);
	        }
	    };
    	    	
		public boolean onTouch(View ButtonView, MotionEvent event) {			
			          
			if (ButtonView.getId() == R.id.button_forward) {           
				input_num = "1";					
			} else if (ButtonView.getId() == R.id.button_left) {
				input_num = "3";
			} else if (ButtonView.getId() == R.id.button_right) {
				input_num = "4";
			} else if (ButtonView.getId() == R.id.button_backward) {
				input_num = "2";
			} else if (ButtonView.getId() == R.id.button_center) {
				input_num = "0";
			}
	        switch(event.getAction()) {
		        case MotionEvent.ACTION_DOWN:
		            if (mHandler != null) return true;
		            mHandler = new Handler();
		            mHandler.postDelayed(mAction, 25);
		            break;
		        case MotionEvent.ACTION_UP:
		        	mConnectedThread.write(("0").getBytes());
		            if (mHandler == null) return true;
		            mHandler.removeCallbacks(mAction);
		            mHandler = null;
		            break;
	        }
	        
	        return false;
		}

    };
    
	
	//Connect to HC-06 Bluetooth Module (slave mode) as a client
	private class ConnectThread extends Thread {

	    private final BluetoothDevice mmDevice;
	    private final BluetoothSocket mmSocket;	 
	    public ConnectThread(BluetoothDevice device) {
	    	Log.d(TAG, "create ConnectTread");
	        BluetoothSocket tmp = null;
	        mmDevice = device;
	 
	        try {
	            tmp = device.createRfcommSocketToServiceRecord(SERIAL_UUID);
	        } catch (IOException e) { 
	        	Log.e(TAG, "createRfcommSocket failed", e);
	        }
	        mmSocket = tmp;
	    }
	 
	    public void run() {
	    	Log.i(TAG, "Begin ConnectTread");
	    	
	        // Cancel discovery because it will slow down the connection
	    	if (mBtAdapter.isDiscovering()) {
		        mBtAdapter.cancelDiscovery();	    		
	    	}

	    	// Connect the device through the socket (this is blocking! must not be in main thread)
	        try {
	            mmSocket.connect();
	        } catch (IOException connectException) {
	        	Log.e(TAG, "Begin Connect, but failed");
	            try {
	                mmSocket.close();
	            } catch (IOException closeException) {
	            	Log.e(TAG, "Begin Connect, but failed, cannot close either");
	            }
	            return;
	        }
	 
	        // Do work to manage the connection (in a separate thread)
	        mConnectedThread = new ConnectedThread(mmSocket);
	        mConnectedThread.start();
	        

	        
	        
	    }
	 
	    /** Will cancel an in-progress connection, and close the socket */
	    public void cancel() {
	        try {
	            mmSocket.close();
	        } catch (IOException e) {
	        	Log.e(TAG, "close() failed in connect socket");
	        }
	    }
	}
	
	
	private class ConnectedThread extends Thread {
		private final BluetoothSocket mmSocket;
	    private final InputStream mmInStream;
	    private final OutputStream mmOutStream;
	 
	    public ConnectedThread(BluetoothSocket socket) {
	    	Log.d(TAG, "create ConnectedTread");
	    	mmSocket = socket;
	        InputStream tmpIn = null;
	        OutputStream tmpOut = null;
	        try {
	            tmpIn = socket.getInputStream();
	            tmpOut = socket.getOutputStream();
	        } catch (IOException e) {
	        	Log.e(TAG, "temp sockets not created");
	        }
	        mmInStream = tmpIn;
	        mmOutStream = tmpOut;
	    }
	 
	    public void run() {
	    	Log.i(TAG, "BEGIN mConnectedThread");
	    	byte[] buffer = new byte[1024];
	    	int bytes;
	    	
	    	Log.i(TAG, "Entering Moving State");	    	
	    	//startMoving();
	    	Log.i(TAG, "Leaving Moving State");
	    	
	        while (true) {

	        	updateInput();
				write(input_num.getBytes());
				Log.i(TAG, "Entering while loop");
	        
	        }
	    }
	 
	    /* Call this from the main activity to send data to the remote device */
	    public void write(byte[] bytes) {
	        try {
	            mmOutStream.write(bytes);
	        } catch (IOException e) { 
	        	Log.e(TAG, "Failed to Write");
	        }
	    }
	 
	    public void cancel() {
	        try {
	        	stopMoving();
	            mmSocket.close();
	        } catch (IOException e) { 
	        	Log.e(TAG, "close() in ConnectedThread failed");
	        }
	    }
	    
	    
		//Move the car
	    Runnable mMove = new Runnable() {
	        @Override 
	        public void run() {
		    	Log.i(TAG, "Entering Moving State");	
	        	updateInput();
	        	write(input_num.getBytes());
	            mHandler.postDelayed(mMove , 50);
	        }
	    };	
	    
	    
	    public void startMoving() {
	    	mMove.run();
	    }
	    
	    public void stopMoving() {
	    	mHandler.removeCallbacks(mMove);
	    }
	    
	    public void updateInput() {
		    int attention = TgDataModel.getTgAttention();
			if (attention > 70) {
				input_num = "4";
				Log.v("movement", "forward");
			} else if (attention <30){
				input_num = "3";
				Log.v("movement", "backward");
			} else {
				input_num = "0";
				Log.v("movement", "don't move");
			}
		}
	        
	}
	
   
	
	
	@Override
	protected void onDestroy() {
		
		
		//mConnectedThread.cancel();
		mConnectThread.cancel();
		
		finish();
		super.onDestroy();
	}
	
	
}
