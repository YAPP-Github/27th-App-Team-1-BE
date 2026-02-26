package com.yapp.ndgl.domain.place.entity;

import com.yapp.ndgl.common.type.PlaceCategory;
import com.yapp.ndgl.domain.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "places")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceEntity extends BaseEntity {

	@Column(name = "google_place_id", nullable = false, unique = true, length = 255)
	private String googlePlaceId;

	@Column(length = 1000)
	private String formattedAddress;

	@Column(nullable = false)
	private Double latitude;

	@Column(nullable = false)
	private Double longitude;

	@Column
	private Double rating;

	@Column(length = 50)
	private String nationalPhoneNumber;

	@Column(length = 50)
	private String internationalPhoneNumber;

	@Column(length = 1000)
	private String websiteUri;

	@Column(length = 1000)
	private String googleMapsUri;

	@Column
	private Integer userRatingCount;

	@Lob
	@Column(nullable = false, length = 500)
	private String name;

	@Column(columnDefinition = "TEXT")
	private String thumbnail;

	@Lob
	@Column(columnDefinition = "JSON")
	private String regularOpeningHours;

	@Lob
	@Column(columnDefinition = "JSON")
	private String photosJson;

	@Column(length = 10)
	private String priceCurrencyCode;

	@Column(length = 20)
	private String priceStartUnits;

	@Column(length = 20)
	private String priceEndUnits;

	@Enumerated(EnumType.STRING)
	@Column(length = 50)
	private PlaceCategory category;

	@Column(length = 100)
	private String primaryType;

	@Lob
	@Column(columnDefinition = "JSON")
	private String nearbyPlacesJson;

	@Builder
	public PlaceEntity(
		final String googlePlaceId,
		final String formattedAddress,
		final Double latitude,
		final Double longitude,
		final Double rating,
		final String nationalPhoneNumber,
		final String internationalPhoneNumber,
		final String websiteUri,
		final String googleMapsUri,
		final Integer userRatingCount,
		final String name,
		final String thumbnail,
		final String regularOpeningHours,
		final String photosJson,
		final String priceCurrencyCode,
		final String priceStartUnits,
		final String priceEndUnits,
		final PlaceCategory category,
		final String primaryType,
		final String nearbyPlacesJson
	) {
		this.googlePlaceId = googlePlaceId;
		this.formattedAddress = formattedAddress;
		this.latitude = latitude;
		this.longitude = longitude;
		this.rating = rating;
		this.nationalPhoneNumber = nationalPhoneNumber;
		this.internationalPhoneNumber = internationalPhoneNumber;
		this.websiteUri = websiteUri;
		this.googleMapsUri = googleMapsUri;
		this.userRatingCount = userRatingCount;
		this.name = name;
		this.thumbnail = thumbnail;
		this.regularOpeningHours = regularOpeningHours;
		this.photosJson = photosJson;
		this.priceCurrencyCode = priceCurrencyCode;
		this.priceStartUnits = priceStartUnits;
		this.priceEndUnits = priceEndUnits;
		this.category = category;
		this.primaryType = primaryType;
		this.nearbyPlacesJson = nearbyPlacesJson;
	}
}
