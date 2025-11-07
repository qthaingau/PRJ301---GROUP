package models;

/**
 * DTO for Brand table
 */
public class BrandDTO {
    private String brandID;
    private String brandName;
    private String origin;
    private boolean isActive; // NEW: trạng thái hoạt động (1 = true, 0 = false)

    public BrandDTO() {
    }

    // CHANGED: thêm tham số isActive
    public BrandDTO(String brandID, String brandName, String origin, boolean isActive) {
        this.brandID = brandID;
        this.brandName = brandName;
        this.origin = origin;
        this.isActive = isActive;
    }

    // Giữ lại constructor cũ (nếu code cũ đang dùng)
    public BrandDTO(String brandID, String brandName, String origin) {
        this.brandID = brandID;
        this.brandName = brandName;
        this.origin = origin;
        this.isActive = true; // mặc định là true khi thêm mới
    }

    public String getBrandID() {
        return brandID;
    }

    public void setBrandID(String brandID) {
        this.brandID = brandID;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    // NEW
    public boolean isIsActive() {
        return isActive;
    }

    // NEW
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        return "BrandDTO{" +
                "brandID='" + brandID + '\'' +
                ", brandName='" + brandName + '\'' +
                ", origin='" + origin + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
