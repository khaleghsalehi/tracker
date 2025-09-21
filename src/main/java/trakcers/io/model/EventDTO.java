package trakcers.io.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventDTO {
    private String devLat;
    private String devLong;
    private String devName;
    private String devLockStatus;
    private String devSecStatus;
    private String timeStamp;
    private String devBattery;

}
