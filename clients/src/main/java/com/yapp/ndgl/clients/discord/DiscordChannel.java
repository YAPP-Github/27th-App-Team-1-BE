package com.yapp.ndgl.clients.discord;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DiscordChannel {

	USER_SUGGESTED_TEMPLATE("user-suggested-template");

	private final String key;
}
