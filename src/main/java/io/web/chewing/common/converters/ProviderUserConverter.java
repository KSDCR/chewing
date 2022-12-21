package io.web.chewing.common.converters;

import org.springframework.stereotype.Component;

public interface ProviderUserConverter<T,R> {

    R converter(T t);

}
