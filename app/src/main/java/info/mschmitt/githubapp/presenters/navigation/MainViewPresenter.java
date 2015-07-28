package info.mschmitt.githubapp.presenters.navigation;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.support.annotation.MainThread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import info.mschmitt.githubapp.BR;
import info.mschmitt.githubapp.android.presentation.OnBackPressedListener;
import info.mschmitt.githubapp.presenters.navigation.scenes.RepositoriesSplitSceneViewPresenter;
import info.mschmitt.githubapp.presenters.navigation.scenes.UsernameSceneViewPresenter;

/**
 * @author Matthias Schmitt
 */
public class MainViewPresenter extends BaseObservable
        implements OnBackPressedListener, UsernameSceneViewPresenter.ParentPresenter,
        RepositoriesSplitSceneViewPresenter.ParentPresenter {
    private boolean mLoading;
    private List<Object> mLoadingQueue = new ArrayList<>();
    private Map<Object, Runnable> mCancelHandlers = new HashMap<>();
    private MainView mView;

    @Inject
    public MainViewPresenter() {
    }

    @MainThread
    public void onCreate(MainView view, Bundle savedState) {
        mView = view;
        if (savedState != null) {
        }
    }

    @MainThread
    public void onSave(Bundle outState) {
    }

    @MainThread
    public void onDestroy() {
        // stop long running operations
    }

    @MainThread
    @Override
    public void onLoading(Object sender, boolean complete, Runnable cancelHandler) {
        if (complete) {
            mLoadingQueue.remove(sender);
            mCancelHandlers.remove(sender);
            setLoading(!mLoadingQueue.isEmpty());
        } else {
            mLoadingQueue.add(sender);
            mCancelHandlers.put(sender, cancelHandler);
            setLoading(true);
        }
    }

    @MainThread
    @Override
    public boolean onBackPressed() {
        if (isLoading()) {
            ArrayList<Runnable> cancelHandlers = new ArrayList<>(mCancelHandlers.values());
            mCancelHandlers.clear();
            mLoadingQueue.clear();
            setLoading(false);
            for (Runnable cancelHandler : cancelHandlers) {
                cancelHandler.run();
            }
            return true;
        }
        boolean handled;
        Object childPresenter = mView.getChildPresenter();
        if (childPresenter instanceof OnBackPressedListener) {
            handled = ((OnBackPressedListener) childPresenter).onBackPressed();
            if (handled) {
                return true;
            }
        }
        handled = mView.tryShowPreviousChildView();
        return handled;
    }

    @Bindable
    public boolean isLoading() {
        return mLoading;
    }

    private void setLoading(boolean loading) {
        if (loading == mLoading) {
            return;
        }
        mLoading = loading;
        notifyPropertyChanged(BR.loading);
    }

    @Override
    public void onError(Object sender, Throwable throwable, Runnable retryHandler) {
        mView.showErrorDialog(throwable, retryHandler);
    }

    public interface MainView {
        ParentPresenter getParentPresenter();

        Object getChildPresenter();

        boolean tryShowPreviousChildView();

        void showErrorDialog(Throwable throwable, Runnable retryHandler);
    }

    public interface ParentPresenter {
    }
}