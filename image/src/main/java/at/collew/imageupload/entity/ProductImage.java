package at.collew.imageupload.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.File;
/**
 * This is the persistance-class for the Product-Images,
 * this class uses Hibernate so I can combine Java with Postgres
 *
 * @author Aleksandar Latinovic
 * @version 25012023
 */
@Entity
@Table(name = "product_images")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "filepath")
    private String filePath;

    @Column(name ="pid")
    private int pid;

    public ProductImage(){

    }

    public ProductImage(String name, String type, String filePath, int pid) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.filePath = filePath;
        this.pid = pid;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getFilePath() {
        return filePath;
    }

    public int getPid() {
        return pid;
    }
}
