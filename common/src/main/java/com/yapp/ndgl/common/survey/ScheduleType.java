package com.yapp.ndgl.common.survey;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ScheduleType {
  TIGHT("빡빡한 일정"),
  BALANCED("균형 잡힌 일정"),
  RELAXED("여유로운 일정");

  private final String description;
}
