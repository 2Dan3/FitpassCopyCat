package rs.ftn.FitpassCopyCat.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ftn.FitpassCopyCat.model.entity.Facility;
import rs.ftn.FitpassCopyCat.repository.FacilityRepository;
import rs.ftn.FitpassCopyCat.service.FacilityService;

import java.util.Optional;

@Service
public class FacilityServiceImpl implements FacilityService {
    private FacilityRepository facilityRepository;
    @Autowired
    public FacilityServiceImpl(FacilityRepository facilityRepository){
        this.facilityRepository = facilityRepository;
    }

    @Override
    public Facility findById(Long id) {
        Optional<Facility> foundFacility = facilityRepository.findById(id);
        return foundFacility.orElse(null);
    }

    @Override
    public Facility save(Facility newFacility) {
        return facilityRepository.save(newFacility);
    }
}