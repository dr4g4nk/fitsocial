package org.unibl.etf.fitsocial.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.hateoas.server.core.Relation;

import java.io.Serializable;

/**
 * DTO for {@link org.unibl.etf.fitsocial.entity.PostMedia}
 */
@Relation(collectionRelation = "items")
public record PostMediaDto(Long id, @NotNull PostDto post, Integer ordering) implements Serializable {
}