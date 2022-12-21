package io.web.chewing.common.converters;

import io.web.chewing.Entity.Member;
import io.web.chewing.common.enums.OAuth2Config;
import io.web.chewing.common.util.OAuth2Utils;
import io.web.chewing.model.ProviderUser;
import io.web.chewing.model.social.NaverUser;
import io.web.chewing.model.users.FormMember;

public class UserDetailsProviderUserConverter implements ProviderUserConverter<ProviderUserRequest, ProviderUser> {
    @Override
    public ProviderUser converter(ProviderUserRequest providerUserRequest) {
        if (providerUserRequest.member() == null) {
            return null;
        }

        Member member = providerUserRequest.member();
        return FormMember.builder()
                .id(member.getEmail())
                .username(member.getEmail())
                .password(member.getPassword())
                .provider("none")
                .name(member.getName())
                .build();
    }
}

