package info.mschmitt.githubapp.dagger;

import java.util.LinkedHashMap;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubapp.application.RepositorySplitViewFragment;
import info.mschmitt.githubapp.entities.Repository;
import info.mschmitt.githubapp.scopes.RepositorySplitViewScope;
import info.mschmitt.githubapp.viewmodels.RepositorySplitViewModel;
import info.mschmitt.githubapp.viewmodels.qualifiers.RepositoryMapObservable;
import info.mschmitt.githubapp.viewmodels.qualifiers.SelectedRepositorySubject;
import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * @author Matthias Schmitt
 */
@Module
class RepositorySplitViewModule {
    @Provides
    @RepositorySplitViewScope
    @RepositoryMapObservable
    public Observable<LinkedHashMap<Long, Repository>> provideRepositoryMapObservable(
            RepositorySplitViewModel viewModel) {
        return viewModel.getRepositoryMapObservable();
    }

    @Provides
    @RepositorySplitViewScope
    @SelectedRepositorySubject
    public BehaviorSubject<Long> provideSelectedRepositorySubject(
            RepositorySplitViewModel viewModel) {
        return viewModel.getSelectedRepositorySubject();
    }

    @Provides
    @RepositorySplitViewScope
    public RepositorySplitViewFragment.Component provideComponent(
            RepositorySplitViewComponent component) {
        return component;
    }
}