package io.web.chewing.common.converters;

import io.web.chewing.common.enums.OAuth2Config;
import io.web.chewing.model.ProviderUser;
import io.web.chewing.model.social.NaverUser;
import io.web.chewing.common.util.OAuth2Utils;

public class OAuth2NaverProviderUserConverter implements ProviderUserConverter<ProviderUserRequest, ProviderUser> {
    @Override
    public ProviderUser converter(ProviderUserRequest providerUserRequest) {
        if (!providerUserRequest.clientRegistration().getRegistrationId()
                .equals(OAuth2Config.SocialType.NAVER.getSocialName())) {
            return null;
        }
        return new NaverUser(OAuth2Utils.getSubAttributes(providerUserRequest.oAuth2User(), "response"),
                providerUserRequest.oAuth2User(),
                providerUserRequest.clientRegistration());
    }
}

