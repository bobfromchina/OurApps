//package com.example.wangbo.ourapp.cache;
//
//import com.jiamu.colorful.bean.GoodsDescriptionDetailsBean;
//import com.jiamu.colorful.bean.RecommendGoodsBean;
//
//import org.greenrobot.greendao.annotation.Entity;
//import org.greenrobot.greendao.annotation.Id;
//import org.greenrobot.greendao.annotation.Property;
//import org.greenrobot.greendao.annotation.ToMany;
//
//import java.util.List;
//
//import org.greenrobot.greendao.annotation.Generated;
//import org.greenrobot.greendao.DaoException;
//
//import com.example.wangbo.ourapp.greendao.DaoSession;
//import com.example.wangbo.ourapp.greendao.GoodsDescriptionDetailsBeanDao;
//import com.example.wangbo.ourapp.greendao.RecommendGoodsBeanDao;
//import com.example.wangbo.ourapp.greendao.SimpleHome2Dao;
//
///**
// * Created by wangbo on 2018/9/3.
// * <p>
// * 首页的缓存
// */
//@Entity
//public class SimpleHome2 {
//
//    public static final String TABLE_NAME = "home_cache_two";
//
//    @Id
//    private Long key;
//
//    @Property
//    private String phone;
//
//    /**
//     * homeBanner
//     */
////    @ToOne(joinProperty = "homeBannerId")
////    private BannerBean homeBanner;
//
//    /**
//     * 首页分类
//     */
////    @ToMany(referencedJoinProperty = "homeStatusId")
////    private List<HomeType> homeStatus;
//
//    /**
//     * 人气推荐
//     */
//    @ToMany(referencedJoinProperty = "key")
//    public List<RecommendGoodsBean> personRecommend;
//
//    /**
//     * 新品推荐
//     */
//    @ToMany(referencedJoinProperty = "key")
//    public List<GoodsDescriptionDetailsBean> newRecommend;
//
//    /**
//     * Used to resolve relations
//     */
//    @Generated(hash = 2040040024)
//    private transient DaoSession daoSession;
//
//    /**
//     * Used for active entity operations.
//     */
//    @Generated(hash = 1422894240)
//    private transient SimpleHome2Dao myDao;
//
//    @Generated(hash = 1191852200)
//    public SimpleHome2(Long key, String phone) {
//        this.key = key;
//        this.phone = phone;
//    }
//
//    @Generated(hash = 504555148)
//    public SimpleHome2() {
//    }
//
//    public Long getKey() {
//        return this.key;
//    }
//
//    public void setKey(Long key) {
//        this.key = key;
//    }
//
//    public String getPhone() {
//        return this.phone;
//    }
//
//    public void setPhone(String phone) {
//        this.phone = phone;
//    }
//
//    /**
//     * To-many relationship, resolved on first access (and after reset).
//     * Changes to to-many relations are not persisted, make changes to the target entity.
//     */
//    @Generated(hash = 1306902104)
//    public List<RecommendGoodsBean> getPersonRecommend() {
//        if (personRecommend == null) {
//            final DaoSession daoSession = this.daoSession;
//            if (daoSession == null) {
//                throw new DaoException("Entity is detached from DAO context");
//            }
//            RecommendGoodsBeanDao targetDao = daoSession.getRecommendGoodsBeanDao();
//            List<RecommendGoodsBean> personRecommendNew = targetDao
//                    ._querySimpleHome2_PersonRecommend(key);
//            synchronized (this) {
//                if (personRecommend == null) {
//                    personRecommend = personRecommendNew;
//                }
//            }
//        }
//        return personRecommend;
//    }
//
//    /**
//     * Resets a to-many relationship, making the next get call to query for a fresh result.
//     */
//    @Generated(hash = 843024012)
//    public synchronized void resetPersonRecommend() {
//        personRecommend = null;
//    }
//
//    /**
//     * To-many relationship, resolved on first access (and after reset).
//     * Changes to to-many relations are not persisted, make changes to the target entity.
//     */
//    @Generated(hash = 1756502430)
//    public List<GoodsDescriptionDetailsBean> getNewRecommend() {
//        if (newRecommend == null) {
//            final DaoSession daoSession = this.daoSession;
//            if (daoSession == null) {
//                throw new DaoException("Entity is detached from DAO context");
//            }
//            GoodsDescriptionDetailsBeanDao targetDao = daoSession
//                    .getGoodsDescriptionDetailsBeanDao();
//            List<GoodsDescriptionDetailsBean> newRecommendNew = targetDao
//                    ._querySimpleHome2_NewRecommend(key);
//            synchronized (this) {
//                if (newRecommend == null) {
//                    newRecommend = newRecommendNew;
//                }
//            }
//        }
//        return newRecommend;
//    }
//
//    /**
//     * Resets a to-many relationship, making the next get call to query for a fresh result.
//     */
//    @Generated(hash = 412073708)
//    public synchronized void resetNewRecommend() {
//        newRecommend = null;
//    }
//
//    /**
//     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
//     * Entity must attached to an entity context.
//     */
//    @Generated(hash = 128553479)
//    public void delete() {
//        if (myDao == null) {
//            throw new DaoException("Entity is detached from DAO context");
//        }
//        myDao.delete(this);
//    }
//
//    /**
//     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
//     * Entity must attached to an entity context.
//     */
//    @Generated(hash = 1942392019)
//    public void refresh() {
//        if (myDao == null) {
//            throw new DaoException("Entity is detached from DAO context");
//        }
//        myDao.refresh(this);
//    }
//
//    /**
//     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
//     * Entity must attached to an entity context.
//     */
//    @Generated(hash = 713229351)
//    public void update() {
//        if (myDao == null) {
//            throw new DaoException("Entity is detached from DAO context");
//        }
//        myDao.update(this);
//    }
//
//    /**
//     * called by internal mechanisms, do not call yourself.
//     */
//    @Generated(hash = 1905577484)
//    public void __setDaoSession(DaoSession daoSession) {
//        this.daoSession = daoSession;
//        myDao = daoSession != null ? daoSession.getSimpleHome2Dao() : null;
//    }
//
////    /**
////     * 热门专题
////     */
////    @ToOne(joinProperty = "homeProjectId")
////    private NewHomeModel homeProject;
//}
