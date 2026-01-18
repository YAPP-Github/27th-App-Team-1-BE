package com.yapp.ndgl.common.survey;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TravelCompanion {
  ALONE("혼자"),
  FRIENDS("친구"),
  PARTNER("연인"),
  FAMILY("가족");

  private final String description;
}
