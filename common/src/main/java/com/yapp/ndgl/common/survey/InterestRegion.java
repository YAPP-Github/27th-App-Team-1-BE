package com.yapp.ndgl.common.survey;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InterestRegion {
  DOMESTIC("국내"),
  JAPAN("일본"),
  CHINA("중화권"),
  SOUTHEAST_ASIA("동남아"),
  EUROPE("유럽"),
  NORTH_AMERICA("북미"),
  OCEANIA("오세아니아"),
  MIDDLE_EAST("중동");

  private final String description;
}
