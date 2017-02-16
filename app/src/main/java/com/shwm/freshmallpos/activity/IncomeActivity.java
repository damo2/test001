package com.shwm.freshmallpos.activity;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.been.IncomeEntity;
import com.shwm.freshmallpos.been.SaleEntity;
import com.shwm.freshmallpos.presenter.MIncomePresenter;
import com.shwm.freshmallpos.util.StringUtil;
import com.shwm.freshmallpos.util.UL;
import com.shwm.freshmallpos.value.ValueFinal;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.view.IIncomeView;
import com.shwm.freshmallpos.base.BaseActivity;

/**
 * 收入
 * 
 * @author wr 2017-1-6
 */
public class IncomeActivity extends BaseActivity<IIncomeView, MIncomePresenter> implements IIncomeView {
	private static final int DEFAULT_DATA = 0;
	private static final int SUBCOLUMNS_DATA = 1;
	private static final int STACKED_DATA = 2;
	private static final int NEGATIVE_SUBCOLUMNS_DATA = 3;
	private static final int NEGATIVE_STACKED_DATA = 4;

	private ColumnChartView chart;
	private ColumnChartData data;
	private boolean hasAxes = true;// 有轴
	private boolean hasAxesX = true;// 有轴
	private boolean hasAxesY = false;// 有轴
	private boolean hasAxesNames = true;// 有轴名称
	private boolean hasLabels = true;// 有标签
	private boolean hasLabelForSelected = false;// 有选中标签
	private int dataType = DEFAULT_DATA;
	//
	private boolean isZoomEnabled = false;// 设置是否支持缩放
	private String AxisXName = "时间（日）";
	private String AxisYName = "";
	private List<PointValue> mPointValues = new ArrayList<PointValue>();
	private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();
	//
	private IncomeEntity income;
	private View viewToday, viewWeek, viewMonth;
	private TextView tvToday, tvWeek, tvMonth;

	private String title;

	@Override
	public int bindLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_pos_income;
	}

	@Override
	public MIncomePresenter initPresenter() {
		// TODO Auto-generated method stub
		return new MIncomePresenter(this);
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		super.init();
		title = getIntent().getExtras().getString(ValueKey.TITLE);
	}
	@Override
	protected void initToolbar() {
		// TODO Auto-generated method stub
		super.initToolbar();
		setToolbar(R.id.toolbar_income_top,title);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		super.initView();
		chart = (ColumnChartView) findViewById(R.id.chart_income);
		chart.setZoomEnabled(isZoomEnabled);

		viewToday = findViewById(R.id.rl_income_today);
		viewWeek = findViewById(R.id.rl_income_week);
		viewMonth = findViewById(R.id.rl_income_month);

		tvToday = (TextView) findViewById(R.id.tv_income_today);
		tvWeek = (TextView) findViewById(R.id.tv_income_week);
		tvMonth = (TextView) findViewById(R.id.tv_income_month);
	}

	@Override
	protected void setValue() {
		// TODO Auto-generated method stub
		super.setValue();
		generateData();
		mPresenter.getIncome();

	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		super.setListener();
		chart.setOnValueTouchListener(new ValueTouchListener());

		viewToday.setOnClickListener(this);
		viewWeek.setOnClickListener(this);
		viewMonth.setOnClickListener(this);
	}

	@Override
	public void mOnClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == viewToday.getId()) {
			Intent intent0 = new Intent(mActivity, IncomeOrderActivity.class);
			intent0.putExtra(ValueKey.TITLE, getString(R.string.income_today));
			intent0.putExtra(ValueKey.DayNearly, ValueFinal.DayNearly_Today);
			startActivity(intent0);
		}

		if (v.getId() == viewWeek.getId()) {
			Intent intent0 = new Intent(mActivity, IncomeOrderActivity.class);
			intent0.putExtra(ValueKey.TITLE, getString(R.string.income_week));
			intent0.putExtra(ValueKey.DayNearly, ValueFinal.DayNearly_Week);
			startActivity(intent0);
		}

		if (v.getId() == viewMonth.getId()) {
			Intent intent0 = new Intent(mActivity, IncomeOrderActivity.class);
			intent0.putExtra(ValueKey.TITLE, getString(R.string.income_month));
			intent0.putExtra(ValueKey.DayNearly, ValueFinal.DayNearly_Month);
			startActivity(intent0);
		}
	}

	private void generateData() {

		switch (dataType) {
		case DEFAULT_DATA:
			generateDefaultData();
			break;
		case SUBCOLUMNS_DATA:
			generateSubcolumnsData();
			break;
		case STACKED_DATA:
			generateStackedData();
			break;
		case NEGATIVE_SUBCOLUMNS_DATA:
			generateNegativeSubcolumnsData();
			break;
		case NEGATIVE_STACKED_DATA:
			generateNegativeStackedData();
			break;
		default:
			generateDefaultData();
			break;
		}
	}

	private void generateDefaultData() {
		if (income == null || income.getListSale() == null)
			return;
		List<SaleEntity> listSale = income.getListSale();
		int numColumns = listSale.size();
		List<Column> columns = new ArrayList<Column>();
		List<SubcolumnValue> values;
		for (int i = 0; i < numColumns; i++) {
			values = new ArrayList<SubcolumnValue>();
			SaleEntity sale = listSale.get(i);
			mAxisXValues.add(new AxisValue(i).setLabel(sale.getDayTag()));
			values.add(new SubcolumnValue(StringUtil.getFloat(sale.getMoney()), ChartUtils.pickColor()));
			Column column = new Column(values);
			column.setHasLabels(hasLabels);
			column.setHasLabelsOnlyForSelected(hasLabelForSelected);
			columns.add(column);

		}
		data = new ColumnChartData(columns);
		if (hasAxesX) {
			Axis axisX = new Axis(mAxisXValues);
			if (hasAxesNames) {
				axisX.setName(AxisXName);
			}
			data.setAxisXBottom(axisX);
		} else {
			data.setAxisXBottom(null);
		}

		if (hasAxesY) {
			Axis axisY = new Axis().setHasLines(true);
			if (hasAxesNames) {
				axisY.setName(AxisYName);
			}
			data.setAxisYLeft(axisY);
		} else {
			data.setAxisYLeft(null);
		}
		chart.setColumnChartData(data);
	}

	/**
	 * Generates columns with subcolumns, columns have larger separation than subcolumns.
	 */
	private void generateSubcolumnsData() {
		int numSubcolumns = 4;
		int numColumns = 4;
		// Column can have many subcolumns, here I use 4 subcolumn in each of 8 columns.
		List<Column> columns = new ArrayList<Column>();
		List<SubcolumnValue> values;
		for (int i = 0; i < numColumns; ++i) {
			values = new ArrayList<SubcolumnValue>();
			for (int j = 0; j < numSubcolumns; ++j) {
				values.add(new SubcolumnValue((float) Math.random() * 50f + 5, ChartUtils.pickColor()));
			}
			Column column = new Column(values);
			column.setHasLabels(hasLabels);
			column.setHasLabelsOnlyForSelected(hasLabelForSelected);
			columns.add(column);
		}

		data = new ColumnChartData(columns);

		if (hasAxes) {
			Axis axisX = new Axis();
			Axis axisY = new Axis().setHasLines(true);
			if (hasAxesNames) {
				axisX.setName("Axis X");
				axisY.setName("Axis Y");
			}
			data.setAxisXBottom(axisX);
			data.setAxisYLeft(axisY);
		} else {
			data.setAxisXBottom(null);
			data.setAxisYLeft(null);
		}
		chart.setColumnChartData(data);
	}

	/**
	 * Generates columns with stacked subcolumns.
	 */
	private void generateStackedData() {
		int numSubcolumns = 4;
		int numColumns = 8;
		// Column can have many stacked subcolumns, here I use 4 stacke subcolumn in each of 4 columns.
		List<Column> columns = new ArrayList<Column>();
		List<SubcolumnValue> values;
		for (int i = 0; i < numColumns; ++i) {

			values = new ArrayList<SubcolumnValue>();
			for (int j = 0; j < numSubcolumns; ++j) {
				values.add(new SubcolumnValue((float) Math.random() * 20f + 5, ChartUtils.pickColor()));
			}

			Column column = new Column(values);
			column.setHasLabels(hasLabels);
			column.setHasLabelsOnlyForSelected(hasLabelForSelected);
			columns.add(column);
		}

		data = new ColumnChartData(columns);

		// Set stacked flag.
		data.setStacked(true);

		if (hasAxes) {
			Axis axisX = new Axis();
			Axis axisY = new Axis().setHasLines(true);
			if (hasAxesNames) {
				axisX.setName("Axis X");
				axisY.setName("Axis Y");
			}
			data.setAxisXBottom(axisX);
			data.setAxisYLeft(axisY);
		} else {
			data.setAxisXBottom(null);
			data.setAxisYLeft(null);
		}

		chart.setColumnChartData(data);
	}

	private void generateNegativeSubcolumnsData() {

		int numSubcolumns = 4;
		int numColumns = 4;
		List<Column> columns = new ArrayList<Column>();
		List<SubcolumnValue> values;
		for (int i = 0; i < numColumns; ++i) {
			values = new ArrayList<SubcolumnValue>();
			for (int j = 0; j < numSubcolumns; ++j) {
				int sign = getSign();
				values.add(new SubcolumnValue((float) Math.random() * 50f * sign + 5 * sign, ChartUtils.pickColor()));
			}
			Column column = new Column(values);
			column.setHasLabels(hasLabels);
			column.setHasLabelsOnlyForSelected(hasLabelForSelected);
			columns.add(column);
		}

		data = new ColumnChartData(columns);

		if (hasAxes) {
			Axis axisX = new Axis();
			Axis axisY = new Axis().setHasLines(true);
			if (hasAxesNames) {
				axisX.setName("Axis X");
				axisY.setName("Axis Y");
			}
			data.setAxisXBottom(axisX);
			data.setAxisYLeft(axisY);
		} else {
			data.setAxisXBottom(null);
			data.setAxisYLeft(null);
		}

		chart.setColumnChartData(data);
	}

	private void generateNegativeStackedData() {
		int numSubcolumns = 4;
		int numColumns = 8;
		// Column can have many stacked subcolumns, here I use 4 stacke subcolumn in each of 4 columns.
		List<Column> columns = new ArrayList<Column>();
		List<SubcolumnValue> values;
		for (int i = 0; i < numColumns; ++i) {
			values = new ArrayList<SubcolumnValue>();
			for (int j = 0; j < numSubcolumns; ++j) {
				int sign = getSign();
				values.add(new SubcolumnValue((float) Math.random() * 20f * sign + 5 * sign, ChartUtils.pickColor()));
			}
			Column column = new Column(values);
			column.setHasLabels(hasLabels);
			column.setHasLabelsOnlyForSelected(hasLabelForSelected);
			columns.add(column);
		}

		data = new ColumnChartData(columns);

		// Set stacked flag.
		data.setStacked(true);

		if (hasAxes) {
			Axis axisX = new Axis();
			Axis axisY = new Axis().setHasLines(true);
			if (hasAxesNames) {
				axisX.setName("Axis X");
				axisY.setName("Axis Y");
			}
			data.setAxisXBottom(axisX);
			data.setAxisYLeft(axisY);
		} else {
			data.setAxisXBottom(null);
			data.setAxisYLeft(null);
		}

		chart.setColumnChartData(data);
	}

	private int getSign() {
		int[] sign = new int[] { -1, 1 };
		return sign[Math.round((float) Math.random())];
	}

	// 切换标签
	private void toggleLabels() {
		hasLabels = !hasLabels;
		if (hasLabels) {
			hasLabelForSelected = false;
			chart.setValueSelectionEnabled(hasLabelForSelected);
		}
		generateData();
	}

	// 切换所选项的标签
	private void toggleLabelForSelected() {
		hasLabelForSelected = !hasLabelForSelected;
		chart.setValueSelectionEnabled(hasLabelForSelected);
		if (hasLabelForSelected) {
			hasLabels = false;
		}
		generateData();
	}

	private class ValueTouchListener implements ColumnChartOnValueSelectListener {
		@Override
		public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
			// Toast.makeText(getApplicationContext(), "Selected: " + value, Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onValueDeselected() {
			// TODO Auto-generated method stub
		}
	}

	@Override
	public void showIncome(IncomeEntity income) {
		// TODO Auto-generated method stub
		this.income = income;
		if (income != null) {
			tvToday.setText(StringUtil.getString(income.getToday()));
			tvWeek.setText(StringUtil.getString(income.getWeek()));
			tvMonth.setText(StringUtil.getString(income.getMonth()));
			generateData();
		}
	}

}
