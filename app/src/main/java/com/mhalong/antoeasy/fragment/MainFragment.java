package com.mhalong.antoeasy.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mhalong.antoeasy.R;
import com.mhalong.antoeasy.adapter.ChannelListAdapter;
import com.mhalong.antoeasy.manager.http.HttpAntoManager;
import com.mhalong.antoeasy.models.AntoChannelItem;
import com.mhalong.antoeasy.models.AntoChannelList;
import com.mhalong.antoeasy.util.ItemClickSupport;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainFragment extends Fragment {
    @BindView(R.id.btnAddControl)
    FloatingActionButton btnAddControl;
    @BindView(R.id.listView)
    RecyclerView listView;

    ChannelListAdapter channelAdapter;

    public MainFragment() {
        super();
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);

        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        listView.setLayoutManager(mLayoutManager);

        channelAdapter = new ChannelListAdapter();
        listView.setAdapter(channelAdapter);

        btnAddControl.setOnClickListener(btnAddChannel);

        ItemClickSupport.addTo(listView).setOnItemClickListener(
                OnListviewItemClicklistener
        );


        ItemClickSupport.addTo(listView).setOnItemLongClickListener(OnListviewItemLongClicklistener);

        channelAdapter.setSwitchChangeListenner(new ChannelListAdapter.AddSwitchChangeListenner() {
            @Override
            public void onChange(boolean swChecked, int position) {
                int value = 0;
                if (swChecked) {
                    value = 1;
                }
                AntoChannelList.getInstance().getChannelList().get(position).setValue(value);
                channelAdapter.notifyDataSetChanged();

                sendAnto(position);
            }
        });


        return view;
    }

    View.OnClickListener btnAddChannel = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AddChannelFragment addControlDialog = new AddChannelFragment();
            addControlDialog.show(getActivity().getFragmentManager(), null);
            addControlDialog.setCloseDialogListenner(new AddChannelFragment.AddChannelDialogListenner() {
                @Override
                public void onClose(boolean status) {
                    if (status) {
                        channelAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
    };

    private void sendAnto(int position) {
        AntoChannelList.getInstance().saveCache();
        Call<ResponseBody> call = HttpAntoManager.getInstance().getService().setAntoChannel(
                AntoChannelList.getInstance().getChannelList().get(position).getKey(),
                AntoChannelList.getInstance().getChannelList().get(position).getThink(),
                AntoChannelList.getInstance().getChannelList().get(position).getChannel(),
                AntoChannelList.getInstance().getChannelList().get(position).getValue()
        );
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String string = response.body().string();
                    JSONObject jsonObject = new JSONObject(string);
                    if (!jsonObject.get("result").toString().equals("true")) {
                        Toast.makeText(getActivity(), "พบข้อผิดพลาด Key หรือข้อมูลไม่ถูกต้อง", Toast.LENGTH_SHORT).show();
                                    /*    if (AntoChannelList.getInstance().getChannelList().get(position).getValue() == 1) {
                                            AntoChannelList.getInstance().getChannelList().get(position).setValue(0);
                                        } else {
                                            AntoChannelList.getInstance().getChannelList().get(position).setValue(1);
                                        }
                                        channelAdapter.notifyDataSetChanged();*/
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                             /*   if (AntoChannelList.getInstance().getChannelList().get(position).getValue() == 1) {
                                    AntoChannelList.getInstance().getChannelList().get(position).setValue(0);
                                } else {
                                    AntoChannelList.getInstance().getChannelList().get(position).setValue(1);
                                }
                                channelAdapter.notifyDataSetChanged();*/
                Toast.makeText(getActivity(), "ไม่สามารถเชื่อมต่อเครื่อข่ายได้", Toast.LENGTH_SHORT).show();
            }
        });

        channelAdapter.notifyDataSetChanged();
    }

    ItemClickSupport.OnItemClickListener OnListviewItemClicklistener = new ItemClickSupport.OnItemClickListener() {
        @Override
        public void onItemClicked(RecyclerView recyclerView, int position, View v) {
            if (AntoChannelList.getInstance().getChannelList().get(position).getValue() == 1) {
                AntoChannelList.getInstance().getChannelList().get(position).setValue(0);
            } else {
                AntoChannelList.getInstance().getChannelList().get(position).setValue(1);
            }
            channelAdapter.notifyDataSetChanged();
            sendAnto(position);
        }

    };

    ItemClickSupport.OnItemLongClickListener OnListviewItemLongClicklistener = new ItemClickSupport.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClicked(RecyclerView recyclerView, final int position, View v) {
            new AlertDialog.Builder(getActivity())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("ยืนยัน")
                    .setMessage("คุณต้องการลบ " + AntoChannelList.getInstance().getChannelList().get(position).getName() + " ใช่หรือไม่")
                    .setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AntoChannelList.getInstance().getChannelList().remove(position);
                            AntoChannelList.getInstance().saveCache();
                            channelAdapter.notifyDataSetChanged();
                        }

                    })
                    .setNegativeButton("ยกเลิก", null)
                    .show();
            return true;
        }
    };

    @SuppressWarnings("UnusedParameters")
    private void init(Bundle savedInstanceState) {
        // Init Fragment level's variable(s) here
    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        // Init 'View' instance(s) with rootView.findViewById here
        // Note: State of variable initialized here could not be saved
        //       in onSavedInstanceState
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance (Fragment level's variables) State here
    }

    @SuppressWarnings("UnusedParameters")
    private void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore Instance (Fragment level's variables) State here
    }


}
