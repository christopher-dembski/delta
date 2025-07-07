package profile.repository;

import profile.model.Profile;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<Profile> findById(String id);
    List<Profile>     findAll();
    boolean           add(Profile p);
    boolean           update(Profile p);
}
