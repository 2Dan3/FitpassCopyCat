package rs.ftn.FitpassCopyCat.service;

import rs.ftn.FitpassCopyCat.model.entity.Discipline;

public interface DisciplineService {
    Discipline findById(Long id);

    void remove(Discipline discipline);
}
