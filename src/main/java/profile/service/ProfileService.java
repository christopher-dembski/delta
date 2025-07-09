
package profile.service;

import profile.model.Profile;

import java.util.List;
import java.util.Optional;

/** High-level façade for all profile use-cases. */
public interface ProfileService {

    /* CRUD --------------------------------------------------------------- */

    Optional<Profile> getById(String id);

    void add(Profile profile);

    void update(Profile profile);

    List<Profile> listAll();

    /* Session handling ---------------------------------------------------- */

    /**
     * “Sign-in”: returns the profile and stores it as the current session.
     *
     * @return empty if no such profile id exists
     */
    Optional<Profile> openSession(String profileId);

    /** “Sign-out”: forget the current session (if any). */
    void closeSession();

    /** @return the signed-in user, or empty if no session is active. */
    Optional<Profile> getCurrentSession();
}
