package at.collew.imageupload.service;


import at.collew.imageupload.entity.ProfileImage;
//import at.collew.imageupload.entity.ImageData;
import at.collew.imageupload.respository.ProfileRepository;
//import at.collew.imageupload.respository.StorageRepository;
import at.collew.imageupload.util.ImageTools;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.xray.model.Http;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
/**
 * This is the persistance-class for the Product-Images,
 * this class uses Hibernate so I can combine Java with Postgres
 */
@Service
public class ProfileService {

    @Value("${application.bucket.name}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3Client;

    @Autowired
    private ProfileRepository profileRepository;


    public ResponseEntity<?> uploadProfileImage(MultipartFile file, int uid) throws IOException {
        Map response = new HashMap();
        HttpStatus status = null;
        if(profileRepository.findByuid(uid).isPresent()){
            response.put("value", "Image already uploaded please use patch request");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).contentType(MediaType.APPLICATION_JSON).body(response);
        }
        //PATH to image
        String PATH ="https://"+bucketName+".s3.eu-central-1.amazonaws.com/";
        String fileName= uid+"-u";
        String filePath = PATH+fileName;
        BufferedImage image = ImageTools.createBufferedImageAndRotate(file);
        byte[] imagebytes = ImageTools.resizeWithAffineTransform(image);
        InputStream imageInputStream = new ByteArrayInputStream(imagebytes);
        //Metadaten hinzufügen zum Inputstream
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(imagebytes.length);
        meta.setContentType("image/jpeg");
        //Hochladen der Bilder in AWS
        s3Client.putObject(new PutObjectRequest(bucketName, fileName, imageInputStream, meta));
        ProfileImage profileImage = profileRepository.save(new ProfileImage(fileName,file.getContentType(), filePath, uid));
        if (profileImage != null) {
            response.put("value", filePath);
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
        }
        response.put("value", "Failed to upload image");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    public ResponseEntity<?> getProfileImages(int uid) throws IOException {
        Optional<ProfileImage> fileData = null;
        Map response = new HashMap<>();
        fileData = profileRepository.findByuid(uid);
        if(fileData.isEmpty()) {
            response.put("value", "Image requested does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(JSONValue.toJSONString(response));
        }
        if(!s3Client.doesObjectExist(bucketName, fileData.get().getName())){
            response.put("value", "Image "+fileData.get().getName()+" does not exist in S3 Bucket, image entry in database will be removed please create new Image");
            this.deleteProfileImage(uid);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.valueOf("application/json"))
                    .body(response);
        }
        String path = fileData.get().getFilePath();
        response.put("value", path);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    public ResponseEntity<?> deleteProfileImage(int uid) {
        Map response = new HashMap();
        String PATH ="https://"+bucketName+".s3.eu-central-1.amazonaws.com/";
        if(profileRepository.findByuid(uid).isEmpty()){
            response.put("value", "The image you tried to delete does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(response);
        }
        Optional<ProfileImage> fileDate = profileRepository.findByuid(uid);
        String fileName = fileDate.get().getFilePath().replace(PATH,"");
        s3Client.deleteObject(bucketName, fileName);
        profileRepository.deleteByuid(uid);
        response.put("value", "true");
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    /**
     * This Method updates the old profileimage to the newone only in the S3 bucket,
     * because the name stays the same the database doesn't have to change
     *
     * @param file new Profileimage
     * @param uid User-ID where to change
     * @return true when succeded
     * @throws IOException
     */
    public ResponseEntity<?> updateProfileImage(MultipartFile file, int uid){
        Map response = new HashMap();
        if(profileRepository.findByuid(uid).isEmpty()){
            if(profileRepository.findByuid(uid).isEmpty()){
                response.put("value", "The image you tried to delete does not exist");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(response);
            }
        }
        String PATH ="https://"+bucketName+".s3.eu-central-1.amazonaws.com/";
        Optional<ProfileImage> fileData = profileRepository.findByuid(uid);
        String fileName = fileData.get().getFilePath().replace(PATH,"");
        s3Client.deleteObject(bucketName, fileName);
        BufferedImage image = ImageTools.createBufferedImageAndRotate(file);
        byte[] imagebytes = ImageTools.resizeWithAffineTransform(image);
        InputStream imageInputStream = new ByteArrayInputStream(imagebytes);
        //Metadaten hinzufügen zum Inputstream
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(imagebytes.length);
        meta.setContentType("image/jpeg");
        //Hochladen der Bilder in AWS
        s3Client.putObject(new PutObjectRequest(bucketName, fileName, imageInputStream, meta));
        response.put("value", fileData.get().getFilePath());
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }
}
