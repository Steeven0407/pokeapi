package com.api.pokeapi.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import io.github.cdimascio.dotenv.Dotenv;

@Service
public class CloudinaryService {

    Cloudinary cloudinary;

    public CloudinaryService(){
        Dotenv dotenv = Dotenv.load();
        cloudinary = new Cloudinary(dotenv.get("CLOUDINARY_URL"));
    }

    public Map<String, String> uploadFile(MultipartFile multipartFile, String filename) throws IOException {

        File file = convert(multipartFile);

        Map opciones = ObjectUtils.asMap(
            "folder", "poke_api",
            "public_id", filename
        );

        Map<String, String> result = cloudinary.uploader().upload(file, opciones);
        if (!Files.deleteIfExists(file.toPath())) {
            throw new IOException("Failed to delete temporary file: " + file.getAbsolutePath());
        }

        return result;

    }

    public Map<String, String> updateFile(MultipartFile multipartFile, String public_id) throws IOException {

        File file = convert(multipartFile);

        Map opciones = ObjectUtils.asMap(
            "public_id", public_id
        );

        Map<String, String> result = cloudinary.uploader().upload(file, opciones);
        if (!Files.deleteIfExists(file.toPath())) {
            throw new IOException("Failed to delete temporary file: " + file.getAbsolutePath());
        }

        return result;

    }

    public Map<String, String> delete(String id) throws IOException {
        return cloudinary.uploader().destroy(id, ObjectUtils.emptyMap());
    }

    private File convert(MultipartFile multipartFile) throws IOException {
        File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        FileOutputStream fo = new FileOutputStream(file);
        fo.write(multipartFile.getBytes());
        fo.close();
        return file;
    }
    
}
