package com.shwm.freshmallpos.activity;

import java.io.Serializable;
import java.util.List;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.been.ClassesEntity;
import com.shwm.freshmallpos.been.FoodEntity;
import com.shwm.freshmallpos.presenter.MFoodEditPresenter;
import com.shwm.freshmallpos.util.BitmapUtil;
import com.shwm.freshmallpos.util.ConfigUtil;
import com.shwm.freshmallpos.util.GetPhotoUtil;
import com.shwm.freshmallpos.util.GetPhotoUtil.OnPhotoListener;
import com.shwm.freshmallpos.util.ImageLoadUtil;
import com.shwm.freshmallpos.util.StringFormatUtil;
import com.shwm.freshmallpos.util.StringUtil;
import com.shwm.freshmallpos.util.UL;
import com.shwm.freshmallpos.util.UT;
import com.shwm.freshmallpos.value.ValueFinal;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.value.ValueRequest;
import com.shwm.freshmallpos.value.ValueType;
import com.shwm.freshmallpos.view.IFoodEditView;
import com.shwm.freshmallpos.base.BaseActivity;
import com.zxing.activity.CodeActivity;

/**
 * 商品编辑
 */
public class FoodEditActivity extends BaseActivity<IFoodEditView, MFoodEditPresenter> implements IFoodEditView {
    private String title;
    private int editType;
    private EditText edtName, edtCode, edtPrice, edtPriceMember, edtUnit, edtNumsum, edtFrom, edtDesc;
    private ClassesEntity classes;
    private Button btnSubmit, btnClasses;
    private ImageView ivCodeIcon;
    private ImageView ivImg;
    private RadioGroup rgroupWeight;
    private RadioButton rbtnDefault;
    private RadioButton rbtnWeidght;

    private Uri bitmapUri;
    private GetPhotoUtil utilPhoto;

    private FoodEntity foodEdit;
    private FoodEntity foodDetail;
    private int foodIdEdit;

    private List<ClassesEntity> listClassesAll;// 所有类型

    @Override
    protected void onCreate(Bundle bundle) {
        // TODO Auto-generated method stub
        super.onCreate(bundle);
        if (editType == ValueType.EDIT) {
            mPresenter.getFoodByFoodId(false);
        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_foodedit;
    }

    @Override
    public MFoodEditPresenter initPresenter() {
        // TODO Auto-generated method stub
        return new MFoodEditPresenter(this);
    }

    @Override
    protected void init() {
        super.init();
        Bundle bundle = getIntent().getExtras();
        editType = bundle.getInt(ValueKey.FOODEDIT_TYPE);
        title = bundle.getString(ValueKey.TITLE);
        classes = (ClassesEntity) bundle.getSerializable(ValueKey.CLASSES);
        if (editType == ValueType.EDIT) {
            foodEdit = (FoodEntity) bundle.getSerializable(ValueKey.FOOD);
            if (foodEdit != null) {
                foodIdEdit = foodEdit.getId();
            }
        }

        utilPhoto = new GetPhotoUtil(FoodEditActivity.this, ConfigUtil.FoodImageWH, ConfigUtil.FoodImageWH);
    }

    @Override
    protected void initView() {
        super.initView();
        edtName = (EditText) findViewById(R.id.edt_foodedit_foodname);
        edtCode = (EditText) findViewById(R.id.edt_foodedit_foodcode);
        edtPrice = (EditText) findViewById(R.id.edt_foodedit_foodprice);
        edtPriceMember = (EditText) findViewById(R.id.edt_foodedit_foodpriceMember);
        edtUnit = (EditText) findViewById(R.id.edt_foodedit_foodunit);
        edtNumsum = (EditText) findViewById(R.id.edt_foodedit_foodnumsum);
        edtFrom = (EditText) findViewById(R.id.edt_foodedit_foodfrom);
        edtDesc = (EditText) findViewById(R.id.edt_foodedit_fooddesc);
        btnSubmit = (Button) findViewById(R.id.btn_foodedit_submit);
        btnClasses = (Button) findViewById(R.id.btn_foodedit_foodclass);
        ivCodeIcon = (ImageView) findViewById(R.id.iv_foodedit_foodcodeIcon);
        ivImg = (ImageView) findViewById(R.id.iv_foodedit_img);

        rgroupWeight = (RadioGroup) findViewById(R.id.rg_foodedit_typeWeight);
        rbtnDefault = (RadioButton) findViewById(R.id.rb_foodedit_typeDefaule);
        rbtnWeidght = (RadioButton) findViewById(R.id.rb_foodedit_typeWeight);
    }

    @Override
    protected void setValue() {
        super.setValue();
        if (classes != null) {
            btnClasses.setText(classes.getName());
        } else {
            UT.showShort(context, "null");
        }
        if (editType == ValueType.EDIT && foodEdit != null) {
            ImageLoadUtil.displayImage(ivImg, foodEdit.getImg(), ImageLoadUtil.getOptionsImgFood());
        }
        if (editType == ValueType.ADD) {
            rgroupWeight.check(rbtnDefault.getId());
        }
    }

    @Override
    protected void setListener() {
        super.setListener();
        btnSubmit.setOnClickListener(this);
        btnClasses.setOnClickListener(this);
        ivCodeIcon.setOnClickListener(this);
        utilPhoto.setOnPhotoListener(onPhotoListener);
        ivImg.setOnClickListener(this);
        rgroupWeight.setOnCheckedChangeListener(onCheckedChange);
    }
    @Override
    protected void initToolbar() {
        // TODO Auto-generated method stub
        super.initToolbar();
        setToolbar(R.id.toolbar_foodedit,title);
    }

    private RadioGroup.OnCheckedChangeListener onCheckedChange = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup radiogroup, int checkedId) {
            // TODO Auto-generated method stub
            // if (checkedId == rbtnDefault.getId()) {
            // typeWeight = ValueTypeUtil.TypeWeight_Default;
            // }
            // if (checkedId == rbtnWeidght.getId()) {
            // typeWeight = ValueTypeUtil.TypeWeight_Weight;
            // }
        }
    };
    private OnPhotoListener onPhotoListener = new OnPhotoListener() {
        @Override
        public void resultBitmapUri(Uri bitmapUri) {
            // TODO Auto-generated method stub
            FoodEditActivity.this.bitmapUri = bitmapUri;
            if (editType == ValueType.EDIT) {
                mPresenter.upFoodImg();
            }
            if (editType == ValueType.ADD) {
                ivImg.setImageBitmap(BitmapUtil.uriToBitmap(bitmapUri));
            }
        }
    };

    @Override
    public void mOnClick(View v) {
        // TODO Auto-generated method stub
        if (v.getId() == btnSubmit.getId()) {
            if (editType == ValueType.ADD) {
                mPresenter.addFoodRequest();
            }
            if (editType == ValueType.EDIT) {
                mPresenter.editFoodRequest();
            }
        }
        if (v.getId() == btnClasses.getId()) {
            Intent intent = new Intent(FoodEditActivity.this, ClassesManageActivity.class);
            intent.putExtra(ValueKey.TITLE, getString(R.string.title_classesmanage_choose));
            intent.putExtra(ValueKey.CLASSESMANAGE_TYPE, ValueType.CHOOSE);
            startActivityForResult(intent, ValueRequest.FoodEdit_Classesmanage);
        }
        if (v.getId() == ivCodeIcon.getId()) {
            Intent intent = new Intent(FoodEditActivity.this, CodeActivity.class);
            intent.putExtra(ValueKey.TITLE, getString(R.string.title_code_foodedit));
            startActivityForResult(intent, ValueRequest.FoodEdit_Code);
        }
        if (v.getId() == ivImg.getId()) {
            getPhoto();
        }
    }

    @Override
    public String getFoodName() {
        // TODO Auto-generated method stub
        return edtName.getText().toString().trim();
    }

    @Override
    public String getFoodCode() {
        // TODO Auto-generated method stub
        return edtCode.getText().toString().trim();
    }

    @Override
    public ClassesEntity getFoodClasses() {
        // TODO Auto-generated method stub
        return classes;
    }

    @Override
    public double getFoodPrice() {
        // TODO Auto-generated method stub
        String priS = edtPrice.getText().toString().trim();
        return StringUtil.getDouble(priS, -1);
    }

    @Override
    public double getFoodPriceMember() {
        // TODO Auto-generated method stub
        String priS = edtPriceMember.getText().toString().trim();
        double priceM = StringUtil.getDouble(priS, -1);
        if (priceM <= 0) {
            priceM = getFoodPrice();
        }
        return priceM;
    }

    @Override
    public String getFoodUnit() {
        // TODO Auto-generated method stub
        return edtUnit.getText().toString().trim();
    }

    @Override
    public double getFoodNumsum() {
        // TODO Auto-generated method stub
        String priS = edtNumsum.getText().toString().trim();
        return StringUtil.getDouble(priS);
    }

    @Override
    public String getFoodFrom() {
        // TODO Auto-generated method stub
        return edtFrom.getText().toString().trim();
    }

    @Override
    public int getFoodWeightType() {
        // TODO Auto-generated method stub
        int typeWeight = ValueFinal.TypeWeight_Default;
        int checkedId = rgroupWeight.getCheckedRadioButtonId();
        if (checkedId == rbtnDefault.getId()) {
            typeWeight = ValueFinal.TypeWeight_Default;
        }
        if (checkedId == rbtnWeidght.getId()) {
            typeWeight = ValueFinal.TypeWeight_Weight;
        }
        UL.e(TAG, "checkedId" + checkedId);
        return typeWeight;
    }

    @Override
    public String getFoodDesc() {
        // TODO Auto-generated method stub
        return edtDesc.getText().toString().trim();
    }

    @Override
    public void editSuccess(FoodEntity foodNew) {
        // TODO Auto-generated method stub
        if (editType == ValueType.ADD) {
            Toast.makeText(context, getString(R.string.foodedit_add_suc), Toast.LENGTH_SHORT).show();
        }

        if (editType == ValueType.EDIT) {
            Toast.makeText(context, getString(R.string.foodedit_edt_suc), Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ValueKey.CLASSESListAll, (Serializable) listClassesAll);
        bundle.putSerializable(ValueKey.FOOD, foodNew);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public int getEditType() {
        // TODO Auto-generated method stub
        return editType;
    }

    private void getPhoto() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("选择照片");
        String[] items = new String[]{"拍照", "相册"};
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (which == 0) {
                    utilPhoto.getPhoto(GetPhotoUtil.PHOTO_REQUEST_CAREMA);
                } else {
                    utilPhoto.getPhoto(GetPhotoUtil.PHOTO_REQUEST_IMAGE);
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public Uri getFoodImgBitmapUri() {
        // TODO Auto-generated method stub
        return bitmapUri;
    }

    @Override
    public void showEditInfo(FoodEntity food) {
        // TODO Auto-generated method stub
        this.foodDetail = food;
        if (foodDetail != null) {
            String name = foodDetail.getName();
            String code = foodDetail.getBarcode();
            double price = foodDetail.getPrice();
            double priceMember = foodDetail.getPriceMember();
            String unit = foodDetail.getUnit();
            String from = foodDetail.getFrom();
            double numsum = foodDetail.getNumsum();
            String classesName = "";
            if (foodDetail.getClasses() != null) {
                classesName = foodEdit.getClasses().getName();
            }
            edtName.setText(name);
            edtPrice.setText(price + "");
            edtPriceMember.setText(priceMember + "");
            edtFrom.setText(from);
            edtUnit.setText(unit);
            edtNumsum.setText(numsum + "");
            edtCode.setText(code + "");
            if (foodDetail.isTypeWeight()) {
                rgroupWeight.check(rbtnWeidght.getId());
            } else {
                rgroupWeight.check(rbtnDefault.getId());
            }
        }
    }


    @Override
    public FoodEntity getFoodEdit() {
        // TODO Auto-generated method stub
        return foodEdit;
    }

    @Override
    public int getFoodEditId() {
        // TODO Auto-generated method stub
        return foodIdEdit;
    }

    @Override
    public void upFoodImgBitmapSuc() {
        // TODO Auto-generated method stub
        if (bitmapUri != null) {
            ivImg.setImageBitmap(BitmapUtil.uriToBitmap(bitmapUri));
        }
        Toast.makeText(context, getString(R.string.foodedit_upimg_suc), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        utilPhoto.onPhotoResult(requestCode, resultCode, data);
        if (requestCode == ValueRequest.FoodEdit_Classesmanage && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            classes = (ClassesEntity) bundle.get(ValueKey.CLASSES);
            listClassesAll = (List<ClassesEntity>) bundle.get(ValueKey.CLASSESListAll);
            if (classes != null) {
                btnClasses.setText(classes.getName());
            }
        }
        if (requestCode == ValueRequest.FoodEdit_Code && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String code = bundle.getString(ValueKey.CODEBAR);
            edtCode.setText(code);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onBack() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mActivity);
        builder.setTitle(getString(R.string.tig));
        builder.setMessage(getString(R.string.is_exit_edit));
        builder.setPositiveButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.setNegativeButton(getString(R.string.cancel),null);
        builder.show();
    }
}
