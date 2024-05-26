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
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
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
        Path newPath = Path.of(PATH + originalFilename);

        Image newImage = new Image();
        newImage.setPath(newPath.toString());
        newImage.setUser(uploader);
        uploader.setImage(newImage);

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
    public void saveFacilityImages(HashMap<String, MultipartFile> files, Facility owningFacility) {
        Set<Image> images = new HashSet<>();
        Path newPath;

        for (String name : files.keySet()) {
            MultipartFile f = files.get(name);

            newPath = Path.of(PATH + f.getOriginalFilename());
            Image newImage = new Image();
            newImage.setPath(newPath.toString());
            newImage.setFacility(owningFacility);
            images.add(newImage);

            try{
                Files.write(newPath, f.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        owningFacility.setImages(images);
//        facilityRepository.save(owningFacility);
        imageRepository.saveAll(images);
//        imageRepository.saveAllAndFlush(images);
    }

//    todo ?
//     @Override
//     public List<MultipartFile> getAllImagesOfFacility(Facility targetFacility) {
//
//     }
}
