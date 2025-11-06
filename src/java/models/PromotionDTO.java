package models;

import java.time.LocalDate;

/**
 * DTO biểu diễn thông tin một chương trình khuyến mãi.
 */
public class PromotionDTO {

	private String discountID;
	private String discountName;
	private String description;
	private double discountPercent;
	private LocalDate startDate;
	private LocalDate endDate;
	private boolean isActive;

	public PromotionDTO() {
	}

	public PromotionDTO(String discountID, String discountName, String description,
			double discountPercent, LocalDate startDate, LocalDate endDate, boolean isActive) {
		super();
		this.discountID = discountID;
		this.discountName = discountName;
		this.description = description;
		this.discountPercent = discountPercent;
		this.startDate = startDate;
		this.endDate = endDate;
		this.isActive = isActive;
	}

	public String getDiscountID() {
		return discountID;
	}

	public void setDiscountID(String discountID) {
		this.discountID = discountID;
	}

	public String getDiscountName() {
		return discountName;
	}

	public void setDiscountName(String discountName) {
		this.discountName = discountName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getDiscountPercent() {
		return discountPercent;
	}

	public void setDiscountPercent(double discountPercent) {
		this.discountPercent = discountPercent;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public String toString() {
		return "PromotionDTO{" + "discountID=" + discountID + ", discountName=" + discountName
				+ ", description=" + description + ", discountPercent=" + discountPercent + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", isActive=" + isActive + '}';
	}
}
