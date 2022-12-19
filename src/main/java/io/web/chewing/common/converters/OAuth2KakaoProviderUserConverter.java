package io.web.chewing.common.converters;

import io.web.chewing.common.enums.OAuth2Config;
import io.web.chewing.model.ProviderUser;
import io.web.chewing.model.social.KakaoUser;
import io.web.chewing.common.util.OAuth2Utils;

// 해당 컨버터를 DelegatingProviderUserConverter에서 List로 모아서 사용하지 않고 있었음
public class OAuth2KakaoProviderUserConverter implements ProviderUserConverter<ProviderUserRequest, ProviderUser> {
    @Override
    public ProviderUser converter(ProviderUserRequest providerUserRequest) {
        if (!providerUserRequest.clientRegistration().getRegistrationId()
                .equals(OAuth2Config.SocialType.KAKAO.getSocialName())) {
            return null;
        }
        return new KakaoUser(OAuth2Utils.getOtherAttributes(providerUserRequest.oAuth2User(),
                "kakao_account", "profile"),
                providerUserRequest.oAuth2User(),
                providerUserRequest.clientRegistration());
    }
}
