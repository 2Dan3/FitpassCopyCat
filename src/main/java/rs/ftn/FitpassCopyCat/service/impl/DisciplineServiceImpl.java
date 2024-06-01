package rs.ftn.FitpassCopyCat.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ftn.FitpassCopyCat.model.entity.Discipline;
import rs.ftn.FitpassCopyCat.repository.DisciplineRepository;
import rs.ftn.FitpassCopyCat.service.DisciplineService;

@Service
public class DisciplineServiceImpl implements DisciplineService {
    private DisciplineRepository disciplineRepository;
    @Autowired
    public DisciplineServiceImpl(DisciplineRepository disciplineRepository) {
        this.disciplineRepository = disciplineRepository;
    }


    @Override
    public Discipline findById(Long id) {
        return disciplineRepository.findById(id).orElse(null);
    }

    @Override
    public void remove(Discipline discipline) {
        disciplineRepository.delete(discipline);
    }
}
