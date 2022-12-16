//package io.web.chewing.repository.search;
//
//import com.querydsl.core.BooleanBuilder;
//import com.querydsl.jpa.JPQLQuery;
//import io.web.chewing.Entity.QReview;
//import io.web.chewing.Entity.Store;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
//
//import java.util.List;
//
//public interface StoreSearchImpl extends QuerydslRepositorySupport implements StoreSearch {
//
//    public StoreSearchImpl(){
//        super(Store.class);
//    }
//
//    @Override
//    public Page<Store> search1(Pageable pageable) {
//
//        QReview store = QStore.store;
//
//        JPQLQuery<Store> query = from(store);
//
//        BooleanBuilder booleanBuilder = new BooleanBuilder(); // (
//
//        booleanBuilder.or(store.title.contains("11")); // title like ...
//
//        booleanBuilder.or(store.content.contains("11")); // content like ....
//
//        query.where(booleanBuilder);
//        query.where(store.id.gt(0L));
//
//
//        //paging
//        this.getQuerydsl().applyPagination(pageable, query);
//
//        List<Store> list = query.fetch();
//
//        long count = query.fetchCount();
//
//
//        return null;
//
//    }
//
//    @Override
//    public Page<Store> searchAll(/*String[] types,*/ String keyword, Pageable pageable) {
//
//        QStore store = QStore.review;
//        JPQLQuery<Store> query = from(store);
//
//        if(/* (types != null && types.length > 0) &&*/ keyword != null ){ //검색 조건과 키워드가 있다면
//
//            BooleanBuilder booleanBuilder = new BooleanBuilder(); // (
//
//            for(String type: types){
//
//                switch (type){
//                    case "t":
//                        booleanBuilder.or(store.title.contains(keyword));
//                        break;
//                    case "c":
//                        booleanBuilder.or(store.content.contains(keyword));
//                        break;
//                    case "w":
//                        booleanBuilder.or(store.writer.contains(keyword));
//                        break;
//                }
//            }//end for
//            query.where(booleanBuilder);
//        }//end if
//
//        //id > 0
//        query.where(store.id.gt(0L));
//
//        //paging
//        this.getQuerydsl().applyPagination(pageable, query);
//
//        List<Store> list = query.fetch();
//
//        long count = query.fetchCount();
//
//        return new PageImpl<>(list, pageable, count);
//
//    }
//
//}
