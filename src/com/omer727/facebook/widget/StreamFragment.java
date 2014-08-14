package com.omer727.facebook.widget;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Request.Callback;
import com.facebook.Request.GraphUserCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.omer727.facebook.widget.R;
import com.facebook.widget.ProfilePictureView;


public class StreamFragment extends Fragment {

	private ProfilePictureView profilePictureView;
	private TextView userNameView;
	private UiLifecycleHelper uiHelper;
	View view;
	private static final int REAUTH_ACTIVITY_CODE = 101;

	DateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
	SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		uiHelper = new UiLifecycleHelper(getActivity(), callback);
		uiHelper.onCreate(savedInstanceState);
		
		
	}


	@Override
	public View onCreateView(LayoutInflater inflater, 
			ViewGroup container, Bundle savedInstanceState) {
		
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.stream, container, false);
		this.view = view;
		// Check for an open session
		Session session = Session.getActiveSession();
		if (session != null && session.isOpened()) {
			// Get the user's data
			makeMeRequest(session,view);
		}
		// Find the user's profile picture custom view
		//profilePictureView = (ProfilePictureView) view.findViewById(R.id.selection_profile_pic);
		//profilePictureView.setCropped(true);

		// Find the user's name view
		//userNameView = (TextView) view.findViewById(R.id.selection_user_name);


		return view;
	}



	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(final Session session, final SessionState state, final Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	private void onSessionStateChange(final Session session, SessionState state, Exception exception) {
		if (session != null && session.isOpened()) {
//			// Get the user's data.
			makeMeRequest(session,view);
		}
	}



	private void makeMeRequest(final Session session, final View view) {
		// Make an API call to get user data and define a 
		// new callback to handle the response.
		Request request = MyRequest(session, new Request.GraphUserCallback() {
			@Override
			public void onCompleted(GraphUser user, Response response) {
				// If the response is successful
				if (session == Session.getActiveSession()) {
					if (user != null) {
						// Set the id for the ProfilePictureView
						// view that in turn displays the profile picture.
						//profilePictureView.setProfileId(user.getId());
						// Set the Textview's text to the user's name.
						try {
							String x= "";//((JSONObject)user.getProperty("home")).getJSONArray("data").getJSONObject(0).getString("message");
							JSONArray statuses = ((JSONObject)user.getProperty("home")).getJSONArray("data");

							List<String> list = new ArrayList<String>();
							
							for (int i=0; i < statuses.length() ; i++){

								JSONObject status = statuses.getJSONObject(i);
								JSONObject from = null;
								try{
								from = status.getJSONObject("from");
								}catch(Exception e){
									e.printStackTrace();
								}
								Date creationDate = inputDateFormat.parse(status.getString("created_time"));
								
								String creationDateStr = outputDateFormat.format(creationDate);
								list.add(from.optString("name") + "\n" + status.optString("message","")+"\n" + creationDateStr);
							}
							
							StableArrayAdapter adapter = new StableArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, list);
						    ListView listview = (ListView) view.findViewById(R.id.listview);
						    listview.setAdapter(adapter);

							    
							//userNameView.setText(+ x);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}
				if (response.getError() != null) {
					// Handle errors, will do so later.
				}
			}
		});
		request.executeAsync();
	}

	
	
	
	
	public static Request MyRequest(Session session, final GraphUserCallback callback) {
		Callback wrapper = new Callback() {
			@Override
			public void onCompleted(Response response) {
				if (callback != null) {
					callback.onCompleted(response.getGraphObjectAs(GraphUser.class), response);
				}
			}
		};
		Bundle bundle = new Bundle();
		bundle.putString("fields", "home.fields(message,from,picture,comments.fields(message,like_count),likes.fields(name))");
		return new Request(session, "me", bundle, null, wrapper);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REAUTH_ACTIVITY_CODE) {
			uiHelper.onActivityResult(requestCode, resultCode, data);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		uiHelper.onResume();
	}

	@Override
	public void onSaveInstanceState(Bundle bundle) {
		super.onSaveInstanceState(bundle);
		uiHelper.onSaveInstanceState(bundle);
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}
	
	private class StableArrayAdapter extends ArrayAdapter<String> {

	    HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

	    public StableArrayAdapter(Context context, int textViewResourceId,
	        List<String> objects) {
	      super(context, textViewResourceId, objects);
	      for (int i = 0; i < objects.size(); ++i) {
	        mIdMap.put(objects.get(i), i);
	      }
	    }

	    @Override
	    public long getItemId(int position) {
	      String item = getItem(position);
	      return mIdMap.get(item);
	    }

	    @Override
	    public boolean hasStableIds() {
	      return true;
	    }

	  }

	
}



