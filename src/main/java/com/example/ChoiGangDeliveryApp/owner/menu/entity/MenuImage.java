package com.example.ChoiGangDeliveryApp.owner.menu.entity;

import com.example.ChoiGangDeliveryApp.common.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class MenuImage extends BaseEntity {
    private String imgName; // The name of the saved image file
    private String oriImgName; // The original name of the uploaded image file
    private String imgUrl; //// The URL path where the image is accessible
    private String repImgYn; //Set the first image as the representative menu image (Y/N)

    @ManyToOne(fetch = FetchType.LAZY) // Multiple images can belong to one item
    @JoinColumn(name = "menu_id")
    private MenuEntity item; // Reference to the associated menu entity

    // Method to update the image details
    public void updateMenuImg(
            String oriImgName,
            String imgName,
            String imgUrl
    ) {
        this.oriImgName = oriImgName; //update original img name
        this.imgName = imgName; // update the saved img name
        this.imgUrl = imgUrl; // update the img path
    }

}
