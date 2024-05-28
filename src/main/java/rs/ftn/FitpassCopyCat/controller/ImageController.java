package rs.ftn.FitpassCopyCat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rs.ftn.FitpassCopyCat.model.entity.Facility;
import rs.ftn.FitpassCopyCat.model.entity.User;
import rs.ftn.FitpassCopyCat.service.FacilityService;
import rs.ftn.FitpassCopyCat.service.ImageService;
import rs.ftn.FitpassCopyCat.service.UserService;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "${apiPrefix}/images")
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
    public ResponseEntity<Void> uploadOwnUserImage(@RequestParam("file") MultipartFile file, Authentication loggedUser) throws IOException {

        User subjectUser = userService.findByEmail(loggedUser.getName());
        if (subjectUser == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        /*String path =*/ imageService.saveUserImage(file.getBytes(), file.getOriginalFilename(), subjectUser);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/facility/{id}")
    public ResponseEntity<Void> uploadFacilityImages(@RequestBody @NotBlank HashMap<String, MultipartFile> files, @PathVariable Long id, Authentication authentication) {

        Facility targetFacility = facilityService.findById(id);
        if (targetFacility == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        User loggedUploader = userService.findByEmail(authentication.getName());
        if (loggedUploader == null || !userService.managesFacility(targetFacility.getId(), loggedUploader))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        /*String path =*/ imageService.saveFacilityImages(files, targetFacility);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping
    public void getImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String imageURL = request.getParameter("path");
        String contentType = Files.probeContentType(Path.of(imageURL));
        if (contentType == null)
            contentType = "image/jpeg";
        response.setContentType(contentType);
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
