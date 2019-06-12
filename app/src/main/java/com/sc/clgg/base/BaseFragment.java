package com.sc.clgg.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sc.clgg.tool.helper.LogHelper;

/**
 * @author：lvke
 * @date：2019/5/31 15:01
 */
public class BaseFragment extends Fragment {

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        LogHelper.e(getClass().getSimpleName() + "  ------  onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogHelper.e(getClass().getSimpleName() + "  ------  onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogHelper.e(getClass().getSimpleName() + "  ------  onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LogHelper.e(getClass().getSimpleName() + "  ------  onViewCreated");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogHelper.e(getClass().getSimpleName() + "  ------  onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        LogHelper.e(getClass().getSimpleName() + "  ------  onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        LogHelper.e(getClass().getSimpleName() + "  ------  onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogHelper.e(getClass().getSimpleName() + "  ------  onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogHelper.e(getClass().getSimpleName() + "  ------  onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogHelper.e(getClass().getSimpleName() + "  ------  onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogHelper.e(getClass().getSimpleName() + "  ------  onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogHelper.e(getClass().getSimpleName() + "  ------  onDetach");
    }
}
