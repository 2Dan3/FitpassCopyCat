package rs.ftn.FitpassCopyCat.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
public class Manages {

    /**
     * Default constructor
     */
    public Manages() {
    }

    /**
     * 
     */
    private LocalDate startDate;

    /**
     * 
     */
    private LocalDate endDate;

}