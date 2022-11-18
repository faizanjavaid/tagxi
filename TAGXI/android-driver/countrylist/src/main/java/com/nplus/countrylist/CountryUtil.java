package com.nplus.countrylist;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Created by pandian on 25/10/16.
 */
public class CountryUtil {
    Context ctx;
    protected static final TreeSet<String> CANADA_CODES = new TreeSet<String>();

    static {
        CANADA_CODES.add("204");
        CANADA_CODES.add("236");
        CANADA_CODES.add("249");
        CANADA_CODES.add("250");
        CANADA_CODES.add("289");
        CANADA_CODES.add("306");
        CANADA_CODES.add("343");
        CANADA_CODES.add("365");
        CANADA_CODES.add("387");
        CANADA_CODES.add("403");
        CANADA_CODES.add("416");
        CANADA_CODES.add("418");
        CANADA_CODES.add("431");
        CANADA_CODES.add("437");
        CANADA_CODES.add("438");
        CANADA_CODES.add("450");
        CANADA_CODES.add("506");
        CANADA_CODES.add("514");
        CANADA_CODES.add("519");
        CANADA_CODES.add("548");
        CANADA_CODES.add("579");
        CANADA_CODES.add("581");
        CANADA_CODES.add("587");
        CANADA_CODES.add("604");
        CANADA_CODES.add("613");
        CANADA_CODES.add("639");
        CANADA_CODES.add("647");
        CANADA_CODES.add("672");
        CANADA_CODES.add("705");
        CANADA_CODES.add("709");
        CANADA_CODES.add("742");
        CANADA_CODES.add("778");
        CANADA_CODES.add("780");
        CANADA_CODES.add("782");
        CANADA_CODES.add("807");
        CANADA_CODES.add("819");
        CANADA_CODES.add("825");
        CANADA_CODES.add("867");
        CANADA_CODES.add("873");
        CANADA_CODES.add("902");
        CANADA_CODES.add("905");
    }

    protected SparseArray<ArrayList<Country>> mCountriesMap = new SparseArray<ArrayList<Country>>();
    protected ImageView img_Spinner;
    protected EditText mPhoneEdit;
    protected PhoneNumberUtil mPhoneNumberUtil = PhoneNumberUtil.getInstance();
    protected String mLastEnteredPhone;
    protected CountryAdapter mAdapter;
    private DialogCountryAdapter mDialogCountryAdapter;
    public ArrayList<Country> AllCountrydata;
    Dialog dialog_countrycode;
    public String ContryCode;
    String LatestCountrycode;
    public CountryCodeChangeListener listener;

    public CountryUtil(Context ctx, String ContryCode) {
        this.ctx = ctx;
        this.ContryCode = ContryCode;
    }

    public CountryUtil(Context ctx) {
        this.ctx = ctx;
    }
//    protected AdapterView.OnItemSelectedListener mOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
//        @Override
//        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//            Country c = (Country) mSpinner.getItemAtPosition(position);
//            if (mLastEnteredPhone != null && mLastEnteredPhone.startsWith(c.getCountryCodeStr())) {
//                return;
//            }
//            mPhoneEdit.getText().clear();
//            mPhoneEdit.getText().insert(mPhoneEdit.getText().length() > 0 ? 1 : 0, String.valueOf(c.getCountryCode()));
//            mPhoneEdit.setSelection(mPhoneEdit.length());
//            mLastEnteredPhone = null;
//        }
//
//        @Override
//        public void onNothingSelected(AdapterView<?> parent) {
//        }
//    };


    protected OnPhoneChangedListener mOnPhoneChangedListener = new OnPhoneChangedListener() {
        @Override
        public void onPhoneChanged(String phone) {
            try {
                mLastEnteredPhone = phone;
                Phonenumber.PhoneNumber p = mPhoneNumberUtil.parse(phone, null);
                ArrayList<Country> list = mCountriesMap.get(p.getCountryCode());
                Country country = null;
                if (list != null) {
                    if (p.getCountryCode() == 1) {
                        String num = String.valueOf(p.getNationalNumber());
                        if (num.length() >= 3) {
                            String code = num.substring(0, 3);
                            if (CANADA_CODES.contains(code)) {
                                for (Country c : list) {
                                    // Canada has priority 1, US has priority 0
                                    if (c.getPriority() == 1) {
                                        country = c;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if (country == null) {
                        for (Country c : list) {
                            if (c.getPriority() == 0) {
                                country = c;
                                break;
                            }
                        }
                    }
                }
                if (country != null) {
                    final int position = country.getNum();

                    final Country Dublicate_contry = country;
                    img_Spinner.post(new Runnable() {
                        @Override
                        public void run() {
//                            img_Spinner.setImageResource(Dublicate_contry.getResId());
                            img_Spinner.setTag(Dublicate_contry.getResId());
                        }
                    });
                }
            } catch (NumberParseException ignore) {
            }

        }
    };

    public void initUI(final EditText mPhoneEdit, ImageView img_Spinner) {
        this.mPhoneEdit = mPhoneEdit;
        this.img_Spinner = img_Spinner;
        this.img_Spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowCountryDialog();
            }
        });
        this.mPhoneEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowCountryDialog();
            }
        });
        mAdapter = new CountryAdapter(ctx);
        this.mPhoneEdit.addTextChangedListener(new CustomPhoneNumberFormattingTextWatcher(mOnPhoneChangedListener));
        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    char c = source.charAt(i);
                    if (dstart > 0 && !Character.isDigit(c)) {
                        return "";
                    }
                }
                return null;
            }
        };

        mPhoneEdit.setFilters(new InputFilter[]{filter});
    }


    public void initUI(final EditText mPhoneEdit, CountryCodeChangeListener listener, ImageView img_Spinner) {
        this.mPhoneEdit = mPhoneEdit;
        this.img_Spinner = img_Spinner;
        this.listener = listener;
        this.img_Spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowCountryDialog();
            }
        });


        mAdapter = new CountryAdapter(ctx);
        this.mPhoneEdit.addTextChangedListener(new CustomPhoneNumberFormattingTextWatcher(mOnPhoneChangedListener));
        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    char c = source.charAt(i);
                    if (dstart > 0 && !Character.isDigit(c)) {
                        return "";
                    }
                }
                return null;
            }
        };

        this.mPhoneEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowCountryDialog();
            }
        });
        mPhoneEdit.setFilters(new InputFilter[]{filter});
    }

    boolean isEditTxtNotAvailable;

    /**
     * Initiating country code libray without EditText and ImageView
     * for loading the Country codes Dialog
     */
    public void initUIDisableEdit(Context context, CountryCodeChangeListener listener, View img_Flag) {
//        this.mPhoneEdit = mPhoneEdit;
        isEditTxtNotAvailable = true;
//        this.img_Spinner = img_Flag;
        this.listener = listener;
        img_Flag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowCountryDialog();
            }
        });
        new AsyncPhoneInitTask(context).execute();
    }


    public void initCodes(Context context) {
        new AsyncPhoneInitTask(context).execute();
    }

    protected class AsyncPhoneInitTask extends AsyncTask<Void, Void, ArrayList<Country>> {

        private int mSpinnerPosition = -1;
        private Context mContext;

        public AsyncPhoneInitTask(Context context) {
            mContext = context;
        }

        @Override
        protected ArrayList<Country> doInBackground(Void... params) {
            ArrayList<Country> data = new ArrayList<Country>(91);
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(mContext.getApplicationContext().getAssets().open("countries.dat"), StandardCharsets.UTF_8));

                // do reading, usually loop until end of file reading
                String line;
                int i = 0;
                while ((line = reader.readLine()) != null) {
                    //process line
                    Country c = new Country(mContext, line, i);
                    data.add(c);
                    ArrayList<Country> list = mCountriesMap.get(c.getCountryCode());
                    if (list == null) {
                        list = new ArrayList<Country>();
                        mCountriesMap.put(c.getCountryCode(), list);
                    }
                    list.add(c);
                    i++;
                }


            } catch (IOException e) {
                //log the exception
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        //log the exception
                    }
                }
            }
            if (isEditTxtNotAvailable) {
                String countryRegion = PhoneUtils.getCountryRegionFromPhone(mContext);
                LatestCountrycode = countryRegion == null ? ContryCode : ((countryRegion.isEmpty()) ? ContryCode : countryRegion);
                //LatestCountrycode = ContryCode == null ? countryRegion : ((ContryCode.isEmpty()) ? countryRegion : ContryCode);

                int code = mPhoneNumberUtil.getCountryCodeForRegion(LatestCountrycode);
                ArrayList<Country> list = mCountriesMap.get(code);
                if (list != null) {
                    for (Country c : list) {
                        if (c.getPriority() == 0) {
                            mSpinnerPosition = c.getNum();
                            break;
                        }
                    }
                }
            } else {
                if (!TextUtils.isEmpty(mPhoneEdit.getText().toString().replace("+", ""))) {
                    return data;
                }
                String countryRegion = PhoneUtils.getCountryRegionFromPhone(mContext);
                LatestCountrycode = ContryCode!= null ? ContryCode :countryRegion;
                int code = mPhoneNumberUtil.getCountryCodeForRegion(LatestCountrycode);

                ArrayList<Country> list = mCountriesMap.get(code);
                if (list != null) {
                    for (Country c : list) {
                        if (c.getPriority() == 0) {
                            mSpinnerPosition = c.getNum();
                            break;
                        }
                    }
                }

            }
            return data;
        }

        @Override
        protected void onPostExecute(ArrayList<Country> data) {
            if (isEditTxtNotAvailable) {
                AllCountrydata = data;
            } else {
                mAdapter.addAll(data);
                AllCountrydata = data;
                if (mSpinnerPosition > 0) {
                    img_Spinner.setImageResource(data.get(mSpinnerPosition).getResId());
                    img_Spinner.setTag(data.get(mSpinnerPosition).getResId());
                    mPhoneEdit.setText((data.get(mSpinnerPosition).getCountryCode() > 0 ? ("+" + data.get(mSpinnerPosition).getCountryCode()) : (data.get(mSpinnerPosition).getCountryCode())) + "");
                    if (listener != null && mPhoneEdit != null)
                        listener.onCountryCodeChanged(mPhoneEdit.getText().toString(), LatestCountrycode, data.get(mSpinnerPosition).getName());
                }
            }
        }
    }

    public boolean validate() {
        if (mLastEnteredPhone != null) {
            try {
                Phonenumber.PhoneNumber p = mPhoneNumberUtil.parse(mLastEnteredPhone, null);
                String region = mPhoneNumberUtil.getRegionCodeForNumber(p);
                if (mPhoneNumberUtil.isValidNumberForRegion(p, region)) {
                    return true;
                }
            } catch (NumberParseException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    protected void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    protected void showKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }


    public void ShowCountryDialog() {
        final AlertDialog.Builder countryBuilder = new AlertDialog.Builder(ctx);

        countryBuilder.setTitle(R.string.text_country_codes);
        final EditText editText = new EditText(ctx);
        final ListView listview = new ListView(ctx);
        editText.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_menu_search, 0, 0, 0);
        LinearLayout layout = new LinearLayout(ctx);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(editText);
        layout.addView(listview);
        countryBuilder.setView(layout);
        mDialogCountryAdapter = new DialogCountryAdapter((Activity) ctx, AllCountrydata);
        listview.setAdapter(mDialogCountryAdapter);
        editText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s,
                                          int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mDialogCountryAdapter.getFilter().filter(s);
                mDialogCountryAdapter.notifyDataSetChanged();
            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv_listitem = ((TextView) ((LinearLayout) view).getChildAt(1));
                Country c = (Country) tv_listitem.getTag();
                if (isEditTxtNotAvailable) {
                    if (listener != null && c != null && c.getCountryISO() != null)
                        listener.onCountryCodeChanged(String.valueOf(c.getCountryCode()), c.getCountryISO(), c.getName());
                    if (dialog_countrycode != null) {
                        if (dialog_countrycode.isShowing())
                            dialog_countrycode.dismiss();

                        return;
                    }
                } else {
                    if (mLastEnteredPhone != null && mLastEnteredPhone.startsWith(c.getCountryCodeStr())) {
                        if (dialog_countrycode != null)
                            if (dialog_countrycode.isShowing())
                                dialog_countrycode.dismiss();

                        return;
                    }
                    mPhoneEdit.getText().clear();
                    mPhoneEdit.getText().insert(mPhoneEdit.getText().length() > 0 ? 1 : 0, String.valueOf(c.getCountryCode()));
                    mPhoneEdit.setSelection(mPhoneEdit.length());
                    if (listener != null && mPhoneEdit != null && c != null) {
                        LatestCountrycode = c.getCountryISO();
                        listener.onCountryCodeChanged(mPhoneEdit.getText().toString(), c.getCountryISO(), c.getName());
                    }
                    img_Spinner.setImageResource(c.getResId());
                    img_Spinner.setTag(c.getResId());
                }
                mLastEnteredPhone = null;
                dialog_countrycode.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                dialog_countrycode.dismiss();
            }
        });

        countryBuilder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog_countrycode = countryBuilder.create();
        if (!((Activity) ctx).isFinishing())
            dialog_countrycode.show();
    }

    public static float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

}
