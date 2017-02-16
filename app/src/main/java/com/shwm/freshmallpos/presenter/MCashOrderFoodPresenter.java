package com.shwm.freshmallpos.presenter;

import com.shwm.freshmallpos.been.ClassesEntity;
import com.shwm.freshmallpos.been.FoodEntity;
import com.shwm.freshmallpos.manage.FoodListData;
import com.shwm.freshmallpos.model.biz.IRequestListener;
import com.shwm.freshmallpos.model.biz.OnClassesListener;
import com.shwm.freshmallpos.model.biz.OnFoodListener;
import com.shwm.freshmallpos.util.UL;
import com.shwm.freshmallpos.value.ValueType;
import com.shwm.freshmallpos.view.ICashOrderFoodView;

import java.util.ArrayList;
import java.util.List;

/**
 * 开单- 选择商品 Fragment
 */
public class MCashOrderFoodPresenter extends MBasePresenter<ICashOrderFoodView> {
    private static final String TAG = "MCashOrderFoodPresenter";
    int pageCurrent = 0;
    int pageType;
    private ICashOrderFoodView mView;
    private OnFoodListener onFoodListener;
    private OnClassesListener classesListener;
    /**
     * 当前类型
     */
    private int classesCurrentPosition;
    private ClassesEntity classesCurrent;
    private int classesCurrentId;
    /**
     * 当前商品列表
     */
    private List<FoodEntity> listFoodCurrent;
    private List<ClassesEntity> listClassesCurrent;
    private int classesCurrentNum;

    public MCashOrderFoodPresenter(ICashOrderFoodView iCashOrderFoodView) {
        this.mView = iCashOrderFoodView;
        onFoodListener = new OnFoodListener();
        classesListener = new OnClassesListener();
    }

    /**
     * 获取商品分类
     */
    public void getClassesList() {
        listClassesCurrent = FoodListData.getListClassesAllOne();
        if (listClassesCurrent != null && listClassesCurrent.size() > 0) {
            if (mView != null) {
                mView.showClassesList(listClassesCurrent);
            }
            return;
        }
        classesListener.getClassesAll(new IRequestListener<List<ClassesEntity>>() {
            @Override
            public void onSuccess(List<ClassesEntity> listClasses) {
                // TODO Auto-generated method stub
                UL.v(TAG, "size" + listClasses.size());
                FoodListData.setListClassesAll(listClasses);
                listClassesCurrent = FoodListData.getListClassesAllOne();
                if (mView != null) {
                    mView.showClassesList(listClassesCurrent);
                }
            }

            @Override
            public void onPreExecute(int type) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onFail(int statu, Exception exception) {
                // TODO Auto-generated method stub

            }
        });
    }

    /**
     * 通过分类查找商品
     */
    public void getFoodListByClasses() {
        listFoodCurrent = new ArrayList<FoodEntity>();
        classesCurrentPosition = mView.getClassesPosion();
        pageType = mView.getPageType();// 刷新、加载、default
        if (listClassesCurrent != null && listClassesCurrent.size() > classesCurrentPosition) {
            classesCurrent = listClassesCurrent.get(classesCurrentPosition);
        }
        if (classesCurrent == null) {
            return;
        }
        classesCurrentId = classesCurrent.getId();// 选中类型Id
        pageCurrent = FoodListData.getPageByClassesId(classesCurrentId, pageCurrent);
        listFoodCurrent = FoodListData.getListFoodByClassesId(classesCurrentId);
        if (listFoodCurrent == null) {
            listFoodCurrent = new ArrayList<FoodEntity>();
        }
        if (pageType == ValueType.PAGE_DEFAULT) { // 点击类型时 取不到保存的就刷新
            if (listFoodCurrent != null && listFoodCurrent.size() > 0) {
                UL.e(TAG, "listFoodCurrent is exit  return listFoodCurrent");
                for (FoodEntity food : listFoodCurrent) {
                    double num = FoodListData.getNumCartByFoodId(food.getId());
                    food.setNum(num);
                }
                if (mView != null) {
                    mView.showFoodList(listFoodCurrent);
                }
                return;
            }
        }
        if (pageType == ValueType.PAGE_REFRESH || pageType == ValueType.PAGE_DEFAULT) {
            pageCurrent = 1;
        } else if (pageType == ValueType.PAGE_LOAD) {
            pageCurrent++;
        }
        request(pageCurrent, pageType);
    }

    /**
     * 通过商品名模糊查找商品
     */
    public void getFoodListByLike(String strLike) {
        pageType = mView.getPageType();// 刷新、加载、default
        if (pageType == ValueType.PAGE_REFRESH || pageType == ValueType.PAGE_DEFAULT) {
            pageCurrent = 1;
        } else if (pageType == ValueType.PAGE_LOAD) {
            pageCurrent++;
        }
        onFoodListener.getFoodByLike(strLike, pageCurrent, pageType, new IRequestListener<List<FoodEntity>>() {

            @Override
            public void onPreExecute(int type) {
                // TODO Auto-generated method stub
                if (pageType == ValueType.PAGE_DEFAULT) {
                    mView.showDialogProgress();
                }
            }

            @Override
            public void onSuccess(List<FoodEntity> listFoods) {
                // TODO Auto-generated method stub

                if (pageType == ValueType.PAGE_DEFAULT) {
                    mView.dismissDialogProgress();
                }
                if (pageType == ValueType.PAGE_REFRESH) {
                    mView.dismissRefresh();
                }
                if (pageType == ValueType.PAGE_LOAD) {
                    if (listFoods != null && listFoods.size() > 0) {
                        mView.setLoadType(ValueType.LOAD_OVER);
                    } else {
                        mView.setLoadType(ValueType.LOAD_OVERALL);
                    }
                }
                if (listFoods != null) {
                    if (listFoods.size() > 0) {
                        for (FoodEntity foods : listFoods) {
                            double num = FoodListData.getNumCartByFoodId(foods.getId());
                            foods.setNum(num);
                        }
                        if (pageType == ValueType.PAGE_REFRESH) {
                            listFoodCurrent.clear();
                        }
                        listFoodCurrent.addAll(listFoods);
                    }
                }
                if (mView != null) {
                    mView.showFoodListByLike(listFoods);
                }
            }

            @Override
            public void onFail(int statu, Exception exception) {
                // TODO Auto-generated method stub
                // 加载失败提示
                if (pageType == ValueType.PAGE_DEFAULT) {
                    mView.dismissDialogProgress();
                }
                if (pageType == ValueType.PAGE_REFRESH) {
                    mView.dismissRefresh();
                }
                if (pageType == ValueType.PAGE_LOAD) {
                    mView.setLoadType(ValueType.LOAD_FAIL);
                }

            }
        });
    }

    private void request(final int pageCurrent, final int pageType) {
        onFoodListener.getFoodByClasses(listClassesCurrent.get(classesCurrentPosition), pageCurrent, pageType,
                new IRequestListener<List<FoodEntity>>() {
                    @Override
                    public void onPreExecute(int type) {
                        // TODO Auto-generated method stub
                        if (pageType == ValueType.PAGE_DEFAULT) {
                            mView.showDialogProgress();
                        }
                    }

                    @Override
                    public void onSuccess(List<FoodEntity> listFoods) {
                        // TODO Auto-generated method stub
                        if (pageType == ValueType.PAGE_DEFAULT) {
                            mView.dismissDialogProgress();
                        }
                        if (pageType == ValueType.PAGE_REFRESH) {
                            mView.dismissRefresh();
                        }
                        if (pageType == ValueType.PAGE_LOAD) {
                            if (listFoods != null && listFoods.size() > 0) {
                                mView.setLoadType(ValueType.LOAD_OVER);
                            } else {
                                mView.setLoadType(ValueType.LOAD_OVERALL);
                            }
                        }

                        if (listFoods != null) {
                            if (listFoods.size() > 0) {
                                for (FoodEntity foods : listFoods) {
                                    double num = FoodListData.getNumCartByFoodId(foods.getId());
                                    foods.setNum(num);
                                    foods.setClasses(classesCurrent);// 给商品添加类型
                                }
                                if (pageType == ValueType.PAGE_REFRESH) {
                                    listFoodCurrent.clear();
                                }
                                listFoodCurrent.addAll(listFoods);
                                FoodListData.setPageByClassesId(classesCurrentId, pageCurrent);
                                FoodListData.setListFoodByClassesId(classesCurrentId, listFoodCurrent);
                            }
                        }
                        if (mView != null) {
                            mView.showFoodList(listFoodCurrent);
                        }
                    }

                    @Override
                    public void onFail(int statu, Exception exception) {
                        // TODO Auto-generated method stub
                        // 加载失败提示
                        if (pageType == ValueType.PAGE_DEFAULT) {
                            mView.dismissDialogProgress();
                        }
                        if (pageType == ValueType.PAGE_REFRESH) {
                            mView.dismissRefresh();
                        }
                        if (pageType == ValueType.PAGE_LOAD) {
                            mView.setLoadType(ValueType.LOAD_FAIL);
                        }

                    }
                });
    }
    // 普通商品 +
    public void foodAddNum(int position, FoodEntity food) {
        addSubNum(position, food, true);
        mView.changeCart(food);
    }

    // 普通商品 -
    public void foodSubNum(int position, FoodEntity food) {
        addSubNum(position, food, false);
        mView.changeCart(food);
    }

    // 称重商品add
    public void foodAddWeight(FoodEntity food) {
        FoodListData.addToCart(food);// 保存 【购物车】
        if (food != null && !FoodListData.isExistCartByFoodId(food.getId())) {
            int classesId = food.getClasses().getId();
            FoodListData.setClassesFoodNumAddOrSub(classesId, true);
        }
        mView.changeCart(food);
        mView.changeItemClasses(-1);
    }

    // 称重商品remove
    public void foodRemoveWeight(FoodEntity food) {
        FoodListData.removeFromCart(food);
        if (food != null && FoodListData.isExistCartByFoodId(food.getId())) {
            int classesId = food.getClasses().getId();
            FoodListData.setClassesFoodNumAddOrSub(classesId, false);
        }
        mView.changeCart(food);
        mView.changeItemClasses(-1);
    }

    // 添加商品加减
    private void addSubNum(int position, FoodEntity food, boolean isAdd) {
        int posiontClasses = mView.getClassesPosion();
        // 当前类型的数量
        int foodId = food.getId();
        for (FoodEntity foodEntity : listFoodCurrent) {
            if (foodEntity.getId() == foodId) {
                double numBefore = foodEntity.getNum();
                if (isAdd) {
                    numBefore++;
                } else {
                    numBefore--;
                }
                if (numBefore >= 0) {
                    foodEntity.setNum(numBefore);
                    // 通过选择商品的类型改变数量
                    if (food.getClasses() != null) {
                        int classesId = food.getClasses().getId();
                        FoodListData.setClassesFoodNumAddOrSub(classesId, isAdd);
                    }
                }
                FoodListData.addToCart(foodEntity);// 保存 【购物车】
            }
        }
        mView.changeItemFood(position);// 通知适配器更新
        mView.changeItemClasses(position);
    }

}
