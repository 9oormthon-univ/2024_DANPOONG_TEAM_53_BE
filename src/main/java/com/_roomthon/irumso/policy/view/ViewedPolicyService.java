package com._roomthon.irumso.policy.view;

import com._roomthon.irumso.policy.like.LikedPolicy;
import com._roomthon.irumso.policy.like.LikedPolicyDto;
import com._roomthon.irumso.policy.like.LikedPolicyRepository;
import com._roomthon.irumso.policy.supportPolicy.SupportPolicy;
import com._roomthon.irumso.policy.supportPolicy.SupportPolicyService;
import com._roomthon.irumso.user.User;
import com._roomthon.irumso.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ViewedPolicyService {
    private final UserService userService;
    private final SupportPolicyService supportPolicyService;
    private final ViewedPolicyRepository viewedPolicyRepository;

    public ViewedPolicyDto viewedPolicy(String nickName, Long policyId){
        User user = userService.findByNickname(nickName);
        SupportPolicy supportPolicy = supportPolicyService.getPolicyById(policyId);

        Optional<ViewedPolicy> existingViewedPolicy = viewedPolicyRepository.findBySupportPolicyAndUser(supportPolicy, user);

        if (existingViewedPolicy.isEmpty()) {
            ViewedPolicy viewedPolicy = new ViewedPolicy(supportPolicy, user);
            viewedPolicyRepository.save(viewedPolicy);

            supportPolicy.addViewedPolicy(viewedPolicy);
            supportPolicyService.saveSupportPolicy(supportPolicy);

            user.addViewedPolicy(viewedPolicy);
            userService.saveUser(user);

            return new ViewedPolicyDto(viewedPolicy);
        }
        return new ViewedPolicyDto(existingViewedPolicy.get());
    }
}
