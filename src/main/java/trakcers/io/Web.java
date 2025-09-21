package trakcers.io;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import trakcers.io.cacher.FastCache;
import trakcers.io.model.Device;
import trakcers.io.model.TrackEvent;
import trakcers.io.model.UserAccount;
import trakcers.io.repo.DeviceRepo;
import trakcers.io.repo.TrackLogRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Controller
public class Web {
    @Autowired
    TrackLogRepo trackLogRepo;
    @Autowired
    DeviceRepo deviceRepo;

    @PersistenceContext
    private EntityManager entityManager;


    @GetMapping("/")
    public String indexPage(Authentication authentication,
                            Model model,
                            @RequestParam(required = false) String device) throws ExecutionException {
        model.addAttribute("lat", 0);
        model.addAttribute("long", 0);
        model.addAttribute("zoom", 15);
        //List<String> deviceList = new ArrayList<>(FastCache.deviceCache.asMap().keySet());

        List<Device> myDevices = entityManager.createQuery(
                        "SELECT t FROM Device t WHERE t.deviceOwner = :deviceOwner", Device.class)
                .setParameter("deviceOwner", authentication.getName())
                .getResultList();
        model.addAttribute("list", myDevices);
        return "index.html";

//        // todo make it so faster
//       try {
//           Device thisDevices = entityManager.createQuery(
//                           "SELECT t FROM Device t WHERE t.deviceId = :deviceId", Device.class)
//                   .setParameter("deviceId", device).getSingleResult();
//           model.addAttribute("name", thisDevices.getDeviceName());
//       } catch (Exception e ) {
//           model.addAttribute("name", "Point");
//       }

//        try {
//            TrackEvent trackEvent = FastCache.deviceCache.get(device);
//            model.addAttribute("lat", trackEvent.getLatitude());
//            model.addAttribute("long", trackEvent.getLongitude());
//            model.addAttribute("lastSeen", trackEvent.getDate());

//            return "index.html";
//        } catch (Exception e) {
//            return "index.html";
//        }
    }

    @GetMapping("/route")
    public String setRoute(Authentication authentication, Model model,
                           @RequestParam(required = false) String startDate,
                           @RequestParam(required = false) String endDate,
                           @RequestParam(required = false) String deviceId) {


        List<TrackEvent> events = entityManager.createQuery(
                        "SELECT t FROM TrackEvent t WHERE t.date >= :startDate AND t.date <= :endDate  AND t.id = :deviceId", TrackEvent.class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .setParameter("deviceId", deviceId)
                .getResultList();


        List<List<String>> points = events.stream()
                .map(e -> List.of(e.getLatitude(), e.getLongitude()))
                .collect(Collectors.toList());


        List<Device> devices = entityManager.createQuery(
                        "SELECT t FROM Device t WHERE t.deviceOwner = :deviceOwner", Device.class)
                .setParameter("deviceOwner", authentication.getName())
                .getResultList();


        model.addAttribute("devices", devices);
        model.addAttribute("points", points);


        return "route.html";
    }

    @GetMapping("/login")
    public String signIn(Model model) {
        return "login.html";
    }

    @GetMapping("/register")
    public String signUp(Model model, @RequestParam(required = false) String msg) {
        model.addAttribute("msg", msg);
        model.addAttribute("registerRequest", new UserAccount());

        return "register.html";
    }

    @GetMapping("/device")
    public String registerDevice(Model model) {
        model.addAttribute("device", new Device());
        return "device.html";
    }
}
