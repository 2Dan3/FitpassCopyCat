package rs.ftn.FitpassCopyCat.service;

import rs.ftn.FitpassCopyCat.model.entity.User;

public interface ImageService {
    String save(byte[] fileBytes, String originalFilename, User uploader);
}
