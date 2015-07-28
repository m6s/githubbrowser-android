package info.mschmitt.githubapp.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import info.mschmitt.githubapp.adapters.RepositoryPagerAdapter;
import info.mschmitt.githubapp.android.presentation.FragmentUtils;
import info.mschmitt.githubapp.android.presentation.Presentable;
import info.mschmitt.githubapp.databinding.RepositoryPagerViewBinding;
import info.mschmitt.githubapp.entities.Repository;
import info.mschmitt.githubapp.presenters.RepositoryPagerViewPresenter;
import rx.Observable;

/**
 * A placeholder fragment containing a simple view.
 */
public class RepositoryPagerFragment extends Fragment
        implements Presentable<RepositoryPagerViewPresenter>,
        RepositoryDetailsFragment.FragmentHost {
    private RepositoryPagerViewPresenter mPresenter;
    private FragmentHost mHost;
    private Observable<List<Repository>> mRepositories;
    private RepositoryPagerAdapter mAdapter;
    private RepositoryPagerViewPresenter.RepositoryPagerView mRepositoryPagerView =
            new RepositoryPagerViewPresenter.RepositoryPagerView() {
                @Override
                public RepositoryPagerAdapter getAdapter() {
                    return mAdapter;
                }

                @Override
                public RepositoryPagerViewPresenter.ParentPresenter getParentPresenter() {
                    return mHost.getPresenter();
                }
            };

    public static RepositoryPagerFragment newInstance() {
        RepositoryPagerFragment fragment = new RepositoryPagerFragment();
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mHost = FragmentUtils.getParent(this, FragmentHost.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHost.getSuperComponent(this).inject(this);
        ArrayList<Repository> repositories = new ArrayList<>();
        mAdapter = new RepositoryPagerAdapter(getChildFragmentManager(), repositories);
        mPresenter.onCreate(mRepositoryPagerView, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RepositoryPagerViewBinding binding =
                RepositoryPagerViewBinding.inflate(inflater, container, false);
        binding.setPresenter(mPresenter);
        return binding.getRoot();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mPresenter.onSave(outState);
    }

    @Override
    public void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        mHost = null;
        super.onDetach();
    }

    @Override
    public RepositoryPagerViewPresenter getPresenter() {
        return mPresenter;
    }

    @Inject
    public void setPresenter(RepositoryPagerViewPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public RepositoryDetailsFragment.SuperComponent getSuperComponent(
            RepositoryDetailsFragment fragment) {
        return mHost.getSuperComponent(this);
    }

    public interface SuperComponent extends RepositoryDetailsFragment.SuperComponent {
        void inject(RepositoryPagerFragment fragment);
    }

    public interface FragmentHost {
        SuperComponent getSuperComponent(RepositoryPagerFragment fragment);

        RepositoryPagerViewPresenter.ParentPresenter getPresenter();
    }
}