package br.com.tenda.atacado.cupom.application.util;

import br.com.tenda.atacado.cupom.core.domain.Page;
import br.com.tenda.atacado.cupom.core.domain.PageRequest;
import br.com.tenda.atacado.cupom.core.domain.Sort;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class PageApplicationUtil {

    private PageApplicationUtil() {
    }

    public static PageRequest toPageRequest(Pageable pageable) {
        if (pageable == null || !pageable.isPaged()) {
            return new PageRequest(0, Integer.MAX_VALUE, Collections.emptyList());
        }

        List<Sort> sorts = pageable.getSort().stream()
                .map(order -> new Sort(
                        order.getProperty(),
                        order.getDirection().isAscending() ? Sort.Direction.ASC : Sort.Direction.DESC
                ))
                .collect(Collectors.toList());

        return new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sorts);
    }

    public static <T, U> org.springframework.data.domain.Page<U> toSpringPage(
            Page<T> domainPage,
            Pageable pageable,
            Function<T, U> mapper
    ) {
        List<U> content = domainPage.getContent().stream()
                .map(mapper)
                .collect(Collectors.toList());
        return new PageImpl<>(content, pageable, domainPage.getTotalElements());
    }
}
