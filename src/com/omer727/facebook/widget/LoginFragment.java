package com.omer727.facebook.widget;

import java.util.Arrays;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.omer727.facebook.widget.R;
import com.facebook.widget.LoginButton;


public class LoginFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.login, container, false);
	    LoginButton authButton = (LoginButton)view.findViewById(R.id.login_button);

	    authButton.setReadPermissions(Arrays.asList("read_stream")); 
	    return view;
	}
	
}
