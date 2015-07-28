package info.mschmitt.githubapp.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import info.mschmitt.githubapp.android.presentation.FragmentUtils;
import info.mschmitt.githubapp.android.presentation.Presentable;
import info.mschmitt.githubapp.databinding.UsernameViewBinding;
import info.mschmitt.githubapp.presenters.UsernameViewPresenter;


public class UsernameFragment extends Fragment implements Presentable<UsernameViewPresenter> {
    private FragmentHost mHost;
    private UsernameViewPresenter mPresenter;
    private UsernameViewPresenter.UsernameView mUsernameView =
            new UsernameViewPresenter.UsernameView() {
                @Override
                public UsernameViewPresenter.ParentPresenter getParentPresenter() {
                    return mHost.getPresenter();
                }
            };

    public static UsernameFragment newInstance() {
        return new UsernameFragment();
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
        mPresenter.onCreate(mUsernameView, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        UsernameViewBinding binding = UsernameViewBinding.inflate(inflater, container, false);
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
    public UsernameViewPresenter getPresenter() {
        return mPresenter;
    }

    @Inject
    public void setPresenter(UsernameViewPresenter presenter) {
        mPresenter = presenter;
    }

    public interface SuperComponent {
        void inject(UsernameFragment fragment);
    }

    public interface FragmentHost {
        SuperComponent getSuperComponent(UsernameFragment fragment);

        UsernameViewPresenter.ParentPresenter getPresenter();
    }
}