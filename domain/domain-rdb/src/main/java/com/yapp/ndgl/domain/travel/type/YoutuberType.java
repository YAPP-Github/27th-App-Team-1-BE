package com.yapp.ndgl.domain.travel.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum YoutuberType {
	PANI_BOTTLE("빠니보틀", "https://yt3.ggpht.com/Sr5y4IxegXCEZ0SYNvFB749crrAZmNpurZqfq2KvPEpiCYeakoMjBWMnW_56rMuYW_HipJOBRtU=s88-c-k-c0x00ffffff-no-rj");

	private final String name;
	private final String profileImage;
}
