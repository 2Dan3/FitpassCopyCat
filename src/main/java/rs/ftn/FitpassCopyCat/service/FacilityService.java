package rs.ftn.FitpassCopyCat.service;

import rs.ftn.FitpassCopyCat.model.entity.Facility;

public interface FacilityService {
    Facility findById(Long id);

    Facility save(Facility newFacility);
}
