package com._roomthon.irumso.policy.like;

import com._roomthon.irumso.policy.supportPolicy.SupportPolicy;
import com._roomthon.irumso.policy.supportPolicy.SupportPolicyService;
import com._roomthon.irumso.user.User;
import com._roomthon.irumso.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikedPolicyService {
    private final UserService userService;
    private final SupportPolicyService supportPolicyService;
    private final LikedPolicyRepository likedPolicyRepository;

    public LikedPolicyDto setLikeToPolicy(String nickName, Long policyId){
        User user = userService.findByNickname(nickName);
        SupportPolicy supportPolicy = supportPolicyService.getPolicyById(policyId);

        Optional<LikedPolicy> existingLikedPolicy = likedPolicyRepository.findBySupportPolicyAndUser(supportPolicy, user);

        if (existingLikedPolicy.isEmpty()) {
            LikedPolicy likedPolicy = new LikedPolicy(supportPolicy, user);
            likedPolicyRepository.save(likedPolicy);

            supportPolicy.addLikedPolicy(likedPolicy);
            supportPolicyService.saveSupportPolicy(supportPolicy);

            user.addLikedPolicy(likedPolicy);
            userService.saveUser(user);

            return new LikedPolicyDto(likedPolicy);
        }

        return new LikedPolicyDto(existingLikedPolicy.get());
    }

}
