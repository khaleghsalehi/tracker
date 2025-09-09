package trakcers.io;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import trakcers.io.model.TrackEvent;
import trakcers.io.repo.TrackLogRepo;

import java.util.List;

@Controller
public class Web {
    @Autowired
    TrackLogRepo trackLogRepo;

    @GetMapping("/")
    public String indexPage(Model model) {
        model.addAttribute("lat", 0);
        model.addAttribute("long", 0);
        model.addAttribute("zoom", 150);
        model.addAttribute("name", "Lucifer");
        try {
            List<TrackEvent> list = (List<TrackEvent>) trackLogRepo.findAll();
            TrackEvent obj = list.getLast();

            System.out.println("last id -> " + obj.getTid());
            model.addAttribute("lat", obj.getLatitude());
            model.addAttribute("long", obj.getLongitude());
            return "index.html";
        } catch (Exception e) {
            return "index.html";
        }
    }
}
