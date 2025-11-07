package models;

/**
 * DTO for Category table
 * @author TEST
 */
public class CategoryDTO {
    private String categoryID;
    private String categoryName;
    private String sportType;
    private boolean isActive; // ✅ Trạng thái hiển thị (1 = active, 0 = inactive)

    public CategoryDTO() {
    }

    public CategoryDTO(String categoryID, String categoryName, String sportType, boolean isActive) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.sportType = sportType;
        this.isActive = isActive;
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

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        return "CategoryDTO{" +
                "categoryID='" + categoryID + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", sportType='" + sportType + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
