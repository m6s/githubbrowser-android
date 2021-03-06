package info.mschmitt.githubbrowser.ui;

import android.support.v4.app.Fragment;

import javax.inject.Inject;

import info.mschmitt.githubbrowser.R;
import info.mschmitt.githubbrowser.ui.fragments.AboutViewDialogFragment;
import info.mschmitt.githubbrowser.ui.fragments.RepositorySplitViewFragment;
import info.mschmitt.githubbrowser.ui.fragments.RootViewFragment;
import info.mschmitt.githubbrowser.ui.scopes.RootViewScope;
import info.mschmitt.githubbrowser.ui.viewmodels.AboutViewModel;
import info.mschmitt.githubbrowser.ui.viewmodels.RepositorySplitViewModel;
import info.mschmitt.githubbrowser.ui.viewmodels.RootViewModel;
import info.mschmitt.githubbrowser.ui.viewmodels.UsernameViewModel;

/**
 * @author Matthias Schmitt
 */
@RootViewScope
public class NavigationService
        implements UsernameViewModel.NavigationService, RepositorySplitViewModel.NavigationService,
        RootViewModel.NavigationService, AboutViewModel.NavigationService {
    private final Fragment mRootViewFragment;

    @Inject
    public NavigationService(RootViewFragment rootViewFragment) {
        mRootViewFragment = rootViewFragment;
    }

    @Override
    public void showRepositorySplitView(String username) {
        mRootViewFragment.getChildFragmentManager().beginTransaction()
                .replace(R.id.contentView, RepositorySplitViewFragment.newInstance(username))
                .addToBackStack(null).commit();
    }

    @Override
    public void showError(Throwable throwable, Runnable retryHandler, Runnable cancelHandler) {
        AlertDialogs.showErrorDialog(mRootViewFragment.getContext(), throwable, retryHandler,
                cancelHandler);
    }

    @Override
    public void showAboutView() {
        AboutViewDialogFragment.newInstance().show(mRootViewFragment.getChildFragmentManager(),
                AboutViewDialogFragment.class.getSimpleName());
    }

    @Override
    public boolean goBack() {
        boolean handled = false;
        RepositorySplitViewFragment repositorySplitViewFragment = findRepositorySplitViewFragment();
        if (repositorySplitViewFragment != null) {
            handled = repositorySplitViewFragment.hideDetailsView();
        }
        if (!handled) {
            handled = mRootViewFragment.getChildFragmentManager().popBackStackImmediate();
        }
        return handled;
    }

    private RepositorySplitViewFragment findRepositorySplitViewFragment() {
        Fragment fragment =
                mRootViewFragment.getChildFragmentManager().findFragmentById(R.id.contentView);
        return fragment instanceof RepositorySplitViewFragment ?
                (RepositorySplitViewFragment) fragment : null;
    }

    @Override
    public boolean goUp() {
        return goBack();
    }
}
