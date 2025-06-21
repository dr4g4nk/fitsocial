package org.unibl.etf.fitsocial.conversation.attachment;

import org.springframework.hateoas.server.core.Relation;
import core.dto.IBasicDto;
import core.dto.ICreateDto;
import core.dto.IListDto;
import core.dto.IUpdateDto;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import org.unibl.etf.fitsocial.conversation.message.MessageDto;

@Relation(collectionRelation = "items")
public record AttachmentDto(
	Long id,
	MessageDto message,
	@NotNull 
	String contentType,
	String fileUrl
) implements IBasicDto {
	@Relation(collectionRelation = "items")
    	public record Create(
			Long id,
			@NotNull
			String contentType,
			String fileUrl
	) implements ICreateDto {}

    	@Relation(collectionRelation = "items")
    	public record Update(
		Long id,
		@NotNull 
		String contentType,
		String fileUrl
	) implements IUpdateDto {}

    	@Relation(collectionRelation = "items")
    	public record List(
		Long id,
		MessageDto message,
		@NotNull
		String contentType,
		String fileUrl
	) implements IListDto {}
}