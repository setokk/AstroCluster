package edu.setokk.astrocluster.core.mapper;

/**
 * Interface inherited from classes that want to map from an origin(O) type to another target(T) type.
 * </br></br>
 * Used for bidirectional mapping (mapper goes both ways).
 * </br>
 * It does not technically matter which type is Initial or Target.
 * These terminologies are just used for making the API more understandable.
 * @param <T> the target type
 * @param <I> the initial type
 */
public interface IMapper<I, T> {
    default T mapToTarget(I i) { return null; }
    default I mapToInitial(T t) { return null; }
}
