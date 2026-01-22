package com.yapp.ndgl.domain.place.entity;

import com.yapp.ndgl.domain.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
	name = "place_photos",
	indexes = {
		@Index(name = "idx_place_photos_place_id", columnList = "google_place_id")
	}
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlacePhotoEntity extends BaseEntity {

	@Column(name = "google_place_id", nullable = false, length = 255)
	private String googlePlaceId;

	@Column(nullable = false, length = 2000, unique = true)
	private String photoName;

	@Column(nullable = false, length = 2000)
	private String photoUri;

	@Column(nullable = false)
	private Integer widthPx;

	@Column(nullable = false)
	private Integer heightPx;

	@Builder
	public PlacePhotoEntity(
		final String googlePlaceId,
		final String photoName,
		final String photoUri,
		final Integer widthPx,
		final Integer heightPx
	) {
		this.googlePlaceId = googlePlaceId;
		this.photoName = photoName;
		this.photoUri = photoUri;
		this.widthPx = widthPx;
		this.heightPx = heightPx;
	}
}
