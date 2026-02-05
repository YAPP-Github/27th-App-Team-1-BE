package com.yapp.ndgl.common.response;

import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SliceResponse<T> {

    private List<T> content;
    private boolean hasNext;

    @Builder
    private SliceResponse(final List<T> content, final boolean hasNext) {
        this.content = content;
        this.hasNext = hasNext;
    }

    public static <T> SliceResponse<T> of(final List<T> content, final boolean hasNext) {
        return SliceResponse.<T>builder()
            .content(content)
            .hasNext(hasNext)
            .build();
    }
}
