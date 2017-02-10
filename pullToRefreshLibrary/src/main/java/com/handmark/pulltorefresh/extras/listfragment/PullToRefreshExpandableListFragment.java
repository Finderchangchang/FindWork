package com.handmark.pulltorefresh.extras.listfragment;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;

public class PullToRefreshExpandableListFragment extends PullToRefreshBaseListFragment<PullToRefreshExpandableListView> {

	protected PullToRefreshExpandableListView onCreatePullToRefreshListView(LayoutInflater inflater,
			Bundle savedInstanceState) {
		return new PullToRefreshExpandableListView(getActivity());
	}

}