/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author TEST
 */
public class CategoryDTO {
    private String categoryID;
    private String categoryName;
    private String sportType;
    private String parentCategoryID;

    public CategoryDTO() {
    }

    public CategoryDTO(String categoryID, String categoryName, String sportType, String parentCategoryID) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.sportType = sportType;
        this.parentCategoryID = parentCategoryID;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSportType() {
        return sportType;
    }

    public void setSportType(String sportType) {
        this.sportType = sportType;
    }

    public String getParentCategoryID() {
        return parentCategoryID;
    }

    public void setParentCategoryID(String parentCategoryID) {
        this.parentCategoryID = parentCategoryID;
    }

    @Override
    public String toString() {
        return "CategoryDTO{" + "categoryID=" + categoryID + ", categoryName=" + categoryName + ", sportType=" + sportType + ", parentCategoryID=" + parentCategoryID + '}';
    }
}
