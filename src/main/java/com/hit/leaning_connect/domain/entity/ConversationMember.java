package com.hit.leaning_connect.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "conversation_members")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationMember {
    @EmbeddedId
    private ConversationMemberId id;

    @ManyToOne
    @MapsId("convoId")
    @JoinColumn(name = "convo_id")
    private Conversation conversation;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "joined_at", nullable = false)
    private LocalDateTime joinedAt = LocalDateTime.now();

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConversationMemberId implements Serializable {

        @Column(name = "convo_id")
        private Long convoId;

        @Column(name = "user_id")
        private Long userId;
    }
}