package info.mschmitt.githubapp.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubapp.application.NavigationManager;
import info.mschmitt.githubapp.utils.LoadingProgressManager;
import info.mschmitt.githubapp.viewmodels.RootViewModel;

/**
 * @author Matthias Schmitt
 */
@Module
public class RootViewModule {
    @Provides
    @Singleton
    public RootViewModel provideViewModel(LoadingProgressManager loadingProgressManager,
                                          NavigationManager navigationManager) {
        return new RootViewModel(loadingProgressManager, navigationManager);
    }
}
