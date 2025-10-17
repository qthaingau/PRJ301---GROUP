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
    private String name;
    private String parrentCategoryID;

    public CategoryDTO() {
    }

    public CategoryDTO(String categoryID, String name, String parrentCategoryID) {
        this.categoryID = categoryID;
        this.name = name;
        this.parrentCategoryID = parrentCategoryID;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParrentCategoryID() {
        return parrentCategoryID;
    }

    public void setParrent_categoryID(String parrentCategoryID) {
        this.parrentCategoryID = parrentCategoryID;
    }

    @Override
    public String toString() {
        return "CategoryDTO{" + "categoryID=" + categoryID + ", name=" + name + ", parrentCategoryID=" + parrentCategoryID + '}';
    }
    
    
}
