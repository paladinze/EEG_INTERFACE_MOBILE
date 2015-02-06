package com.paladinze.androideeg.fragments;

import com.paladinze.androideeg.R;
import com.paladinze.androideeg.activity.BluetoothDeviceSelectionActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class ModeInteractionFragment extends Fragment {
	
	Button button_car;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View view = inflater.inflate(R.layout.fragment_mode_interaction, container, false);
         
        button_car = (Button) view.findViewById(R.id.button1);
        button_car.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(getActivity(), BluetoothDeviceSelectionActivity.class);
				startActivity(intent);
			}
		});
        
        
        return view;
    }
}
