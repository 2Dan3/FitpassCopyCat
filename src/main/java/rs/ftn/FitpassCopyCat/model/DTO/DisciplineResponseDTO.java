package rs.ftn.FitpassCopyCat.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ftn.FitpassCopyCat.model.entity.Discipline;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DisciplineResponseDTO {
    private Long id;
    private String name;

    public DisciplineResponseDTO(Discipline d) {
        this.id = d.getId();
        this.name = d.getName();
    }
}
