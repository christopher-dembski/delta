package profile.repository;

import java.util.List;
import java.util.Optional;

import profile.model.Profile;

public interface IUserRepository {
    Optional<Profile> findById(Integer id);
    List<Profile>     findAll();
    boolean           add(Profile p);
    boolean           update(Profile p);
}
