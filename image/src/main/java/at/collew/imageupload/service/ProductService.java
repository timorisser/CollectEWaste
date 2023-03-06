package at.collew.imageupload.service;


import at.collew.imageupload.entity.ProductImage;
import at.collew.imageupload.entity.ProfileImage;
//import at.collew.imageupload.entity.ImageData;
import at.collew.imageupload.respository.ProductRepository;
//import at.collew.imageupload.respository.StorageRepository;
import at.collew.imageupload.util.ImageTools;
import com.amazonaws.services.dynamodbv2.xspec.S;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
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
import java.util.*;

/**
 * This class contains the main functionalities for the imagehandleing,
 * the code uses Streams to handle the images received from the HTTP-Request.
 *
 * @author Aleksandar Latinovic
 * @version 25012023
 *
 *
 */

@Service
public class ProductService {

    @Value("${application.bucket.name}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3Client;

    @Autowired
    private ProductRepository productRepository;


    /**
     * This method contains the logic for the image upload, it uses streams and the ImageTools-class
     * to compress, resize and upload the images to AWS. The images are stored in the AWS S3 Bucket and
     * to access them easily, the URL to the image and the pid is stored in a database
     *
     * @param file the image
     * @param pid the Product ID
     */
    public ResponseEntity<?> uploadProductImages(MultipartFile file, int pid, int index) throws IOException {
        Map response = new HashMap();
        if(index<0 || index > 9){
            response.put("value", "ID has to be between 0 and 9");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).contentType(MediaType.APPLICATION_JSON).body(response);
        }
        List<ProductImage> images = productRepository.findAllByPid(pid);
        for(ProductImage i : images){
            if(i.getName().contains("-"+index)){
                response.put("value", "Index already in use");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).contentType(MediaType.APPLICATION_JSON).body(response);
            }
        }
        String PATH ="https://"+this.bucketName+".s3.eu-central-1.amazonaws.com/";
        String fileName = ""+pid+"-"+index+"p";
        String filePath = PATH+fileName; //creates the URL to the image
        BufferedImage image = ImageTools.createBufferedImageAndRotate(file); //
        byte[] imagebytes = ImageTools.resizeWithAffineTransform(image); //Uses the resize-method to resize the image to 40% less
        InputStream imageInputStream = new ByteArrayInputStream(imagebytes);
        //Adding the Metadata to the Object so AWS can view it properly
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(imagebytes.length);
        meta.setContentType("image/jpeg");
        s3Client.putObject(new PutObjectRequest(bucketName, fileName, imageInputStream, meta)); //Upload Image to AWS
        ProductImage profileImage = productRepository.save(new ProductImage(fileName, file.getContentType(), filePath, pid)); //Add image to Database
        if (profileImage != null) {
            response.put("value", filePath);
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
        }
        response.put("value", "Failed to upload Image");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    public ResponseEntity<?> getProductImages(int pid) {
        ArrayList<String> images = new ArrayList<>();
        Map response = new HashMap();
        List<ProductImage> fileData = productRepository.findAllByPid(pid);
        if (fileData.isEmpty()){
            response.put("value", "Image requested does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.valueOf("application/json"))
                    .body(response);
        }
        for(ProductImage pi : fileData){
            if(!s3Client.doesObjectExist(bucketName, pi.getName())){
                response.put("value", "Image "+pi.getName()+" does not exist in S3 Bucket, image entry in database will be removed please create new Image");
                this.deleteProductImages(pid);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.valueOf("application/json"))
                        .body(response);
            }
        }

        for (ProductImage pi : fileData){
                images.add(pi.getFilePath());
        }
        response.put("value",images);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("application/json"))
                .body(response);
    }

    public ResponseEntity<?> deleteProductImages(int pid) {
        Map response = new HashMap();
        if(productRepository.findAllByPid(pid).isEmpty()){
            response.put("value", "The image you tried to delete does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(response);
        }
        List<ProductImage> fileData = productRepository.findAllByPid(pid);
        for (ProductImage image : fileData){
            String fileName = image.getName();
            s3Client.deleteObject(bucketName, fileName);
            System.out.println(fileName);
        }
        productRepository.deleteAllBypid(pid);
        response.put("value", "true");
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }
}
