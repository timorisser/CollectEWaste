package at.collew.imageupload.controller;

import at.collew.imageupload.service.ProductService;
import at.collew.imageupload.service.ProfileService;
import com.amazonaws.services.xray.model.Http;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;

/**
 * This class handles the request and defines what and how the response looks like.
 * The Methods are accessed at api/v1/image/ProfileImage and api/v1/image/ProductImage.
 * They are all called the same,
 * because it can be automaticlly differenced between post, get and delete requests
 *
 * @author Aleksandar Latinovic
 * @version 25012023
 */
@RestController
@RequestMapping("api/v1/image")
public class ImageController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ProductService productService;

    /**
     * The uploadProfileImage can be accessed at api/v1/image/ProfileImage,
     * it calls the uploadmethod from the ProfileService-class.
     *
     * @param file the image for the upload
     * @param uid the User-ID
     * @return the URL where the image is located
     * @throws IOException
     */
    @PostMapping("/ProfileImage")
    public ResponseEntity<?> uploadProfileImage(@RequestParam("image") MultipartFile file, @RequestParam("uid") int uid) throws IOException {
        return profileService.uploadProfileImage(file, uid);
    }

    /**
     * The getProfileImage can be accessed at api/v1/image/ProfileImage,
     * it calls the getmethod from the ProfileService-class.
     *
     * @param uid the User-ID
     * @return the URL where the Image is located
     * @throws IOException
     */
    @GetMapping("/ProfileImage/{uid}")
    public ResponseEntity<?> getProfileImage(@PathVariable int uid) throws IOException {
        return profileService.getProfileImages(uid);
    }

    /**
     * The deleteProfileImage can be accessed at api/v1/image/ProfileImage,
     * it calls the deletemethod from the ProfileService-class.
     *
     * @param uid the User-ID where you want to delete the Profile-Image
     * @return true if sucessfully deleted
     */
    @DeleteMapping("/ProfileImage/{uid}")
    public ResponseEntity<?> deleteProfileImage(@PathVariable int uid) {
        return profileService.deleteProfileImage(uid);
    }

    /**
     * The updateProfileImage can be accessed at api/v1/image/ProfileImage,
     * it calls the updatemethod from the ProfileService-class.
     *
     * @param file the new Profile-Image
     * @param uid the User-ID
     * @return
     */
    @PatchMapping("/ProfileImage")
    public ResponseEntity<?> updateProfileImage(@RequestParam("image") MultipartFile file, @RequestParam("uid") int uid){
        return profileService.updateProfileImage(file, uid);
    }

    /**
     * The uploadProductImage can be accessed at api/v1/image/ProductImage,
     * it calls the uploadmethod from the ProductService-class.
     *
     * @param file the image for the upload
     * @param pid the Product-ID
     * @return
     * @throws IOException
     */
    @PostMapping("/ProductImage")
    public ResponseEntity<?> uploadProductImage(@RequestParam("image") MultipartFile file, @RequestParam("pid") int pid, @RequestParam("index") int index) throws IOException {
        return productService.uploadProductImages(file, pid, index);
    }

    /**
     * The getProductImage can be accessed at api/v1/image/ProductImage,
     * it calls the getmethod from the ProductService-class.
     *
     * @param pid the Product-ID
     * @return a list of all URLs of the product's image
     * @throws IOException
     */
    @GetMapping("/ProductImage/{pid}")
    public ResponseEntity<?> getProductImage(@PathVariable int pid) throws IOException {
        return productService.getProductImages(pid);
    }

    /**
     * The deleteProductImage can be accessed at api/v1/image/ProductImage,
     * it calls the deletemethod from the ProductService-class
     *
     * @param pid the Product-ID
     * @return true if successfully deleted
     */
    @DeleteMapping("/ProductImage/{pid}")
    public ResponseEntity<?> deleteProductImage(@PathVariable int pid) {
        return productService.deleteProductImages(pid);
    }

}