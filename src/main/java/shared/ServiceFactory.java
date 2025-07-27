package shared;

import profile.repository.IUserRepository;
import profile.repository.UserRepositoryImplementor;
import profile.service.IProfileService;
import profile.service.ProfileServiceImplementor;
import statistics.service.IStatisticsService;
import statistics.service.StatisticsService;

/**
 * Factory for creating and managing singleton service instances.
 */
public class ServiceFactory {
    
    // Singleton instances
    private static final IUserRepository USER_REPOSITORY = new UserRepositoryImplementor();
    private static final IProfileService PROFILE_SERVICE = new ProfileServiceImplementor(USER_REPOSITORY);
    private static final IStatisticsService STATISTICS_SERVICE = StatisticsService.instance();
    
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
    
    /**
     * Gets the singleton statistics service instance.
     * 
     * @return the statistics service
     */
    public static IStatisticsService getStatisticsService() {
        return STATISTICS_SERVICE;
    }
}
