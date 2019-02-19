/*
 * Copyright 2019 Esri
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.esri.arcgisruntime.sample.analyzehotspots;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

public class ProgressDialogFragment extends DialogFragment {

  private static final String ARGS_TITLE = ProgressDialogFragment.class.getSimpleName() + "_title";

  private static final String ARGS_CANCEL_TEXT = ProgressDialogFragment.class.getSimpleName() + "_cancel_text";

  private String mTitle;

  private String mCancelText;

  private OnProgressDialogCancelListener mOnCancelListener;

  public static ProgressDialogFragment newInstance(String title, String cancelText) {
    ProgressDialogFragment fragment = new ProgressDialogFragment();
    Bundle args = new Bundle();
    args.putString(ARGS_TITLE, title);
    args.putString(ARGS_CANCEL_TEXT, cancelText);
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // prevent re-creation during configuration change
    setRetainInstance(true);
    setCancelable(false);

    if (getArguments() != null) {
      this.mTitle = getArguments().getString(ARGS_TITLE);
      this.mCancelText = getArguments().getString(ARGS_CANCEL_TEXT);
    }
  }

  @NonNull @Override public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    super.onCreateDialog(savedInstanceState);
    // create a dialog to show progress
    final ProgressDialog progressDialog = new ProgressDialog(getContext());
    progressDialog.setTitle(mTitle);
    progressDialog.setIndeterminate(false);
    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    progressDialog.setMax(100);
    progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, mCancelText, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        mOnCancelListener.onProgressDialogCancel();
        onDismiss(dialog);
      }
    });
    return progressDialog;
  }

  @Override public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof OnProgressDialogCancelListener) {
      this.mOnCancelListener = (OnProgressDialogCancelListener) context;
    }
  }

  @Override
  public void onDestroyView() {
    Dialog dialog = getDialog();
    // handles https://code.google.com/p/android/issues/detail?id=17423
    if (dialog != null && getRetainInstance()) {
      dialog.setDismissMessage(null);
    }
    super.onDestroyView();
  }

  public void setProgress(int progress) {
    ((ProgressDialog) getDialog()).setProgress(progress);
  }

  interface OnProgressDialogCancelListener {
    void onProgressDialogCancel();
  }

}
