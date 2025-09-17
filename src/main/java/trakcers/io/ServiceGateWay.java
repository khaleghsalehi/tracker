package trakcers.io;

import com.google.gson.Gson;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import trakcers.io.cacher.FastCache;
import trakcers.io.iam.RegisterRequest;
import trakcers.io.model.Device;
import trakcers.io.model.EventDTO;
import trakcers.io.model.TrackEvent;
import trakcers.io.model.UserAccount;
import trakcers.io.repo.DeviceRepo;
import trakcers.io.repo.TrackLogRepo;
import trakcers.io.repo.UserAccountRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/")
public class ServiceGateWay {
    @Autowired
    UserAccountRepository repo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager entityManager;


    @Autowired
    TrackLogRepo trackLogRepo;

    @Autowired
    DeviceRepo deviceRepo;

    @PostMapping("/getLocation")
    public void getLatLong(@RequestBody String rawJson) {
        TrackEvent trackEvent = new TrackEvent();
        Gson gson = new Gson();
        trackEvent = gson.fromJson(rawJson, TrackEvent.class);
        System.out.println("new track entry (event) successfully added. ->" + trackEvent.getDate());
        FastCache.deviceCache.put(trackEvent.getId(), trackEvent);
        trackLogRepo.save(trackEvent);
        return;
    }

    @PostMapping("/registerNewDevice")
    public void registerNewDevice(Authentication authentication,
                                  @ModelAttribute("device") Device device,
                                  HttpServletResponse response) throws IOException {


        List<Device> myDevices = entityManager.createQuery(
                        "SELECT t FROM Device t WHERE t.deviceId = :deviceId", Device.class)
                .setParameter("deviceId", device.getDeviceId())
                .getResultList();

        if (!myDevices.isEmpty()) {
            System.out.println("device already registered.");
            response.sendRedirect("/");
            return;
        }
        System.out.println(device.getDeviceId());
        System.out.println(device.getDeviceName());
        device.setDeviceOwner(authentication.getName());
        // todo if registered, ignore with error message
        deviceRepo.save(device);
        response.sendRedirect("/");
    }

    @PostMapping("/registerNewUser")
    public void registerUser(@ModelAttribute("registerRequest") RegisterRequest request,
                             HttpServletResponse response) throws IOException {
        System.out.println("username  -> " + request.getUsername());
        System.out.println("password  -> " + request.getPassword());
        // Check if username already exists
        if (repo.findByUsername(request.getUsername()).isPresent()) {
            response.sendRedirect("/register");
            return;
        }

        UserAccount user = new UserAccount();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // 🔐 encrypt
        user.setRole("ROLE_USER"); // default role

        repo.save(user);
        response.sendRedirect("/login");
        return;
    }

    @GetMapping("/getLastLatLong")
    public String setLastLocation(Authentication authentication,
                                  @RequestParam(required = false) String deviceID) {
        Gson gson = new Gson();
        EventDTO eventDTO = new EventDTO();
        try {
            Device thisDevices = entityManager.createQuery(
                            "SELECT t FROM Device t WHERE t.deviceId = :deviceId", Device.class)
                    .setParameter("deviceId", deviceID).getSingleResult();
            TrackEvent trackEvent = FastCache.deviceCache.get(deviceID);
            eventDTO.setDevLat(trackEvent.getLatitude());
            eventDTO.setDevLong(trackEvent.getLongitude());
            eventDTO.setTimeStamp(trackEvent.getDate());
            eventDTO.setDevName(thisDevices.getDeviceName());
        } catch (Exception e) {
            System.out.println("Ops, internal error");
        }
        return gson.toJson(eventDTO);
    }
}
