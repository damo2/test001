package com.shwm.freshmallpos.manage;

import android.text.TextUtils;
import android.util.SparseArray;

import com.shwm.freshmallpos.been.ClassesEntity;
import com.shwm.freshmallpos.been.FoodEntity;
import com.shwm.freshmallpos.util.UL;
import com.shwm.freshmallpos.util.UtilMath;

import java.util.ArrayList;
import java.util.List;

/**
 * 保存信息
 *
 * @author wr 2016-12-8
 */
public class FoodListData {
    private static final String TAG = "FoodListData";
    /**
     * 保存（每个类型的多页数据） 根据【类型Id】保存
     */
    private static SparseArray<List<FoodEntity>> listFoodSparse = new SparseArray<List<FoodEntity>>();
    /**
     * 保存（每个类型的多页数据）第几页 （根据【类型Id】保存）
     */
    private static SparseArray<Integer> listPageSparse = new SparseArray<Integer>();
    /**
     * 保存购物车 根据【商品Id】保存
     */
    private static SparseArray<FoodEntity> listCartSparse = new SparseArray<FoodEntity>();
    /**
     * 保存类型列表
     */
    private static List<ClassesEntity> listClassesAll = new ArrayList<ClassesEntity>();
    private static List<ClassesEntity> listClassesOne = new ArrayList<ClassesEntity>();
    /**
     * 保存选中商品 根据【商品Id】保存
     */
    private static SparseArray<FoodEntity> listChooseFood = new SparseArray<FoodEntity>();
    // private static SparseArray<Integer> listClassesFoodNumSparse = new SparseArray<Integer>();
    private static int foodIdnoCode;

    public static void setPageByClassesId(int classesId, int page) {
        FoodListData.listPageSparse.put(classesId, page);
    }

    /**
     * 获取所有分类（1级下面有2级）
     */
    public static List<ClassesEntity> getListClassesAll() {
        return listClassesAll;
    }

    /**
     * 保存所有分类（1级下面有2级）
     */
    public static void setListClassesAll(List<ClassesEntity> listClasses) {
        FoodListData.listClassesAll = listClasses;
        FoodListData.listClassesOne = new ArrayList<ClassesEntity>();
        if (listClassesAll != null && listClassesAll.size() > 0) {
            for (ClassesEntity classesEntity : listClassesAll) {
                List<ClassesEntity> listClassSub = classesEntity.getListSub();
                // listClassesCurrent.add(new ClassesEntity(classesEntity.getId(), classesEntity.getName(),
                // true));
                FoodListData.listClassesOne.addAll(listClassSub);
            }
        }
    }

    /**
     * 获取分类（所有2级）
     */
    public static List<ClassesEntity> getListClassesAllOne() {
        return listClassesOne;
    }

    /**
     * 通过类型Id 查保存的商品列表 （多页）
     *
     * @param classesId
     * @return 没有返回空
     */
    public static List<FoodEntity> getListFoodByClassesId(int classesId) {
        return FoodListData.listFoodSparse.get(classesId);
    }

    public static void setListFoodByClassesId(int classesId, List<FoodEntity> listFood) {
        FoodListData.listFoodSparse.put(classesId, listFood);
    }

    /**
     * 通过类型Id 查保存的商品列表的页数 @return 没有返回空
     */
    public static Integer getPageByClassesId(int classesId, int pageDefault) {
        return FoodListData.listPageSparse.get(classesId, pageDefault);
    }

    /******************* 购物车start **********************/

    /**
     * 添加到购物车
     */
    public static void addToCart(FoodEntity foodEntity) {
        if (foodEntity == null)
            return;
        int foodId = foodEntity.getId();
        double num = foodEntity.getNum();
        UL.e(TAG, "num=" + num);
        if (num > 0) {
            FoodListData.listCartSparse.put(foodId, foodEntity);// 保存 【购物车】
            UL.d(TAG, "保存 【购物车】  " + foodEntity.getName() + "   num=" + foodEntity.getNum() + "   id=" + foodEntity.getId());
        } else {
            if (FoodListData.listCartSparse.indexOfKey(foodId) >= 0) {
                FoodListData.listCartSparse.remove(foodId);// 从购物车移除小于0的商品
                UL.d(TAG, "移除 【购物车】  " + foodEntity.getName() + "   id=" + foodEntity.getId());
            }
        }
    }

    public static void removeFromCart(FoodEntity foodEntity) {
        if (foodEntity == null)
            return;
        int foodId = foodEntity.getId();
        if (FoodListData.listCartSparse.indexOfKey(foodId) >= 0) {
            FoodListData.listCartSparse.remove(foodId);// 从购物车移除小于0的商品
            UL.d(TAG, "移除 【购物车】  " + foodEntity.getName() + "   id=" + foodEntity.getId());
        }
    }

    /**
     * id商品是否存在购物车
     */
    public static boolean isExistCartByFoodId(int foodId) {
        return listCartSparse.indexOfKey(foodId) >= 0;
    }

    /**
     * 通过条形码在购物车查找商品
     */
    public static FoodEntity getFoodFromCartByBarcode(String barcode) {
        if (listCartSparse == null || TextUtils.isEmpty(barcode))
            return null;
        int size = listCartSparse.size();
        for (int i = 0; i < size; i++) {
            FoodEntity food = listCartSparse.valueAt(i);
            String barcodeFood = food.getBarcode();
            if (!TextUtils.isEmpty(barcodeFood) && barcodeFood.equals(barcode)) {
                UL.d(TAG, "购物车已存在" + food.getName());
                return food;
            }
        }
        return null;
    }

    /**
     * 得到商品id在购物车中的数量
     *
     * @return 数量 为空返回0
     */
    public static double getNumCartByFoodId(int foodId) {
        FoodEntity food = listCartSparse.get(foodId);
        if (food == null) {
            return 0;
        }
        return food.getNum();
    }

    /**
     * 获取购物车商品
     */
    public static List<FoodEntity> getCartAll() {
        List<FoodEntity> listFood = new ArrayList<FoodEntity>();
        for (int i = 0; i < FoodListData.listCartSparse.size(); i++) {
            listFood.add(FoodListData.listCartSparse.valueAt(i));
        }
        return listFood;
    }

    /**
     * 清除购物车数量
     */
    public static void clearCartNum() {
        if (FoodListData.listCartSparse != null) {
            FoodListData.listCartSparse.clear();
        }
        clearClassesNum();
    }


    /**
     * 购物车数量加1
     *
     * @param foodId 商品Id
     * @return 商品数量
     */
    public static double setFoodSupByIdFromCart(int foodId) {
        double numBefore = getNumCartByFoodId(foodId);
        UL.e(TAG, "numBefore=" + numBefore);
        double foodNum = UtilMath.add(numBefore, 1);
        UL.e(TAG, "numNew=" + foodNum);
        return setFoodNumByIdFromCart(foodId, foodNum);
    }

    /**
     * 购物车数量减1
     *
     * @param foodId 商品Id
     * @return 商品数量
     */
    public static double setFoodSubByIdFromCart(int foodId) {
        double numBefore = getNumCartByFoodId(foodId);
        UL.e(TAG, "numBefore=" + numBefore);
        double foodNum = UtilMath.sub(numBefore, 1);
        UL.e(TAG, "numNew=" + foodNum);
        return setFoodNumByIdFromCart(foodId, foodNum);
    }

    /**
     * 设置购物车数量
     *
     * @param foodId 商品Id
     * @param num    商品数量
     * @return 商品数量
     */
    public static double setFoodNumByIdFromCart(int foodId, double num) {
        if (FoodListData.listCartSparse.indexOfKey(foodId) >= 0) {
            FoodEntity food = FoodListData.listCartSparse.get(foodId);
            food.setNum(num);
            addToCart(food);
        }
        return getNumCartByFoodId(foodId);
    }

    /******************* 购物车end **********************/

    /***************
     * 类型start
     *************/

    public static void setClassesFoodNumAddOrSub(int classesId, boolean isAdd) {
        int numBefore = getClassesFoodNumByClassesId(classesId);
        if (isAdd) {
            numBefore++;
        } else {
            numBefore--;
        }
        if (numBefore >= 0) {
            for (ClassesEntity classes : listClassesOne) {
                if (classes.getId() == classesId) {
                    classes.setNum(numBefore);
                }
            }
        }
    }

    public static void setClassesNumAddOrSubByFood(FoodEntity food, boolean isAdd) {
        int classesId = food.getClasses().getId();
        FoodListData.setClassesFoodNumAddOrSub(classesId, isAdd);
    }

    public static Integer getClassesFoodNumByClassesId(int classesId) {
        if (listClassesOne == null)
            return 0;
        for (ClassesEntity classes : listClassesOne) {
            if (classes.getId() == classesId) {
                return classes.getNum();
            }
        }
        return 0;
    }

    /**
     * 清除类型数量
     */
    public static void clearClassesNum() {
        if (FoodListData.listClassesOne != null) {
            int size = FoodListData.listClassesOne.size();
            for (int i = 0; i < size; i++) {
                FoodListData.listClassesOne.get(i).setNum(0);
            }
        }
    }

    /*************** 类型end *************/

    /****************
     * 商品管理 choose start
     ********************/

    public static List<FoodEntity> getListChooseAll() {
        if (FoodListData.listChooseFood == null)
            return null;
        List<FoodEntity> listFood = new ArrayList<FoodEntity>();
        int size = FoodListData.listChooseFood.size();
        for (int i = 0; i < size; i++) {
            listFood.add(FoodListData.listChooseFood.valueAt(i));
        }
        return listFood;
    }

    /**
     * 是否存在 选中商品中
     */
    public static boolean isExitListChoose(FoodEntity food) {
        if (food == null) {
            return false;
        }
        return listChooseFood.indexOfKey(food.getId()) >= 0;
    }

    public static void addOrRemoveToListChoose(FoodEntity food) {
        if (food == null)
            return;
        int n = 1;
        ClassesEntity classes = food.getClasses();
        if (isExitListChoose(food)) {
            classes = food.getClasses();
            listChooseFood.remove(food.getId());
            n = -1;
        } else {
            listChooseFood.put(food.getId(), food);
            n = 1;
        }

        if (classes != null) {
            for (ClassesEntity classesEntity : listClassesOne) {
                if (classes.getId() == classesEntity.getId()) {
                    int num = classesEntity.getNum();
                    classesEntity.setNum(num + n);
                }
            }
        }
    }

    public static void removeFromListChoose(FoodEntity food) {

    }

    /**************** 商品管理 choose end ********************/

    /********************
     * 无码start
     ****************************/

    public static int getFoodIdNocode() {
        do {
            foodIdnoCode--;
            UL.d(TAG, "FoodIdNocode=" + foodIdnoCode);
        } while (isExistCartByFoodId(foodIdnoCode));
        return foodIdnoCode;
    }

    /********************
     * 无码end
     ****************************/

    public static void clearData() {
        if (listFoodSparse != null) {
            listFoodSparse.clear();
        }
        if (listPageSparse != null) {
            listPageSparse.clear();
        }
        if (listCartSparse != null) {
            listCartSparse.clear();
        }
        if (listClassesAll != null) {
            listClassesAll.clear();
        }
        if (listClassesOne != null) {
            listClassesOne.clear();
        }
        if (listChooseFood != null) {
            listChooseFood.clear();
        }
    }

    public static void clearFoodSparse() {
        if (listFoodSparse != null) {
            listFoodSparse.clear();
        }
    }

    /**
     * 清除选择商品
     */
    public static void clearChoose() {
        if (listChooseFood != null) {
            listChooseFood.clear();
        }
        clearClassesNum();
    }
}
