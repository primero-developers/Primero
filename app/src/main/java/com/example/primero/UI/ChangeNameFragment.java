package com.example.primero.UI;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;

import com.example.primero.R;
import com.example.primero.Repos.UserRepo;
import com.example.primero.SessionManager;
import com.example.primero.Utils.DialogClick;
import com.example.primero.Utils.SoftInputDealer;
import com.google.android.material.textfield.TextInputEditText;

public class ChangeNameFragment extends DialogFragment {

    private static final String TAG = "ChangeNameFragment";

    SessionManager sessionManager;

    DialogClick clickInterface;
    TextInputEditText nameInp;
    Button submitBtn;
    RelativeLayout loaderParent;
    LinearLayout dialogContent;

    public ChangeNameFragment(Context clickInterface) {
        this.clickInterface = (DialogClick) clickInterface;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = SessionManager.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_change_name, container, false);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dialogContent = view.findViewById(R.id.change_dialog_contents);
        loaderParent = view.findViewById(R.id.change_loader_parent);

        nameInp = view.findViewById(R.id.change_name_inp);
        submitBtn = view.findViewById(R.id.change_name_btn);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nameInp.getText() != null  && nameInp.getText().length() > 0){
                    String name = nameInp.getText().toString();
                    clickInterface.dialogClick("Name",name);
                    SoftInputDealer.forceHide(getActivity(),nameInp);
                    dialogContent.setVisibility(View.GONE);
                    loaderParent.setVisibility(View.VISIBLE);

                    sessionManager.getGLOBAL_TRIGGER().observe(getActivity(), new Observer<String>() {
                        @Override
                        public void onChanged(String s) {
                            if (s.equals("NAME CHANGED")){
                                dismiss();
                            }
                        }
                    });

                } else {
                    nameInp.setError("Name field is empty");
                }

            }
        });
    }
}
