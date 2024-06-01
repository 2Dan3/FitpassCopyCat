package rs.ftn.FitpassCopyCat.service;

import rs.ftn.FitpassCopyCat.model.DTO.WorkDayDTO;
import rs.ftn.FitpassCopyCat.model.entity.Facility;
import rs.ftn.FitpassCopyCat.model.entity.WorkDay;

public interface WorkDayService {
    WorkDay findWorkDay(WorkDayDTO data, Facility facility);

    WorkDay findById(Long id);
}
