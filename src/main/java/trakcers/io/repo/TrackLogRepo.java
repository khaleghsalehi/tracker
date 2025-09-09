package trakcers.io.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import trakcers.io.model.TrackEvent;

@Repository
public interface TrackLogRepo extends CrudRepository<TrackEvent, Long> {
}
