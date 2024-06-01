package rs.ftn.FitpassCopyCat.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ftn.FitpassCopyCat.model.DTO.WorkDayDTO;
import rs.ftn.FitpassCopyCat.model.entity.Facility;
import rs.ftn.FitpassCopyCat.model.entity.WorkDay;
import rs.ftn.FitpassCopyCat.repository.WorkDayRepository;
import rs.ftn.FitpassCopyCat.service.WorkDayService;

@Service
public class WorkDayServiceImpl implements WorkDayService {
    private WorkDayRepository workDayRepository;
    @Autowired
    public WorkDayServiceImpl(WorkDayRepository workDayRepository) {
        this.workDayRepository = workDayRepository;
    }

    @Override
    public WorkDay findWorkDay(WorkDayDTO data, Facility facility) {
        return workDayRepository.findByDayAndFacility(data.getDay(), facility);
    }

    @Override
    public WorkDay findById(Long id) {
        return workDayRepository.findById(id).orElse(null);
    }
}
