package io.web.chewing.common.converters;

import io.jsonwebtoken.lang.Assert;
import io.web.chewing.model.ProviderUser;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Component
public class DelegatingProviderUserConverter implements ProviderUserConverter<ProviderUserRequest, ProviderUser> {

    private List<ProviderUserConverter<ProviderUserRequest, ProviderUser>> converters;
    // DelegatingProviderUserConverter에서 Arrays.asList에서 OAuth2NaverProviderUserConverter만 2개 적었음
    // 다음부턴 코드 제대로 확인하고 클래스 단위로 사용된지 확인할것
    public DelegatingProviderUserConverter() {
        List<ProviderUserConverter<ProviderUserRequest, ProviderUser>> providerUserConverters =
                Arrays.asList(new OAuth2GoogleProviderUserConverter(),
                        new OAuth2NaverProviderUserConverter(),
                        new OAuth2KakaoProviderUserConverter(),
                        new UserDetailsProviderUserConverter());

                this.converters = Collections.unmodifiableList(new LinkedList<>(providerUserConverters));
    }

    @Override
    public ProviderUser converter(ProviderUserRequest providerUserRequest) {

        Assert.notNull(providerUserRequest, "providerUserRequest cannot be null");

        for (ProviderUserConverter<ProviderUserRequest, ProviderUser> converter : converters){
            ProviderUser providerUser = converter.converter(providerUserRequest);
            if (providerUser != null) return providerUser;
        }

        return null;
    }
}
