package rs.ftn.FitpassCopyCat.service;

import org.springframework.web.multipart.MultipartFile;
import rs.ftn.FitpassCopyCat.model.entity.Facility;
import rs.ftn.FitpassCopyCat.model.entity.User;

import java.util.HashMap;
import java.util.List;

public interface ImageService {
    String saveUserImage(byte[] fileBytes, String originalFilename, User uploader);

    void saveFacilityImages(List<MultipartFile> files, Facility owningFacility);
}
