package rs.ftn.FitpassCopyCat.service;

import rs.ftn.FitpassCopyCat.model.entity.Facility;

import java.util.List;

public interface FacilityService {
    Facility findById(Long id);

    Facility save(Facility newFacility);

    void updateFacilityRating(Facility facilityReviewed);

    List<Facility> findActive();

    Facility findByCommentLeft(Long commentId);

    List<Facility> findAll();
}
