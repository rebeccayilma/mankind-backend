package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "wishlist", uniqueConstraints = @UniqueConstraint(columnNames = {"userId", "productId"}))

public class WishlistItem {
	
	public WishlistItem() {
    }
	
	public WishlistItem(Long id, Long userId, Long productId) {
	    this.id = id;
	    this.userId = userId;
	    this.productId = productId;
	}
		@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
		@JsonIgnore
	    private Long id;

	    private Long userId;

	    private Long productId;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Long getUserId() {
			return userId;
		}

		public void setUserId(Long userId) {
			this.userId = userId;
		}

		public Long getProductId() {
			return productId;
		}

		public void setProductId(Long productId) {
			this.productId = productId;
		}
	    
	    
	    
	  

}
