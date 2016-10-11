package com.mhalong.antoeasy.fragment;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mhalong.antoeasy.R;
import com.mhalong.antoeasy.manager.http.HttpAntoManager;
import com.mhalong.antoeasy.models.AntoChannelItem;
import com.mhalong.antoeasy.models.AntoChannelList;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by passa on 10/10/2559.
 */

public class AddChannelFragment extends DialogFragment {
    @BindView(R.id.btnAdd)
    Button btnAdd;
    @BindView(R.id.btnCancle)
    Button btnCancle;
    @BindView(R.id.EdtName)
    EditText EdtName;
    @BindView(R.id.edtThink)
    EditText edtThink;
    @BindView(R.id.edtChannel)
    EditText edtChannel;
    @BindView(R.id.edtKey)
    EditText edtKey;

    private AddChannelDialogListenner listener;

    public interface AddChannelDialogListenner {
        void onClose(boolean status);
    }

    public AddChannelFragment() {
        // set null or default listener or accept as argument to constructor
        this.listener = null;
    }

    // Assign the listener implementing events interface that will receive the events
    public void setCloseDialogListenner(AddChannelDialogListenner listener) {
        this.listener = listener;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_channel, container);
        ButterKnife.bind(this, view);
        getDialog().setCanceledOnTouchOutside(false);
        btnCancle.setOnClickListener(onClickCancleListener);
        btnAdd.setOnClickListener(onClickOkListener);

        return view;
    }


    View.OnClickListener onClickOkListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Call<ResponseBody> call = HttpAntoManager.getInstance().getService().getAntoChannel(
                    edtKey.getText().toString(),
                    edtThink.getText().toString(),
                    edtChannel.getText().toString()
            );
            final ProgressDialog dialogLoad = ProgressDialog.show(getActivity(), "",
                    "กรุณารอสักครู่...", true);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    dialogLoad.dismiss();
                    try {
                        String string = response.body().string();


                        JSONObject jsonObject = new JSONObject(string);
                        if (jsonObject.get("result").toString().equals("true")) {
                            AntoChannelItem item = new AntoChannelItem();
                            item.setName(EdtName.getText().toString());
                            item.setThink(edtThink.getText().toString());
                            item.setChannel(edtChannel.getText().toString());
                            item.setKey(edtKey.getText().toString());
                            item.setValue(Integer.parseInt(jsonObject.get("value").toString()));
                            AntoChannelList.getInstance().getChannelList().add(item);
                            Toast.makeText(getActivity(), "เพิ่ม Channel เสร็จแล้ว", Toast.LENGTH_SHORT).show();
                            AntoChannelList.getInstance().saveCache();
                            if (listener != null)
                                listener.onClose(true);
                            getDialog().dismiss();
                        } else {
                            Toast.makeText(getActivity(), "ข้อมูลไม่ถูกต้อง กรุณาแก้ไข", Toast.LENGTH_SHORT).show();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    dialogLoad.dismiss();
                    Toast.makeText(getActivity(), "ไม่สามารถเชื่อมต่อเครื่อข่ายได้", Toast.LENGTH_SHORT).show();
                }
            });


        }
    };


    View.OnClickListener onClickCancleListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (listener != null)
                listener.onClose(false);
            getDialog().dismiss();
        }
    };
}
