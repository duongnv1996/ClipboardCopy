package com.duongkk.clipboardcopy.fragments;

import android.support.v4.app.Fragment;

import com.duongkk.clipboardcopy.interfaces.UpdateFragment;
import com.duongkk.clipboardcopy.models.Message;

import java.util.List;

/**
 * Created by MyPC on 8/25/2016.
 */
public class BaseFragment extends Fragment implements UpdateFragment {


    @Override
    public void update(List<Message> msgs) {

    }
}
