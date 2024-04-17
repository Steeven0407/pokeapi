package com.api.pokeapi.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import jakarta.annotation.PostConstruct;


@Service
public class CloudinaryService {

    // Instancia de cloudinary
    Cloudinary cloudinary;

    // URL de conexión del servicio
    @Value("${application.services.cloudinary_service}")
    private String cloudinary_sevice;

    /**
     * Método encargado de subir un archivo a cloudinary
     * @param multipartFile archivo
     * @param filename nombre del archivo
     * @return Map<String, String> con las propiedades del archivo subido
     * @throws IOException
     */
    public Map<String, String> uploadFile(MultipartFile multipartFile, String filename) throws IOException {

        // Convertimos en archivo para compatibilidad con cloudinary
        File file = convert(multipartFile);

        // Definimos la configuración de subida
        Map opciones = ObjectUtils.asMap(
            "folder", "poke_api",
            "public_id", filename
        );

        // Subimos el archivo y obtenemos las caracteristicas de cloudinary
        Map<String, String> result = cloudinary.uploader().upload(file, opciones);
        if (!Files.deleteIfExists(file.toPath())) { // Eliminamos el archivo en memoria
            throw new IOException("Failed to delete temporary file: " + file.getAbsolutePath());
        }

        return result;

    }

    /**
     * Método encargado de actualizar un archivo en cloudinary
     * @param multipartFile archivo
     * @param public_id identificador del archivo
     * @return Map<String, String> con las propiedades del nuevo archivo subido
     * @throws IOException
     */
    public Map<String, String> updateFile(MultipartFile multipartFile, String public_id) throws IOException {

        // Convertimos el nuevo archivo para compatibilidad con cloudinary
        File file = convert(multipartFile);

        // Definimos la configuración de subida
        Map opciones = ObjectUtils.asMap(
            "folder", "poke_api",
            "public_id", public_id
        );

        // Actualizamos el archivo y obtenemos las caracteristicas de cloudinary
        Map<String, String> result = cloudinary.uploader().upload(file, opciones);
        if (!Files.deleteIfExists(file.toPath())) {
            throw new IOException("Failed to delete temporary file: " + file.getAbsolutePath());
        }

        return result;

    }

    /**
     * Método encargado de eliminar un archivo en cloudinary
     * @param id del archivo
     * @return Map<String, String> con las propiedades del archivo eliminado
     * @throws IOException
     */
    public Map<String, String> delete(String id) throws IOException {
        return cloudinary.uploader().destroy(id, ObjectUtils.emptyMap());
    }

    /**
     * Método encargado de convertir un MultipartFile a File
     * @param multipartFile
     * @return un archivo tipo File
     * @throws IOException
     */
    private File convert(MultipartFile multipartFile) throws IOException {
        File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        FileOutputStream fo = new FileOutputStream(file);
        fo.write(multipartFile.getBytes());
        fo.close();
        return file;
    }

    // Inicializa la instancia de cloudinary una vez se obtiene la URL de conexión
    @PostConstruct
    public void init() {
        cloudinary = new Cloudinary(cloudinary_sevice);
    }
    
}
