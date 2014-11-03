package com.example.andronote;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class InAppActivity extends Activity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v13.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_in_app);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		
		
		
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
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.in_app, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class
			// below).
			return PlaceholderFragment.newInstance(position + 1);
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}
	public void addNewNoteAction(View view)
	{
		Intent myIntent = new Intent(this, AddNoteActivity.class);
		startActivity(myIntent);
	}
	public void addNewRightAction(View view)
	{
		Intent myIntent = new Intent(this, AddNewRightActivity.class);
		startActivity(myIntent);
	}
	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment(sectionNumber);
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}
		private int screen;
		public PlaceholderFragment(int scr) {
			screen = scr;
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = null;
			int res = 0;
			switch(screen){
			case 1:
				res = R.layout.fragment_note_list_view;
			    rootView = inflater.inflate(res, container, false);
				ListView mainListView = (ListView) rootView.findViewById( MainActivity.list1 );  
				  
			    // Create and populate a List of planet names.  
			    String[] planets = new String[] {};    
			    ArrayList<String> planetList = new ArrayList<String>();  
			    planetList.addAll( Arrays.asList(planets) );  
			      
			    // Create ArrayAdapter using the planet list.  
			    ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(MainActivity.context, R.layout.simplerow, planetList);  
			      
			    // Add more planets. If you passed a String[] instead of a List<String>   
			    // into the ArrayAdapter constructor, you must not add more items.   
			    // Otherwise an exception will occur.  
			    for(int i = 0 ;i<20;++i){
			    	listAdapter.add("Note "+(i+1));
			    	
			    }
			    
			    // Set the ArrayAdapter as the ListView's adapter.  
			    mainListView.setAdapter( listAdapter );  
				
				break;
			case 2:
				res = R.layout.fragment_note_view;
			rootView = inflater.inflate(res,
					container, false);
				break;
			case 3:
				res = R.layout.fragment_rights_view;
			    rootView = inflater.inflate(res, container, false);
				ListView mainListView2 = (ListView) rootView.findViewById( R.id.rightsView );  
				  
			    // Create and populate a List of planet names.  
			    String[] planets2 = new String[] {};    
			    ArrayList<String> planetList2 = new ArrayList<String>();  
			    planetList2.addAll( Arrays.asList(planets2) );  
			      
			    // Create ArrayAdapter using the planet list.  
			    ArrayAdapter<String> listAdapter2 = new ArrayAdapter<String>(MainActivity.context, R.layout.simplerow, planetList2);  
			      
			    // Add more planets. If you passed a String[] instead of a List<String>   
			    // into the ArrayAdapter constructor, you must not add more items.   
			    // Otherwise an exception will occur.  
			    for(int i = 0 ;i<20;++i){
			    	listAdapter2.add("Right "+(i+1));
			    	
			    }
			    
			    // Set the ArrayAdapter as the ListView's adapter.  
			    mainListView2.setAdapter( listAdapter2 ); 
				break;
			}
			return rootView;
		}
	}

}
