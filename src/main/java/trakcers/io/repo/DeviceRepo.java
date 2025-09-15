package trakcers.io.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import trakcers.io.model.Device;

@Repository
public interface DeviceRepo extends CrudRepository<Device, Long> {
}

