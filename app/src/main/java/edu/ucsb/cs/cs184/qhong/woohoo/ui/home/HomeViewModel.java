package edu.ucsb.cs.cs184.qhong.woohoo.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<String> buttonCreateText;
    private MutableLiveData<String> buttonFindText;


    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Good Quiz");
        buttonCreateText = new MutableLiveData<>();
        buttonCreateText.setValue("Create Room");
        buttonFindText = new MutableLiveData<>();
        buttonFindText.setValue("Find Room");
    }

    public LiveData<String> getmText() {
        return mText;
    }
    public LiveData<String> getButtonCreateText() {
        return buttonCreateText;
    }
    public LiveData<String> getButtonFindText() {
        return buttonFindText;
    }
}