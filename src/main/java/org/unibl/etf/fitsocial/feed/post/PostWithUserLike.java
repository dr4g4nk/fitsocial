package org.unibl.etf.fitsocial.feed.post;

import org.unibl.etf.fitsocial.feed.like.Like;

public class PostWithUserLike {
    public Post post;
    public Like like;

    public PostWithUserLike(Post post, Like like) {
        this.post = post;
        this.like = like;
    }
}
