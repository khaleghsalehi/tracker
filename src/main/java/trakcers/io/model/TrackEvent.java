package trakcers.io.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter


@Entity
public class TrackEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tid;

    /**
    String deviceType; // Phone, Tablet, Laptop, PC, etc
    String deviceID;
    */

    //{"android":1, "id":"e2774a4a52aa681a","date":"2025-09-09 03:33:28","latitude":37.4219983, "longitude":-122.084,"altitude":5.0}
    String android;
    String id;
    String date;
    String locked;
    String secured;
    String battery;
    String latitude;
    String longitude;
    String altitude;
}
