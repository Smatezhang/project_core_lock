package com.itek.library_core.base;

import android.app.Activity;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.itek.library_common.util.ToastTool;
import com.itek.library_core.util.AutoClearedValue;
import com.itek.library_core.widget.ZProgressHUD;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


/**
 * Author:：simon
 * Date：2019-12-12:18:06
 * Mail：simon@itekiot.com
 * Copyright (c) 2019 itek. All rights reserved.
 * Description：
 */

public abstract class BaseFragment<VM extends BaseViewModel, VB extends ViewDataBinding> extends BaseInjectFragment implements IBaseView{

    protected AutoClearedValue<VB> mBinding;

    protected VM mViewModel;

    private LinearLayout rootView;
    private int viewModelId;

    public BaseFragment() {

    }

    protected abstract int getLayoutId();

    protected abstract VM getViewModel();

    protected abstract void initView(View var1);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParam();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        VB dataBinding = DataBindingUtil.inflate(inflater, this.getLayoutId(), container, false);
        this.mBinding = new AutoClearedValue(this, dataBinding);
        return (this.mBinding.get()).getRoot();
    }



//    @Nullable
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        this.rootView = (LinearLayout) inflater.inflate(R.layout.fragment_base, (ViewGroup) null);
//        VB dataBinding = DataBindingUtil.inflate(inflater, this.getLayoutId(), container, false);
//        this.mBinding = new AutoClearedValue(this, dataBinding);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, -1);
//        (this.mBinding.get()).getRoot().setLayoutParams(params);
//        LinearLayout rlContainer = this.rootView.findViewById(R.id.container);
//        rlContainer.addView((this.mBinding.get()).getRoot());
//        this.initView(this.rootView);
//        return this.rootView;
//    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //私有的初始化Databinding和ViewModel方法
        initViewDataBinding();
        //私有的ViewModel与View的契约事件回调逻辑
        registorUIChangeLiveDataCallBack();
        //页面数据初始化方法
        initData();
        //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
        initViewObservable();
        //注册RxBus
        mViewModel.registerRxBus();
    }

    /**
     * 注入绑定
     */
    private void initViewDataBinding() {
        viewModelId = initVariableId();
        mViewModel = this.getViewModel();
        if (mViewModel == null) {
            Class modelClass;
            Type type = getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                modelClass = (Class) ((ParameterizedType) type).getActualTypeArguments()[1];
            } else {
                //如果没有指定泛型参数，则默认使用BaseViewModel
                modelClass = BaseViewModel.class;
            }
            mViewModel = (VM) createViewModel(this, modelClass);
        }
        mBinding.get().setVariable(viewModelId, mViewModel);
        //让ViewModel拥有View的生命周期感应
        getLifecycle().addObserver(mViewModel);
        //注入RxLifecycle生命周期
        mViewModel.injectLifecycleProvider(this);
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        //解除ViewModel生命周期感应
        getLifecycle().removeObserver(mViewModel);
        if (mViewModel != null) {
            mViewModel.removeRxBus();
        }

    }
    /**
     * 初始化ViewModel的id
     *
     * @return BR的id
     */
    public abstract int initVariableId();


    protected void showToast(String content) {
        ToastTool.show(this.getActivity(), content);
    }

    protected BaseActivity getHoldingActivity() {
        return (BaseActivity) this.getActivity();
    }

    protected LinearLayout getRootView() {
        return this.rootView;
    }

    /**
     * =====================================================================
     **/
    //注册ViewModel与View的契约UI回调事件
    protected void registorUIChangeLiveDataCallBack() {

        //加载对话框显示
        mViewModel.getUC().getShowDialogEvent().observe(this, title -> showDialog(title));
        //加载对话框消失
        mViewModel.getUC().getDismissDialogEvent().observe(this, v -> dismissDialog());
        //跳入新页面
        mViewModel.getUC().getStartActivityEvent().observe(this, params -> {
            Class<?> clz = (Class<?>) params.get(BaseViewModel.ParameterField.CLASS);
            Bundle bundle = (Bundle) params.get(BaseViewModel.ParameterField.BUNDLE);
            startActivity(clz, bundle);
        });
        //跳入ContainerActivity
        mViewModel.getUC().getStartContainerActivityEvent().observe(this, params -> {
            String canonicalName = (String) params.get(BaseViewModel.ParameterField.CANONICAL_NAME);
            Bundle bundle = (Bundle) params.get(BaseViewModel.ParameterField.BUNDLE);
            startContainerActivity(canonicalName, bundle);
        });

        //关闭界面
        mViewModel.getUC().getFinishEvent().observe(this, v -> getActivity().finish());
        //关闭上一层
        mViewModel.getUC().getOnBackPressedEvent().observe(this, v -> getActivity().onBackPressed());

        mViewModel.getUC().getShowErrorMessageEvent().observe(this, msg -> showErrorMessage(msg));
    }

    private void showErrorMessage(String msg) {
        showToast(msg);
    }

    public void showDialog(String title) {

        ZProgressHUD.getInstance(getActivity()).setMessage(title);
        ZProgressHUD.getInstance(getActivity()).setCanceledOnTouchOutside(true);
        ZProgressHUD.getInstance(getActivity()).setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);
        ZProgressHUD.getInstance(getActivity()).show();

    }

    public void dismissDialog() {
        ZProgressHUD.getInstance(getActivity()).dismiss();
    }

    /**
     * 跳转页面
     *
     * @param clz 所跳转的目的Activity类
     */
    public void startActivity(Class<?> clz) {
        startActivity(new Intent(getContext(), clz));
    }

    /**
     * 跳转页面
     *
     * @param clz    所跳转的目的Activity类
     * @param bundle 跳转所携带的信息
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(getContext(), clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }


    /**
     * 跳转容器页面
     *
     * @param canonicalName 规范名 : Fragment.class.getCanonicalName()
     */
    public void startContainerActivity(String canonicalName) {
        startContainerActivity(canonicalName, null);
    }


    /**
     * 跳转容器页面
     *
     * @param canonicalName 规范名 : Fragment.class.getCanonicalName()
     * @param bundle        跳转所携带的信息
     */
    public void startContainerActivity(String canonicalName, Bundle bundle) {
        Intent intent = new Intent(getContext(), ContainerActivity.class);
        intent.putExtra(ContainerActivity.FRAGMENT, canonicalName);
        if (bundle != null) {
            intent.putExtra(ContainerActivity.BUNDLE, bundle);
        }
        startActivity(intent);
    }


    @Override
    public void initParam() {

    }


    @Override
    public void initData() {

    }


    @Override
    public void initViewObservable() {

    }

    public boolean isBackPressed() {
        return false;
    }


    /**
     * 创建ViewModel
     *
     * @param cls
     * @param <T>
     * @return
     */
    public <T extends ViewModel> T createViewModel(Fragment fragment, Class<T> cls) {
        return ViewModelProviders.of(fragment).get(cls);
    }


}

