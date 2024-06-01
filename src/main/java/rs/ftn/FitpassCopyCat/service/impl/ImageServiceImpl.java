package rs.ftn.FitpassCopyCat.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import rs.ftn.FitpassCopyCat.model.entity.Facility;
import rs.ftn.FitpassCopyCat.model.entity.Image;
import rs.ftn.FitpassCopyCat.model.entity.User;
import rs.ftn.FitpassCopyCat.repository.ImageRepository;
import rs.ftn.FitpassCopyCat.service.ImageService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ImageServiceImpl implements ImageService {

    @Value("${imagePath}")
    private String PATH;
    private ImageRepository imageRepository;
//    private UserRepository userRepository;
//    private FacilityRepository facilityRepository;
    @Autowired
    public ImageServiceImpl(ImageRepository imageRepository /*,UserRepository userRepository, FacilityRepository facilityRepository*/) {
        this.imageRepository = imageRepository;
//        this.userRepository = userRepository;
//        this.facilityRepository = facilityRepository;
    }

    @Override
    public String saveUserImage(byte[] fileBytes, String originalFilename, User uploader) {
        Path oldPath = null;
        Path newPath = Path.of(PATH + originalFilename);

        if (uploader.getImage() != null) {
            try {
                oldPath = Path.of(uploader.getImage().getPath());
            } catch (InvalidPathException e) {
                e.printStackTrace();
            }
        }


        Image newImage = new Image();
        newImage.setPath(newPath.toString());
        newImage.setUser(uploader);
        uploader.setImage(newImage);

        if (oldPath != null) {
            try {
                Files.deleteIfExists(oldPath);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        try{
            Files.write(newPath, fileBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

//        this.userRepository.save(uploader);
        imageRepository.save(newImage);
        return newPath.toString();
    }

    @Override
    public void saveFacilityImages(List<MultipartFile> files, Facility owningFacility) {
        Set<Image> newImages = new HashSet<>();
        Path newPath;

        for (Image i : owningFacility.getImages()) {
            try {
                Path existingPath = Path.of(i.getPath());
                Files.deleteIfExists(existingPath);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        owningFacility.setImages(new HashSet<>());
//      todo - might also need to:
//        imageRepository.deleteAllForFacility(owningFacility.getId());

        for (MultipartFile f : files) {

            newPath = Path.of(PATH + f.getOriginalFilename());
            Image newImage = new Image();
            newImage.setPath(newPath.toString());
            newImage.setFacility(owningFacility);
            newImages.add(newImage);

            try{
                Files.write(newPath, f.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        owningFacility.setImages(newImages);
//        facilityRepository.save(owningFacility);
        imageRepository.saveAll(newImages);
//        imageRepository.saveAllAndFlush(images);
    }

//    todo ?
//     @Override
//     public List<MultipartFile> getAllImagesOfFacility(Facility targetFacility) {
//
//     }
}
