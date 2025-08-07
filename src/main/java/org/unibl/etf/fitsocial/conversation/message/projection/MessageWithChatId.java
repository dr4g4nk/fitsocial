package org.unibl.etf.fitsocial.conversation.message.projection;

import java.time.Instant;

public interface MessageWithChatId {
    Long getId();
    String getContent();
    Long getChatId();
    String getLabel();
    String getSender();

    MessageWithChatId Instance = new MessageWithChatId() {
        @Override
        public Long getId() {
            return 0L;
        }

        @Override
        public String getContent() {
            return "";
        }

        @Override
        public Long getChatId() {
            return 0L;
        }

        @Override
        public String getLabel() {
            return "";
        }

        @Override
        public String getSender() {
            return "";
        }


    };
}
