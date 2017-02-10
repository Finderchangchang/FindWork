package com.handmark.pulltorefresh.extras.listfragment;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class PullToRefreshListFragment extends PullToRefreshBaseListFragment<PullToRefreshListView> {

	protected PullToRefreshListView onCreatePullToRefreshListView(LayoutInflater inflater, Bundle savedInstanceState) {
		return new PullToRefreshListView(getActivity());
	}

}