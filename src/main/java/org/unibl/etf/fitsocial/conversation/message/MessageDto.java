package org.unibl.etf.fitsocial.conversation.message;

import org.springframework.hateoas.server.core.Relation;
import core.dto.IBasicDto;
import core.dto.ICreateDto;
import core.dto.IListDto;
import core.dto.IUpdateDto;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

import org.unibl.etf.fitsocial.auth.user.UserDto;
import org.unibl.etf.fitsocial.conversation.attachment.AttachmentDto;
import org.unibl.etf.fitsocial.conversation.chat.ChatDto;
import org.unibl.etf.fitsocial.conversation.chatuser.ChatUserDto;

@Relation(collectionRelation = "items")
public record MessageDto(
	Long id,
	Long chatId,
	UserDto user,
	@NotNull 
	String content
) implements IBasicDto {
	@Relation(collectionRelation = "items")
	public record Create(@NotNull Long chatId, @NotNull String content, java.util.List<AttachmentDto.Create> attachments) implements ICreateDto {
	}
	@Relation(collectionRelation = "items")
	public record Update(@NotNull Long chatId, @NotNull String content) implements IUpdateDto {
	}

    	@Relation(collectionRelation = "items")
    	public record List(
		Long id,
		Long chatId,
		UserDto user,
		@NotNull
		String content
	) implements IListDto {}
}