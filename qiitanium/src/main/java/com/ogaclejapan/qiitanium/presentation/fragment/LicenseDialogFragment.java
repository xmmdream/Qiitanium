package com.ogaclejapan.qiitanium.presentation.fragment;

import com.ogaclejapan.qiitanium.R;
import com.ogaclejapan.qiitanium.util.IOUtils;
import com.ogaclejapan.rx.binding.Rx;
import com.ogaclejapan.rx.binding.RxActions;
import com.ogaclejapan.rx.binding.RxUtils;
import com.ogaclejapan.rx.binding.RxView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

public class LicenseDialogFragment extends AppDialogFragment {

    public static final String TAG = LicenseDialogFragment.class.getSimpleName();

    private static final String LICENSE_PATH = "license.txt";

    public static LicenseDialogFragment newInstance() {
        return new LicenseDialogFragment();
    }

    public LicenseDialogFragment() {
        super(TAG);
    }

    private Rx<TextView> mLicenseText;

    @Override
    protected View onSetupView(final LayoutInflater inflater, final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_license, null, false);
        mLicenseText = RxView.findById(view, R.id.fragment_license_text);
        return view;
    }

    @Override
    protected Subscription onBind() {
        return Subscriptions.from(
                mLicenseText.bind(loadLicenseHtml(), RxActions.setText())
        );
    }

    private Observable<String> loadLicenseHtml() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                try {
                    final String text = IOUtils.readAllFromAssets(getContext(), LICENSE_PATH);
                    RxUtils.onNextIfSubscribed(subscriber, text);
                    RxUtils.onCompletedIfSubsribed(subscriber);
                } catch (IOException ioe) {
                    RxUtils.onErrorIfSubscribed(subscriber, ioe);
                }
            }
        });
    }

}
