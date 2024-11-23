package com._roomthon.irumso.policy.view;

import com._roomthon.irumso.policy.supportPolicy.SupportPolicy;
import com._roomthon.irumso.user.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "viewed_policy")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ViewedPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "viewed_policy_id", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="support_policy_id")
    private SupportPolicy supportPolicy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @Builder
    public ViewedPolicy(SupportPolicy supportPolicy, User user){
        this.supportPolicy = supportPolicy;
        this.user = user;
    }
}
