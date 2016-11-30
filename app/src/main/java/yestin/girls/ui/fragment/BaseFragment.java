package yestin.girls.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.View;

import com.trello.rxlifecycle.components.support.RxFragment;

import butterknife.ButterKnife;
import yestin.girls.utils.LogUtil;


/**
 * Created by yinlu on 2016/4/21.
 */
public class BaseFragment extends RxFragment {


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LogUtil.i(this.getClass().getSimpleName() + "-----onHiddenChanged");
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        LogUtil.i(this.getClass().getSimpleName() + "-----startActivityForResult");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.i(this.getClass().getSimpleName() + "-----onActivityResult");
    }

    @Override
    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);
        LogUtil.i(this.getClass().getSimpleName() + "-----onInflate");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogUtil.i(this.getClass().getSimpleName() + "-----onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.i(this.getClass().getSimpleName() + "-----onCreate");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtil.i(this.getClass().getSimpleName() + "-----onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtil.i(this.getClass().getSimpleName() + "-----onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.i(this.getClass().getSimpleName() + "-----onResume");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        LogUtil.i(this.getClass().getSimpleName() + "-----onSaveInstanceState");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtil.i(this.getClass().getSimpleName() + "-----onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtil.i(this.getClass().getSimpleName() + "-----onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.i(this.getClass().getSimpleName() + "-----onDestroy");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtil.i(this.getClass().getSimpleName() + "-----onDestroyView");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtil.i(this.getClass().getSimpleName() + "-----onDetach");
    }


}
