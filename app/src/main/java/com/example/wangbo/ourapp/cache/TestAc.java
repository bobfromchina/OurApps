//package com.example.wangbo.ourapp.cache;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.example.wangbo.ourapp.R;
//import com.example.wangbo.ourapp.greendao.GoodsDescriptionDetailsBeanDao;
//import com.example.wangbo.ourapp.greendao.SimpleHome2Dao;
//
//import java.util.List;
//
//public class TestAc extends Activity implements View.OnClickListener {
//
//    private TextView showMyCode;
//
//    SimpleHome2Dao homeDao;
//
//    GoodsDescriptionDetailsBeanDao goodDao;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.actvity_test);
//
//        //初始化
//        DBManager.init(this);
//        homeDao = DBManager.getInstance().getSession().getSimpleHome2Dao();
//        goodDao = DBManager.getInstance().getSession().getGoodsDescriptionDetailsBeanDao();
//
//        findViewById(R.id.set).setOnClickListener(this);
//        findViewById(R.id.get).setOnClickListener(this);
//        findViewById(R.id.delete).setOnClickListener(this);
//        showMyCode = (TextView) findViewById(R.id.showMyCode);
//    }
//
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.set: {
//
//                List<SimpleHome2> simpleHome = DBManager.getInstance().getSession().getSimpleHome2Dao().loadAll();
//
//                if (simpleHome.isEmpty()) {
//                    SimpleHome2 simpleHome2 = new SimpleHome2();
//
//                    simpleHome2.setPhone("1517888");
//
//                    homeDao.insert(simpleHome2);
//
//                    for (int i = 0; i < 1; i++) {
//                        goodDao.insert(new GoodsDescriptionDetailsBean(Long.parseLong(String.valueOf(i)),
//                                "1", "1", "1", i, "1",
//                                "1", "1", "1", "1", "1", "1", "1", "1"));
//                    }
//
//                    Toast.makeText(this, "新增成功！", Toast.LENGTH_SHORT).show();
//                } else {
//                    SimpleHome2 simpleHome2 = simpleHome.get(0);
//
//                    simpleHome2.setPhone("15178889347");
//
//                    for (int i = 0; i < 10; i++) {
//                        GoodsDescriptionDetailsBean bean = new GoodsDescriptionDetailsBean(Long.parseLong(String.valueOf(i)),
//                                "1", "1", "1", i, "1",
//                                "1", "1", "1", "1", "1", "1", "1", "1");
//                        goodDao.insert(bean);
//                    }
//
//                    Toast.makeText(this, "添加成功！", Toast.LENGTH_SHORT).show();
//                }
//            }
//            break;
//            case R.id.get: {
//                List<SimpleHome2> simpleHome = homeDao.loadAll();
//                if (simpleHome != null && !simpleHome.isEmpty()) {
//                    if (simpleHome.get(0).getNewRecommend() == null) {
//                        showMyCode.setText("getNewRecommend == null");
//                    } else {
//                        showMyCode.setText("我有" + goodDao.count() + "条数据," + simpleHome.get(0).getPhone());
//                    }
//                } else {
//                    showMyCode.setText("simpleHome == null");
//                }
//
//                Toast.makeText(this, "查询成功！", Toast.LENGTH_SHORT).show();
//            }
//
//            break;
//            case R.id.delete: {
//                List<SimpleHome2> simpleHome = homeDao.loadAll();
//                if (!simpleHome.isEmpty()) {
//                    goodDao.deleteAll();
//                    homeDao.deleteAll();
//                    Toast.makeText(this, "删除成功！", Toast.LENGTH_SHORT).show();
//                }
//            }
//            break;
////            public void btnAlter(View view) {
////                //根据一个实体来修改年龄
////                mUser.setAge(66);
////                DBManager.getmInstance().getSession().getUserDao().update(mUser);
////            }
//
//        }
//    }
//}
//
////                SimpleHome simpleHome = DataManager.getInstance().getCurrentUser();
////                if (simpleHome == null) {
////                    simpleHome = new SimpleHome();
////                }
////                BannerBean bannerBean = new BannerBean();
////                List<ShopBannerBean> bannerList = new ArrayList<>();
////                for (int i = 0; i < 10; i++) {
////                    bannerList.add(new ShopBannerBean("1", "1", "1", "1", "1", "1", "1"));
////                }
////                bannerBean.setBannerList(bannerList);
////                simpleHome.setHomeBanner(bannerBean);
////                simpleHome.setPhone(123456);
////                DataManager.getInstance().notifyUserChanged(simpleHome);
//
////                SimpleHome simpleHome = DataManager.getInstance().getCurrentUser();