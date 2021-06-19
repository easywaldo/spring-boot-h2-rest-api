package com.approval.document.documentapproval.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class User {
    private String userId;
    private String userName;

    @Builder
    public User(String userId,
                String userName) {
      this.userId = userId;
      this.userName = userName;
    }
}
