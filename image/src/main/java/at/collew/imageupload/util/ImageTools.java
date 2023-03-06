package at.collew.imageupload.util;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifReader;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Rotation;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.logging.Logger;

/**
 * This class contains the resize and compress Methods for images
 *
 * @author Aleksandar Latinovic
 * @version 10012023
 *
 */
public class ImageTools {
    public static byte[] resizeWithAffineTransform(BufferedImage image) {
        int newWidth = (int) (image.getWidth() * 0.6);
        int newHeight = (int) (image.getHeight() * 0.6);
        //Checks if image is jpg or other format
        BufferedImage resizedImage = null;
        if (image.getType() == 5) {
            resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_3BYTE_BGR);
        } else {
            resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        }

        // Berechnen des Skalierungsfaktors, um das Seitenverhältnis beizubehalten
        double scaleX = (double) newWidth / image.getWidth();
        double scaleY = (double) newHeight / image.getHeight();
        double scale = Math.min(scaleX, scaleY);

        AffineTransform at = new AffineTransform();
        at.scale(scale, scale);
        AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        resizedImage = scaleOp.filter(image, resizedImage);
        // Compress image
        try {
            resizedImage = compressImage(resizedImage, 0.5f);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(resizedImage, "jpg", os);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        byte[] buffer = os.toByteArray();

        return buffer;
    }

    /**
     * converts the MultipartFile to a BufferedImage and rotates the image if the image is stored in a different Orientation.
     * To check if the image is rotated, the method checks the EXIF of the image (ExifIFD0Directory.TAG_ORIENTATION).
     *
     * @param file
     * @return
     */
    public static BufferedImage createBufferedImageAndRotate(MultipartFile file){
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream(); //get the inputstream of the File
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Metadata metadata = null;
        try {
            metadata = ImageMetadataReader.readMetadata(inputStream); //get the Metadata
        } catch (ImageProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ExifIFD0Directory exifIFD0 = metadata.getDirectory(ExifIFD0Directory.class);
        int orientation = 0;
        try {
            orientation = exifIFD0.getInt(ExifIFD0Directory.TAG_ORIENTATION); //get the int that identifies the orientation
        } catch (MetadataException e) {
            throw new RuntimeException(e);
        } catch (NullPointerException e){
            // Image does not contain exif
            // in most cases user upload images that were already compressed
            // therefore the EXIF gets deleted most of the time
        }
        Rotation rotation = null;
        switch (orientation) {
            case 1: // [Exif IFD0] Orientation - Top, left side (Horizontal / normal)
                break;
            case 6: // [Exif IFD0] Orientation - Right side, top (Rotate 90 CW)
                rotation = Rotation.CW_90;
                break;
            case 3: // [Exif IFD0] Orientation - Bottom, right side (Rotate 180)
                rotation =  Rotation.CW_180;
                break;
            case 8: // [Exif IFD0] Orientation - Left side, bottom (Rotate 270 CW)
                rotation = Rotation.CW_270;
        }
        BufferedImage image = null;
        try {
            image = ImageIO.read(file.getInputStream()); //converts the MultipartFile to a BufferedImage
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(rotation != null) {
            image = Scalr.rotate(image, rotation); //rotates the image
        }
        return image;
    }

    public static BufferedImage compressImage(BufferedImage image, float quality) throws IOException {
        // Get an image writer for JPEG format
        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();

        // Set the compression quality
        ImageWriteParam param = writer.getDefaultWriteParam();
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(quality);  // Quality ranges from 0 to 1, with higher values being higher quality

        // Create a byte array output stream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ImageOutputStream outputStream = ImageIO.createImageOutputStream(baos)) {
            writer.setOutput(outputStream);
            // Write the image
            writer.write(null, new IIOImage(image, null, null), param);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

        // Convert the output stream to a byte array
        byte[] data = baos.toByteArray();

        // Convert the byte array to a buffered image
        return ImageIO.read(new ByteArrayInputStream(data));
    }


}
