package shared;

import profile.repository.IUserRepository;
import profile.repository.UserRepositoryImplementor;
import profile.service.IProfileService;
import profile.service.ProfileServiceImplementor;

/**
 * Factory for creating and managing singleton service instances.
 */
public class ServiceFactory {
    
    // Singleton instances
    private static final IUserRepository USER_REPOSITORY = new UserRepositoryImplementor();
    private static final IProfileService PROFILE_SERVICE = new ProfileServiceImplementor(USER_REPOSITORY);
    
    // Private constructor to prevent instantiation
    private ServiceFactory() {
        throw new UnsupportedOperationException("ServiceFactory is a utility class");
    }
    
    /**
     * Gets the singleton user repository instance.
     * 
     * @return the user repository
     */
    public static IUserRepository getUserRepository() {
        return USER_REPOSITORY;
    }
    
    /**
     * Gets the singleton profile service instance.
     * 
     * @return the profile service
     */
    public static IProfileService getProfileService() {
        return PROFILE_SERVICE;
    }
}
