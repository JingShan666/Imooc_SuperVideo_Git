package com.hejunlin.imooc_supervideo.indircator;

import java.util.List;

/**
 * Created by hejunlin on 17/4/8.
 */

public interface IPagerIndicatorView extends IPagerChangeListener {

    void setPostionDataList(List<PositionData> list);
}
