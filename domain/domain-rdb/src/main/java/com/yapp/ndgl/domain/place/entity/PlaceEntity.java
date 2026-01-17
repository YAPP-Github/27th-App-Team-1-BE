package com.yapp.ndgl.domain.place.entity;

import com.yapp.ndgl.domain.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

	@Column(nullable = false, unique = true, length = 255)
	private String placeId;

	@Column(length = 1000)
	private String formattedAddress;

	@Column
	private Double latitude;

	@Column
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
	@Column(columnDefinition = "JSON")
	private String displayNameJson;

	@Lob
	@Column(columnDefinition = "JSON")
	private String regularOpeningHoursJson;

	@Lob
	@Column(columnDefinition = "JSON")
	private String photosJson;

	@Lob
	@Column(columnDefinition = "JSON")
	private String postalAddressJson;

	@Builder
	public PlaceEntity(
		final String placeId,
		final String formattedAddress,
		final Double latitude,
		final Double longitude,
		final Double rating,
		final String nationalPhoneNumber,
		final String internationalPhoneNumber,
		final String websiteUri,
		final String googleMapsUri,
		final Integer userRatingCount,
		final String displayNameJson,
		final String regularOpeningHoursJson,
		final String photosJson,
		final String postalAddressJson
	) {
		this.placeId = placeId;
		this.formattedAddress = formattedAddress;
		this.latitude = latitude;
		this.longitude = longitude;
		this.rating = rating;
		this.nationalPhoneNumber = nationalPhoneNumber;
		this.internationalPhoneNumber = internationalPhoneNumber;
		this.websiteUri = websiteUri;
		this.googleMapsUri = googleMapsUri;
		this.userRatingCount = userRatingCount;
		this.displayNameJson = displayNameJson;
		this.regularOpeningHoursJson = regularOpeningHoursJson;
		this.photosJson = photosJson;
		this.postalAddressJson = postalAddressJson;
	}
}
