package info.mschmitt.githubbrowser.network;

import info.mschmitt.githubbrowser.network.internal.dagger.DaggerRetrofitNetworkComponent;
import info.mschmitt.githubbrowser.network.internal.dagger.RetrofitNetworkComponent;
import info.mschmitt.githubbrowser.network.internal.dagger.RetrofitNetworkModule;
import okhttp3.OkHttpClient;

/**
 * @author Matthias Schmitt
 */
public class Network {
    private final RetrofitNetworkComponent mRetrofitNetworkComponent;

    public Network(String baseUrl, boolean debug) {
        mRetrofitNetworkComponent = DaggerRetrofitNetworkComponent.builder()
                .retrofitNetworkModule(new RetrofitNetworkModule(baseUrl, debug)).build();
    }

    public GitHubService getGitHubService() {
        return mRetrofitNetworkComponent.getGitHubService();
    }

    public OkHttpClient getOkHttpClient() {
        return mRetrofitNetworkComponent.getOkHttpClient();
    }
}
