package com.example.primero.Worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.primero.Repos.TurfRepo;

public class TurfWorker extends Worker {

    private static final String TAG = "TurfWorker";

    TurfRepo turfRepo;

    public TurfWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        turfRepo = new TurfRepo();
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "doWork: FRESH WORK GETTING TURF turf worker init");
        turfRepo.getOnlineTurfList();
        return Result.success();
    }
}
