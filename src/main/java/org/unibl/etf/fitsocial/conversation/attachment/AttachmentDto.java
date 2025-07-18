package org.unibl.etf.fitsocial.conversation.attachment;

import org.springframework.hateoas.server.core.Relation;
import core.dto.IBasicDto;
import core.dto.ICreateDto;
import core.dto.IListDto;
import core.dto.IUpdateDto;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;
import org.unibl.etf.fitsocial.conversation.message.MessageDto;

@Relation(collectionRelation = "items")
public record AttachmentDto(
	Long id,
	String fileName,
	@NotNull 
	String contentType
) implements IBasicDto {
	@Relation(collectionRelation = "items")
    	public record Create(
				String fileName,
			@NotNull
			String contentType,
			MultipartFile file
	) implements ICreateDto {}

    	@Relation(collectionRelation = "items")
    	public record Update(
		Long id,
		String fileName,
		@NotNull 
		String contentType,
		MultipartFile file
	) implements IUpdateDto {}

    	@Relation(collectionRelation = "items")
    	public record List(
		Long id,
		String fileName,
		@NotNull
		String contentType
	) implements IListDto {}
}