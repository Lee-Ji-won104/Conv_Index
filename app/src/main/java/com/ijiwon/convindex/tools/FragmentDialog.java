package com.ijiwon.convindex.tools;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.ijiwon.convindex.CameraActivity;
import com.ijiwon.convindex.R;


public class FragmentDialog extends DialogFragment implements View.OnClickListener {

    private static final String TAG = "CustomDialogFragment";
    private static final String DIALOG_MAIN = "dialog_main";

    private String mMainMsg;



    public static FragmentDialog getInstance() {
        FragmentDialog dialog = new FragmentDialog();
        return dialog;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMainMsg = getArguments().getString(DIALOG_MAIN);
        }
    }


    @Override
    public void onResume() {    //fragmentdialog 는 크기조정 안되기때문에 dimens에 저장한값 불러옴
        super.onResume();

        int dialogWidth = getResources().getDimensionPixelSize(R.dimen.fragment_dialog_width);
        int dialogHeight = getResources().getDimensionPixelSize(R.dimen.fragment_dialog_height);
        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_dialog, null);

        TextView dialog_msg = view.findViewById(R.id.dialog_msg);
        ImageView dialog_image = view.findViewById(R.id.dialog_image);


        //dialog_image.setImageBitmap(CameraActivity.dialog_bitmap);
        //dialog_msg.setText(CameraActivity.dialog_result.toString());

        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

/*
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_custom_dialog, container, false);
        ((TextView)view.findViewById(R.id.dialog_confirm_msg)).setText(mMainMsg);
        view.findViewById(R.id.dialog_confirm_btn).setOnClickListener(this);
        Dialog dialog = getDialog();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        return view;
    }
*/

    private void dismissDialog() {
        this.dismiss();
    }

    @Override
    public void onClick(View v) {

        /*
        switch (v.getId()) {
            case R.id.dialog_confirm_btn:
                dismissDialog();
                break;
        }

         */
    }
}