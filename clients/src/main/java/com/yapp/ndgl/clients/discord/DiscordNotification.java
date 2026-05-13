package com.yapp.ndgl.clients.discord;

import java.util.List;

import com.yapp.ndgl.clients.discord.request.DiscordEmbed;

/**
 * Discord 채널에 발송할 알림의 표현 형식.
 * <p>알림 종류별로 구현체를 추가해 확장한다.
 */
public interface DiscordNotification {

	/**
	 * 알림을 전송할 Discord webhook 채널.
	 */
	DiscordChannel channel();

	/**
	 * 메시지 상단 본문(content). 없으면 {@code null}.
	 */
	String createContent();

	/**
	 * Discord embed 목록. 없으면 {@code null} 또는 빈 리스트.
	 */
	List<DiscordEmbed> createEmbeds();
}
