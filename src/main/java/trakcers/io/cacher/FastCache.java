package trakcers.io.cacher;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import trakcers.io.model.TrackEvent;

public class FastCache {
    public static LoadingCache<String, TrackEvent> deviceCache = CacheBuilder.newBuilder()
            .build(new CacheLoader<String, TrackEvent>() {
                @Override
                public TrackEvent load(String key) throws Exception {
                    return null;
                }
            });

}
