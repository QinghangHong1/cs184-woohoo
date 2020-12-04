package edu.ucsb.cs.cs184.qhong.woohoo.ui.upload;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import edu.ucsb.cs.cs184.qhong.woohoo.utils.ProblemSet;

public class UploadViewModel extends ViewModel {

    private MutableLiveData<ProblemSet> mProblemSet;

    public UploadViewModel() {
        mProblemSet = new MutableLiveData<>();
        mProblemSet.setValue(new ProblemSet());
    }

    public LiveData<ProblemSet> getProblemSet() {
        return mProblemSet;
    }
    public void clearProblemSet(){
        mProblemSet.setValue(new ProblemSet());
    }

}