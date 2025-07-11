package profile.service;

import java.util.List;
import java.util.Optional;

import profile.model.Profile;

public interface IProfileService {

    Optional<Profile> getById(Integer id);

    void add(Profile profile);

    void update(Profile profile);

    List<Profile> listAll();


    /**
     * "Sign-in": returns the profile and stores it as the current session.
     *
     * @return empty if no such profile id exists
     */
    Optional<Profile> openSession(Integer profileId);

    /** "Sign-out": forget the current session (if any). */
    void closeSession();

    /** @return the signed-in user, or empty if no session is active. */
    Optional<Profile> getCurrentSession();
}
