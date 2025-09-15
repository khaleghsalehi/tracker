package trakcers.io;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import trakcers.io.cacher.FastCache;
import trakcers.io.model.TrackEvent;
import trakcers.io.repo.TrackLogRepo;

@RestController
@RequestMapping("/v1/")
public class ServiceGateWay {
    @Autowired
    TrackLogRepo trackLogRepo;

    @PostMapping("/getLocation")
    public void getLatLong(@RequestBody String rawJson) {
        TrackEvent trackEvent = new TrackEvent();
        Gson gson = new Gson();
        trackEvent = gson.fromJson(rawJson, TrackEvent.class);
        System.out.println("new track entry (event) successfully added. ->" + trackEvent.getDate());
        FastCache.deviceCache.put(trackEvent.getId(), trackEvent);
        trackLogRepo.save(trackEvent);
    }
}
