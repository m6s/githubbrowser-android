package info.mschmitt.githubapp.app;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import info.mschmitt.githubapp.R;
import info.mschmitt.githubapp.android.presentation.FragmentUtils;
import info.mschmitt.githubapp.android.presentation.Presentable;
import info.mschmitt.githubapp.databinding.UsernameViewBinding;
import info.mschmitt.githubapp.modules.UsernameModule;
import info.mschmitt.githubapp.presenters.UsernamePresenter;


public class UsernameFragment extends Fragment
        implements Presentable<UsernamePresenter>, UsernamePresenter.UsernameView {
    private FragmentHost mHost;
    private UsernamePresenter mPresenter;
    private Component mComponent;

    public static UsernameFragment newInstance() {
        return new UsernameFragment();
    }

    @Override
    public UsernamePresenter.ParentPresenter getParentPresenter() {
        return mHost.getPresenter();
    }

    @Override
    public void showRepositories(Object sender, String username) {
        getFragmentManager().beginTransaction()
                .replace(FragmentUtils.getContainerViewId(UsernameFragment.this),
                        RepositoriesSplitFragment.newInstance(username)).addToBackStack(null)
                .commit();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mHost = FragmentUtils.getParent(this, FragmentHost.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mComponent = mHost.getSuperComponent(this).plus(new UsernameModule());
        mComponent.inject(this);
        mPresenter.onCreate(this, savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        UsernameViewBinding binding = UsernameViewBinding.inflate(inflater, container, false);
        binding.setPresenter(mPresenter);
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbar);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_about:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public UsernamePresenter getPresenter() {
        return mPresenter;
    }

    @Inject
    public void setPresenter(UsernamePresenter presenter) {
        mPresenter = presenter;
    }

    public interface Component {
        void inject(UsernameFragment fragment);
    }

    public interface SuperComponent {
        Component plus(UsernameModule module);
    }

    public interface FragmentHost {
        SuperComponent getSuperComponent(UsernameFragment fragment);

        UsernamePresenter.ParentPresenter getPresenter();
    }
}