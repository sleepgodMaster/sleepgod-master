package com.sleepgod.sleepgod.view;

import com.sleepgod.net.base.view.BaseView;
import com.sleepgod.net.http.Progress;

public interface MainView extends BaseView{
    void onProgress(Progress progress);
    void getDataSuccess(String data);
    void getDataFail(String msg);
}
