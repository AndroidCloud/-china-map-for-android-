package com.aiceking.chinamapview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.aiceking.chinamap.model.ChinaMapModel;
import com.aiceking.chinamapview.util.ColorChangeUtil;
import com.aiceking.chinamap.view.ColorView;
import com.aiceking.chinamap.util.SvgUtil;
import com.aiceking.chinamap.view.ChinaMapView;
import com.aiceking.chinamap.model.MycolorArea;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ChinaMapView mapview;
    private provinceAdapter adapter;
    private ChinaMapModel myMap;
    private Button changeType;
    private ColorView colorView;
    private int currentColor = 0;
    private ListView province_listview;
    private HashMap<String, List<MycolorArea>> colorView_hashmap;
    private List<String> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        //设置颜色渐变条
        setColorView();
        //初始化map
        initMap();
//        //初始化map各省份颜色
//        ColorChangeUtil.changeMapColors(myMap, ColorChangeUtil.nameStrings[0]);
//        mapview.chingeMapColors();
//        //listview
//        setListAdapter();
        changeType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String namestring = ColorChangeUtil.nameStrings[++currentColor % ColorChangeUtil.nameStrings.length];
                changeType.setText(namestring);
                colorView.setList(colorView_hashmap.get(namestring));
                //重置map各省份颜色
                ColorChangeUtil.changeMapColors(myMap, namestring);
                mapview.chingeMapColors();
            }
        });
        mapview.setOnChoseProvince(new ChinaMapView.onProvinceClickLisener() {
            @Override
            public void onChose(String provincename) {
                Toast.makeText(MainActivity.this, provincename, Toast.LENGTH_SHORT).show();
                //地图点击省份回调接口，listview滚动到相应省份位置
//                for (int i = 0; i < list.size(); i++) {
//                    if (list.get(i).contains(provincename)) {
//                        adapter.setPosition(i);
//                        province_listview.setSelection(i);
//                        break;
//                    }
//                }
            }
        });
    }
    private void setListAdapter() {
        list=new ArrayList<>();
        //最后三个是香港，澳门和台湾，不需要
        for (int i = 0; i< ColorChangeUtil.province_datas.length-3; i++){
            list.add(ColorChangeUtil.province_datas[i]);
        }
        adapter = new provinceAdapter(this, list);
        province_listview.setAdapter(adapter);
    }

    private void initView() {
        province_listview = (ListView) findViewById(R.id.province_listview);
        mapview = (ChinaMapView) findViewById(R.id.view);
        colorView = (ColorView) findViewById(R.id.colorView);
        changeType = (Button) findViewById(R.id.changeType);
        changeType.setFocusable(true);
        changeType.setFocusableInTouchMode(true);
        changeType.setText(ColorChangeUtil.nameStrings[0]);
    }
    /**
     * 设置颜色渐变条
     */
    private void setColorView() {
        colorView_hashmap = new HashMap<>();
        for (int i = 0; i < ColorChangeUtil.nameStrings.length; i++) {
            String colors[] = ColorChangeUtil.colorStrings[i].split(",");
            String texts[] = ColorChangeUtil.textStrings[i].split(",");
            List<MycolorArea> list = new ArrayList<>();
            for (int j = 0; j < colors.length; j++) {
                MycolorArea c = new MycolorArea();
                c.setColor(Color.parseColor(colors[j]));
                c.setText(texts[j]);
                list.add(c);
            }
            colorView_hashmap.put(ColorChangeUtil.nameStrings[i], list);
        }
        colorView.setList(colorView_hashmap.get(ColorChangeUtil.nameStrings[0]));
    }

    private void initMap() {
        //拿到SVG文件，解析成对象
        myMap = new SvgUtil(this).getProvinces();
        //传数据
        mapview.setMap(myMap);
    }
}
