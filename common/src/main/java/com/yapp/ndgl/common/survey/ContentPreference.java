package com.yapp.ndgl.common.survey;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ContentPreference {
  NEW_EXPERIENCE("새로운 시도 / 경험"),
  FOOD_FOCUSED("먹방 / 맛집 중심"),
  HEALING_SCENERY("힐링 / 풍경 위주"),
  LOCAL_CULTURE("로컬 감성 / 골목 여행");

  private final String description;
}
