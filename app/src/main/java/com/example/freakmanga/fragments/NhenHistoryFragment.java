package com.example.freakmanga.fragments;

import static com.example.freakmanga.MyApp.localAppDB;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.freakmanga.R;
import com.example.freakmanga.adapters.MainMangaAdapter;
import com.example.freakmanga.data.room.nhen_local.history.NhenHistoryTable;
import com.example.freakmanga.databinding.FragmentNhenLocalBinding;
import com.google.gson.Gson;

import java.util.List;

public class NhenHistoryFragment extends Fragment implements SearchView.OnQueryTextListener {
    private FragmentNhenLocalBinding mBinding;
    private Context mContext;

    public NhenHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentNhenLocalBinding.inflate(inflater, container, false);
        getDataFromLocalDB("ordinary", "");
        initUI();
        initEvent();
        return mBinding.getRoot();
    }

    private void initUI() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    private void initEvent() {
        mBinding.swipeRefreshAnimeBookmark.setOnRefreshListener(() -> {
            mBinding.swipeRefreshAnimeBookmark.setRefreshing(false);
            getDataFromLocalDB("ordinary", "");
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getDataFromLocalDB("ordinary", "");
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.searchBar);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void getDataFromLocalDB(String hitStatus, String newText) {
        List<NhenHistoryTable> historyModelList;
        if (hitStatus.equalsIgnoreCase("ordinary")) {
            historyModelList = localAppDB.nhenHistoryDAO().getMangaHistoryData();
            Log.e("read history", new Gson().toJson(historyModelList));
            if (validateList(historyModelList)) {
                showRecyclerResult(historyModelList);
            } else {
                showErrorLayout("Oops, you haven't marked your favourite manga");
            }
        } else {
            historyModelList = localAppDB.nhenHistoryDAO().searchByName("%" + newText + "%");
            if (validateList(historyModelList)) {
                showRecyclerResult(historyModelList);
            } else {
                showErrorLayout("Oops, please type correctly");
            }
        }
    }

    private boolean validateList(List<NhenHistoryTable> historyModelList) {
        return historyModelList != null && historyModelList.size() > 0;
    }

    private void showRecyclerResult(List<NhenHistoryTable> historyModelList) {
        mBinding.recylerAnimeBookmark.setVisibility(View.VISIBLE);
        mBinding.linearError.setVisibility(View.GONE);
        mBinding.recylerAnimeBookmark.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        mBinding.recylerAnimeBookmark.setAdapter(new MainMangaAdapter(requireActivity(), historyModelList));
        mBinding.recylerAnimeBookmark.setHasFixedSize(true);
    }

    private void showErrorLayout(String errorMessage) {
        mBinding.recylerAnimeBookmark.setVisibility(View.GONE);
        Glide.with(mContext).asGif().load(R.raw.aquacry).into(mBinding.imageError);
        mBinding.textViewErrorMessage.setText(errorMessage);
        mBinding.linearError.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        getDataFromLocalDB("search", query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (!newText.isEmpty()){
            getDataFromLocalDB("search", newText);
        }
        return true;
    }
}