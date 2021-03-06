package info.mschmitt.githubbrowser.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import info.mschmitt.githubbrowser.android.FragmentUtils;
import info.mschmitt.githubbrowser.android.design.DividerItemDecoration;
import info.mschmitt.githubbrowser.databinding.RepositoryListViewBinding;
import info.mschmitt.githubbrowser.ui.adapters.RepositoryRecyclerViewAdapter;
import info.mschmitt.githubbrowser.ui.viewmodels.RepositoryListViewModel;

/**
 * @author Matthias Schmitt
 */
public class RepositoryListViewFragment extends Fragment {
    @Inject RepositoryListViewModel mViewModel;
    @Inject RepositoryRecyclerViewAdapter mAdapter;

    public static RepositoryListViewFragment newInstance() {
        return new RepositoryListViewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentUtils.getHost(this, FragmentHost.class).repositoryListViewComponent(this)
                .inject(this);
        mViewModel.onLoad(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RepositoryListViewBinding binding =
                RepositoryListViewBinding.inflate(inflater, container, false);
        binding.repositoriesView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.repositoriesView.addItemDecoration(
                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        binding.setViewModel(mViewModel);
        mAdapter.onCreateView(savedInstanceState);
        binding.setAdapter(mAdapter);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mViewModel.onSave(outState);
    }

    @Override
    public void onPause() {
        mViewModel.onPause();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        mAdapter.onDestroyView();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        mViewModel = null;
        mAdapter = null;
        super.onDestroy();
    }

    public interface FragmentHost {
        Component repositoryListViewComponent(RepositoryListViewFragment fragment);
    }

    public interface Component {
        void inject(RepositoryListViewFragment fragment);
    }
}
