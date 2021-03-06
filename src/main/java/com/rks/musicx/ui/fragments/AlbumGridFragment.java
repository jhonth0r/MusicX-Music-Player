package com.rks.musicx.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.util.Pair;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.afollestad.appthemeengine.Config;
import com.rks.musicx.R;
import com.rks.musicx.data.loaders.AlbumLoader;
import com.rks.musicx.data.loaders.SortOrder;
import com.rks.musicx.data.model.Album;
import com.rks.musicx.misc.utils.ATEUtils;
import com.rks.musicx.misc.utils.Extras;
import com.rks.musicx.misc.utils.GridSpacingItemDecoration;
import com.rks.musicx.misc.utils.Helper;
import com.rks.musicx.ui.adapters.AlbumListAdapter;
import com.rks.musicx.ui.adapters.BaseRecyclerViewAdapter;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by Coolalien on 6/28/2016.
 */

public class AlbumGridFragment extends miniFragment implements LoaderManager.LoaderCallbacks<List<Album>>, SearchView.OnQueryTextListener {


    private AlbumListAdapter albumListAdapter;
    private FastScrollRecyclerView rv;
    private int albumLoader = -1;
    private List<Album> albumList;
    private SearchView searchView;

    private BaseRecyclerViewAdapter.OnItemClickListener mOnClick = new BaseRecyclerViewAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position, View view) {
            switch (view.getId()) {
                case R.id.album_artwork:
                case R.id.item_view:
                    Fragment fragment = AlbumFragment.newInstance(albumListAdapter.getItem(position));
                    ImageView imageView = (ImageView) view.findViewById(R.id.album_artwork);
                    fragTransition(fragment, imageView);
                    rv.smoothScrollToPosition(position);
                    break;
            }
        }
    };

    public static AlbumGridFragment newInstance(int pos) {
        Extras.getInstance().setTabIndex(pos);
        return new AlbumGridFragment();
    }

    private void fragTransition(Fragment fragment, ImageView imageView) {
        ViewCompat.setTransitionName(imageView, "TransitionArtwork");
        Helper.setFragmentTransition(getActivity(), AlbumGridFragment.this, fragment, new Pair<View, String>(imageView, "TransitionArtwork"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.common_rv, container, false);
        rv = (FastScrollRecyclerView) rootView.findViewById(R.id.commonrv);
        albumListAdapter = new AlbumListAdapter(getActivity());
        albumListAdapter.setLayoutID(R.layout.item_grid_view);
        albumListAdapter.setOnItemClickListener(mOnClick);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(new AlbumListAdapter(getActivity()));
        rv.addItemDecoration(new GridSpacingItemDecoration(2, Extras.px2Dp(2, getContext()), true));
        String ateKey = Helper.getATEKey(getContext());
        int colorAccent = Config.accentColor(getContext(), ateKey);
        rv.setPopupBgColor(colorAccent);
        rv.setHasFixedSize(true);
        rv.setAdapter(albumListAdapter);
        initload();
        setHasOptionsMenu(true);
        albumList = new ArrayList<>();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        String ateKey = Helper.getATEKey(getContext());
        ATEUtils.setStatusBarColor(getActivity(), ateKey, Config.primaryColor(getActivity(), ateKey));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.album_view_menu, menu);
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.album_search));
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search album");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Extras extras = Extras.getInstance();
        switch (item.getItemId()) {
            case R.id.menu_sort_by_az:
                extras.setAlbumSortOrder(SortOrder.AlbumSortOrder.ALBUM_A_Z);
                load();
                break;
            case R.id.menu_sort_by_za:
                extras.setAlbumSortOrder(SortOrder.AlbumSortOrder.ALBUM_Z_A);
                load();
                break;
            case R.id.menu_sort_by_year:
                extras.setAlbumSortOrder(SortOrder.AlbumSortOrder.ALBUM_YEAR);
                load();
                break;
            case R.id.menu_sort_by_artist:
                extras.setAlbumSortOrder(SortOrder.AlbumSortOrder.ALBUM_ARTIST);
                load();
                break;
            case R.id.menu_sort_by_number_of_songs:
                extras.setAlbumSortOrder(SortOrder.AlbumSortOrder.ALBUM_NUMBER_OF_SONGS);
                load();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<Album>> onCreateLoader(int id, Bundle args) {

        AlbumLoader albumsLoader = new AlbumLoader(getContext());
        if (id == albumLoader) {
            albumsLoader.setSortOrder(Extras.getInstance().getAlbumSortOrder());
            albumsLoader.filterartistsong(null, null);
            return albumsLoader;
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<List<Album>> loader, List<Album> data) {
        if (data == null) {
            return;
        }
        albumList = data;
        albumListAdapter.addDataList(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Album>> loader) {
        albumListAdapter.notifyDataSetChanged();
    }

    /*
    load album
     */
    private void initload() {
        getLoaderManager().initLoader(albumLoader, null, this);
    }

    /*
    reload album
     */
    @Override
    public void load() {
        getLoaderManager().restartLoader(albumLoader, null, this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<Album> filterlist = Helper.filterAlbum(albumList, newText);
        albumListAdapter.setFilter(filterlist);
        return true;
    }

}
