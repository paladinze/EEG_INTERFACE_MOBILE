package com.paladinze.androideeg.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.paladinze.androideeg.R;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.InputFilter.LengthFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class BluetoothDeviceSelectionActivity extends Activity {
    private static final int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter mBluetoothAdapter;
    
    private ListView list_paired;
    private ListView list_available;
    
    private List<BluetoothDevice> devices_paired;
    private List<BluetoothDevice> devices_available;    

    private ArrayAdapter<String> list_paired_adapter;
    private ArrayAdapter<String> list_available_adapter;  
    
    private ProgressBar progress;
 
    
	/*********************************************************************************************	
	 * Discover & select bluetooth device
	**********************************************************************************************/
    @Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_control_panel);

		//View for show status of discovery
		progress = (ProgressBar) findViewById(R.id.progress);
		
		//View for paired devices
		list_paired = (ListView) findViewById(R.id.list_paired);
		list_paired.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				final BluetoothDevice device = ((BluetoothDevice) devices_paired.toArray()[arg2]);
				Intent intent = new Intent(BluetoothDeviceSelectionActivity.this, RoverControlActivity.class);
				intent.putExtra("device", device);
				startActivity(intent);
			}
		});
		list_paired_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		list_paired.setAdapter(list_paired_adapter);

		//View for discovered devices
		list_available = (ListView) findViewById(R.id.list_available);
		list_available.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				final BluetoothDevice device = ((BluetoothDevice) devices_available.toArray()[arg2]);
				Intent intent = new Intent(BluetoothDeviceSelectionActivity.this, RoverControlActivity.class);
				intent.putExtra("device", device);
				startActivity(intent);
			}
		});
		list_available_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		list_available.setAdapter(list_available_adapter);

		
		//Check bluetooth capability
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			Toast.makeText(this, "This is not a bluetooth device !", Toast.LENGTH_SHORT).show();
			return;
		}
		
		//Check bluetooth availability; if yes, start discovery
		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		} else {
			discoverDevices();
		}
    }

    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_ENABLE_BT) {
			if (resultCode == RESULT_OK) { 
				//enable bluetooth successful
				discoverDevices();
			} else if (resultCode == RESULT_CANCELED) {
				//cancel during enabling
				unregisterReceiver(mReceiver);
				finish();
			}
		}
    }

    private void discoverDevices() {
		// Register the BroadcastReceiver
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		registerReceiver(mReceiver, filter);
	
		//get paired devices
		devices_paired = new ArrayList<BluetoothDevice>(mBluetoothAdapter.getBondedDevices());
		for (BluetoothDevice device : devices_paired) {
		    list_paired_adapter.add(device.getName() + "\n" + device.getAddress());
		}
		((BaseAdapter) list_paired.getAdapter()).notifyDataSetChanged();
	
		//start discovery
		mBluetoothAdapter.startDiscovery();
    }

    //rescan button on action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem add = menu.add("Scan");
		add.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				mBluetoothAdapter.startDiscovery();
				return false;
			}
		});
		return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
		if (mReceiver != null) {
			unregisterReceiver(mReceiver);
		}
		finish();
		super.onDestroy();
    }

    //// Create a BroadcastReceiver for ACTION_FOUND, register it during discovery to receive device info
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){
				progress.setVisibility(View.VISIBLE);
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
				progress.setVisibility(View.INVISIBLE);
				//Toast.makeText(getApplicationContext(), getString(R.string.bt_discover_finish), Toast.LENGTH_SHORT).show();			
			} else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	
				//add info to discovered device list
				if (devices_available == null) {
					devices_available = new ArrayList<BluetoothDevice>();
				}
	
				if (!devices_available.contains(device)) {
					list_available_adapter.add(device.getName() + "\n" + device.getAddress());
					list_available_adapter.notifyDataSetChanged();
				}
	
				devices_available.add(device);

			} 

		}
    };

}