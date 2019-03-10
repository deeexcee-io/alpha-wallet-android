package io.stormbird.wallet.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import io.stormbird.wallet.R;
import io.stormbird.wallet.entity.DApp;
import io.stormbird.wallet.ui.widget.OnDappClickListener;
import io.stormbird.wallet.ui.widget.adapter.MyDappsListAdapter;
import io.stormbird.wallet.util.DappBrowserUtils;
import io.stormbird.wallet.widget.AWalletAlertDialog;


public class MyDappsFragment extends Fragment {
    private static final String ON_DAPP_CLICK_LISTENER = "onDappClickListener";
    private MyDappsListAdapter adapter;
    private OnDappClickListener onDappClickListener;
    private AWalletAlertDialog dialog;
    private TextView noDapps;

    public static MyDappsFragment newInstance(OnDappClickListener listener) {
        MyDappsFragment f = new MyDappsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ON_DAPP_CLICK_LISTENER, listener);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        onDappClickListener = (OnDappClickListener) getArguments().get(ON_DAPP_CLICK_LISTENER);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_my_dapps, container, false);
        adapter = new MyDappsListAdapter(
                getData(),
                onDappClickListener,
                this::onDappRemoved,
                this::onDappEdited);
        RecyclerView list = view.findViewById(R.id.my_dapps_list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setAdapter(adapter);
        noDapps = view.findViewById(R.id.no_dapps);
        showOrHideViews();
        return view;
    }

    private List<DApp> getData() {
        return DappBrowserUtils.getMyDapps(getContext());
    }

    private void onDappEdited(DApp dapp) {
        List<DApp> myDapps = DappBrowserUtils.getMyDapps(getContext());
        for (DApp d : myDapps) {
            if (d.getName().equals(dapp.getName())
                    && d.getUrl().equals(dapp.getUrl())) {
//                d.setName(newName);
//                d.setUrl(newUrl);
                break;
            }
        }
        DappBrowserUtils.saveToPrefs(getContext(), myDapps);
    }

    private void onDappRemoved(DApp dapp) {
        dialog = new AWalletAlertDialog(getActivity());
        dialog.setTitle(R.string.title_remove_dapp);
        dialog.setMessage(getString(R.string.remove_from_my_dapps, dapp.getName()));
        dialog.setIcon(AWalletAlertDialog.NONE);
        dialog.setButtonText(R.string.action_remove);
        dialog.setButtonListener(v -> {
            removeDapp(dapp);
            dialog.dismiss();
        });
        dialog.setSecondaryButtonText(R.string.dialog_cancel_back);
        dialog.show();
    }

    private void removeDapp(DApp dapp) {
        List<DApp> myDapps = DappBrowserUtils.getMyDapps(getContext());
        for (DApp d : myDapps) {
            if (d.getName().equals(dapp.getName())
                    && d.getUrl().equals(dapp.getUrl())) {
                myDapps.remove(d);
                break;
            }
        }
        DappBrowserUtils.saveToPrefs(getContext(), myDapps);
        updateData();
    }

    private void updateData() {
        adapter.setDapps(DappBrowserUtils.getMyDapps(getContext()));
        showOrHideViews();
    }

    private void showOrHideViews() {
        if (adapter.getItemCount() > 0) {
            noDapps.setVisibility(View.GONE);
        } else {
            noDapps.setVisibility(View.VISIBLE);
        }
    }
}
