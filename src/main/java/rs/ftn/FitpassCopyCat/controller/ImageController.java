package rs.ftn.FitpassCopyCat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rs.ftn.FitpassCopyCat.model.entity.User;
import rs.ftn.FitpassCopyCat.service.FacilityService;
import rs.ftn.FitpassCopyCat.service.ImageService;
import rs.ftn.FitpassCopyCat.service.UserService;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
@RequestMapping(path = "${apiPrefix}/image")
public class ImageController {
    private ImageService imageService;
    private UserService userService;
    private FacilityService facilityService;
    @Autowired
    public ImageController(ImageService imageService, UserService userService, FacilityService facilityService) {
        this.imageService = imageService;
        this.userService = userService;
        this.facilityService = facilityService;
    }

    @PostMapping
    public ResponseEntity<Void> uploadOwnUserImage(@RequestParam("imageFile") MultipartFile file, Authentication loggedUser) throws IOException {

        User subjectUser = userService.findByEmail(loggedUser.getName());
        if (subjectUser == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

//        Map<String,String> response = new HashMap<>();
        String imagePath = imageService.save(file.getBytes(), file.getOriginalFilename(), subjectUser);
//        response.put("path", imagePath);
        return new ResponseEntity<>(HttpStatus.OK);
    }

//  todo
//    @PostMapping(path = "/facility/{id}")
//    public ResponseEntity<Map<String,String>> uploadFacilityImages(@RequestParam("imageFile") MultipartFile file) throws IOException {
//        Map<String,String> response = new HashMap<>();
//        System.out.println("Original Image Byte Size - " + file.getBytes().length);
//        String path = imageRepository.save(file.getBytes(),file.getOriginalFilename());
//        response.put("path",path);
//        return new ResponseEntity(response, HttpStatus.OK);
//    }


    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping
    public void getImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String imageURL = request.getParameter("path");
        response.setContentType("image/jpeg");
        ServletOutputStream out = response.getOutputStream();
        FileInputStream fis = new FileInputStream(imageURL);

        BufferedInputStream bin = new BufferedInputStream(fis);
        BufferedOutputStream bout = new BufferedOutputStream(out);
        int ch = 0;
        while ((ch = bin.read()) != -1) {
            bout.write(ch);
        }

        bin.close();
        fis.close();
        bout.close();
        out.close();
    }
}
