package trakcers.io.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Device {
    private String id;
    private String name;

    public Device(String id, String name) {
        this.id = id;
        this.name = name;
    }

}
