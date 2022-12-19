package io.web.chewing.common.converters;

import io.web.chewing.common.enums.OAuth2Config;
import io.web.chewing.model.ProviderUser;
import io.web.chewing.model.social.GoogleUser;
import io.web.chewing.common.util.OAuth2Utils;

public class OAuth2GoogleProviderUserConverter implements ProviderUserConverter<ProviderUserRequest, ProviderUser> {
    @Override
    public ProviderUser converter(ProviderUserRequest providerUserRequest) {

        if (!providerUserRequest.clientRegistration().getRegistrationId()
                .equals(OAuth2Config.SocialType.GOOGLE.getSocialName())) {
            return null;
        }

        return new GoogleUser(OAuth2Utils.getMainAttributes(providerUserRequest.oAuth2User()),
                providerUserRequest.oAuth2User(), providerUserRequest.clientRegistration());
    }
}
