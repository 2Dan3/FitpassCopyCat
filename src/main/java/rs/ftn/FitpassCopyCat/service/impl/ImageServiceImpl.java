package rs.ftn.FitpassCopyCat.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import rs.ftn.FitpassCopyCat.model.entity.Image;
import rs.ftn.FitpassCopyCat.model.entity.User;
import rs.ftn.FitpassCopyCat.repository.ImageRepository;
import rs.ftn.FitpassCopyCat.repository.UserRepository;
import rs.ftn.FitpassCopyCat.service.ImageService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class ImageServiceImpl implements ImageService {

    @Value("${imagePath}")
    private String path;
    private ImageRepository imageRepository;
    private UserRepository userRepository;
    @Autowired
    public ImageServiceImpl(ImageRepository imageRepository, UserRepository userRepository) {
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
    }

    @Override
    public String save(byte[] fileBytes, String originalFilename, User uploader) {
        Path newPath = Path.of(path + originalFilename);

        Image newImage = new Image();
        newImage.setPath(newPath.toString());
        newImage.setUser(uploader);
        uploader.setImage(newImage);
        this.userRepository.save(uploader);

        try{
            Files.write(newPath, fileBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newPath.toString();
    }
}
