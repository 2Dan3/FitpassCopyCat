package rs.ftn.FitpassCopyCat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.ftn.FitpassCopyCat.model.entity.Facility;

import java.util.List;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long>, JpaSpecificationExecutor<Facility> {

//    @Query("select f from Facility f where f.active = true")
    List<Facility> findByActiveTrue();

    @Query("SELECT f " +
            "FROM Facility f, Review rv, Comment c " +
            "WHERE " +
            "c.id = :commentId AND " +
            "c.id = rv.comment.id AND " +
            "rv.facility.id = f.id")
    Facility findByCommentLeft(@Param("commentId") Long commentId);

    @Query(nativeQuery = true, 
            value = 
            "SELECT DISTINCT f.facility_id, f.name, f.created_at, f.total_rating, f.description, f.city, f.address, f.active " +
            "FROM Facility f, Training t, User u " +
            "WHERE " +
            "f.facility_id = t.facility_id AND " +
            "t.user_id = u.user_id AND " +
            "u.user_id = :userId AND " +
            "f.active = true")
    List<Facility> findVisitedByUser(@Param("userId") Long userId);

    @Query("SELECT f " +
            "FROM Facility f, Review rv, Rating rt " +
            "WHERE rv.facility.id = f.id  AND " +
            "rv.rating.id = rt.id AND " +
            "(rt.equipment + rt.hygiene + rt.space + rt.staff) > 20 AND " +
            "f.active = true " +
            "GROUP BY f.id " +
            "ORDER BY (COUNT(case when rt.hygiene > 5 then 1 else 0 end)" +
            " + COUNT(case when rt.equipment > 5 then 1 else 0 end)" +
            " + COUNT(case when rt.staff > 5 then 1 else 0 end)" +
            " + COUNT(case when rt.space > 5 then 1 else 0 end)) DESC")
    List<Facility> findMostPopular();

    @Query(nativeQuery = true,
            value =
            "WITH tt AS " +
            "(SELECT f.facility_id as fid, d.name as nm " +
            "FROM facility f, discipline d, training t " +
            "WHERE t.user_id = :userId AND " +
            "t.facility_id = f.facility_id AND " +
            "d.facility_id = f.facility_id) " +
            "SELECT DISTINCT fc.facility_id, fc.name, fc.created_at, fc.total_rating, fc.description, fc.city, fc.address, fc.active " +
            "FROM facility fc, discipline di " +
            "WHERE NOT EXISTS (SELECT tt.fid FROM tt WHERE tt.fid = fc.facility_id) AND " +
            "NOT EXISTS (SELECT tt.nm FROM tt WHERE tt.nm = di.name) AND " +
            "fc.active = true")
    List<Facility> findUnexplored(@Param("userId") Long userId);

    @Query(nativeQuery = true,
            value =
            "WITH tt AS " +
            "(SELECT f.facility_id as fid, d.name as nm " +
            "FROM facility f, discipline d, training t " +
            "WHERE t.user_id = :userId AND " +
            "t.facility_id = f.facility_id AND " +
            "d.facility_id = f.facility_id) " +
            "SELECT DISTINCT fc.facility_id, fc.name, fc.created_at, fc.total_rating, fc.description, fc.city, fc.address, fc.active " +
            "FROM facility fc, discipline di " +
            "WHERE NOT EXISTS (SELECT tt.fid FROM tt WHERE tt.fid = fc.facility_id) AND " +
            "NOT EXISTS (SELECT tt.nm FROM tt WHERE tt.nm = di.name) AND " +
            "fc.active = true " +
            "LIMIT 5")
    List<Facility> findUnexploredLimit5(@Param("userId") Long userId);
}
