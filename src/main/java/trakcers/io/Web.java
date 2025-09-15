package trakcers.io;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import trakcers.io.cacher.FastCache;
import trakcers.io.model.Device;
import trakcers.io.model.TrackEvent;
import trakcers.io.model.UserAccount;
import trakcers.io.repo.TrackLogRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class Web {
    @Autowired
    TrackLogRepo trackLogRepo;

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/")
    public String indexPage(Model model, @RequestParam(required = false) String device) {
        model.addAttribute("lat", 0);
        model.addAttribute("long", 0);
        model.addAttribute("zoom", 150);
        model.addAttribute("name", "Lucifer");
        List<String> deviceList = new ArrayList<>(FastCache.deviceCache.asMap().keySet());
        model.addAttribute("list", deviceList);
        try {
            TrackEvent trackEvent = FastCache.deviceCache.get(device);
            System.out.println("last id -> " + trackEvent.getTid());
            model.addAttribute("lat", trackEvent.getLatitude());
            model.addAttribute("long", trackEvent.getLongitude());
            return "index.html";
        } catch (Exception e) {
            return "index.html";
        }
    }

    @GetMapping("/route")
    public String setRoute(Model model,
                           @RequestParam(required = false) String startDate,
                           @RequestParam(required = false) String endDate,
                           @RequestParam(required = false) String deviceId) {


        List<TrackEvent> events = entityManager.createQuery(
                        "SELECT t FROM TrackEvent t WHERE t.date >= :startDate AND t.date <= :endDate  AND t.id = :deviceId", TrackEvent.class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .setParameter("deviceId", deviceId)
                .getResultList();


        // Convert to List of [lat, lng]
        List<List<String>> points = events.stream()
                .map(e -> List.of(e.getLatitude(), e.getLongitude()))
                .collect(Collectors.toList());

        List<Device> devices = new ArrayList<>();
        devices.add(new Device("25ec97a58877c8e7", "Tablet"));
        devices.add(new Device("82fa8c628b501db0", "Others"));


        // Add to model
        model.addAttribute("devices", devices);
        model.addAttribute("points", points);


        return "route.html";
    }

    @GetMapping("/login")
    public String signIn(Model model) {
        return "login.html";
    }

    @GetMapping("/register")
    public String signUp(Model model) {
        model.addAttribute("registerRequest", new UserAccount());
        return "register.html";
    }
}
