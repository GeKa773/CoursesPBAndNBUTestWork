package com.gekaradchenko.coursespbandnbutestwork;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private final static String API_PRIVATE_BANK = "https://api.privatbank.ua/p24api/exchange_rates?json&date=";
    private final static String API_NBU = "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?date=";

    private TextView datePrivateBankTextView;
    private TextView dateNBUTextView;

    private Calendar calendar;

    //For PrivateBank
    private Double EUR_buy = 0d;
    private Double EUR_sale = 0d;
    private Double USD_buy = 0d;
    private Double USD_sale = 0d;
    private Double RUR_buy = 0d;
    private Double RUR_sale = 0d;

    private BigDecimal EUR_buyDecimal;
    private BigDecimal EUR_saleDecimal;
    private BigDecimal USD_buyDecimal;
    private BigDecimal USD_saleDecimal;
    private BigDecimal RUR_buyDecimal;
    private BigDecimal RUR_saleDecimal;

    private String datePrivateBankForCourses;
    //

    //For NBU
    private Double EUR = 0d;
    private Double USD = 0d;
    private Double RUR = 0d;
    private Double BYN = 0d;
    private Double PLN = 0d;
    private Double CAD = 0d;
    private Double HKD = 0d;

    private BigDecimal EURDecimal;
    private BigDecimal USDDecimal;
    private BigDecimal RURDecimal;
    private BigDecimal BYNDecimal;
    private BigDecimal PLNDecimal;
    private BigDecimal CADDecimal;
    private BigDecimal HKDDecimal;

    private String dateNBUForCourses;

    //

    private RecyclerView recyclerViewForPrivateBank;
    private RecyclerView recyclerViewForNBU;

    private PrivateBankAdapter privateBankAdapter;
    private NBUAdapter nbuAdapter;

    private ArrayList<PrivateBank> privateBankArrayList;
    private ArrayList<NBU> nbuArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(getScreenOrientation()){
            setTitle("");
        }

        init();
        setDateTextView();

        new GetCoursesPrivateBank().execute();
        new GetCoursesNBU().execute();

        privateBankAdapter.setOnItemClickListener(new PrivateBankAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                scrollRecyclerViewForNBU(privateBankArrayList.get(position).getVal());
                Log.d("VAL", privateBankArrayList.get(position).getVal());
            }
        });
    }

    private void scrollRecyclerViewForNBU(String val) {
        for (int i = 0; i < nbuArrayList.size(); i++) {
            if (val.equals(nbuArrayList.get(i).getName())) {
                recyclerViewForNBU.smoothScrollToPosition(i);
                if (!nbuArrayList.get(i).isSelect()){
                selectedIsFalse();
                nbuArrayList.get(i).setSelect(true);
                }
                else selectedIsFalse();

                nbuAdapter.setNbuArrayList(nbuArrayList);
            }
        }
    }
    private void selectedIsFalse(){
        for (int i = 0; i<nbuArrayList.size();i++){
            nbuArrayList.get(i).setSelect(false);
        }
    }

    private void setDateTextView() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat sdfForString = new SimpleDateFormat("yyyyMMdd");

        SpannableString ss = new SpannableString(format.format(date));
        ss.setSpan(new UnderlineSpan(),0,format.format(date).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        datePrivateBankTextView.setText(ss);
        dateNBUTextView.setText(ss);

        datePrivateBankForCourses = format.format(new Date());
        dateNBUForCourses = sdfForString.format(new Date());      // date for String
    }

    private void init() {

        datePrivateBankTextView = findViewById(R.id.datePrivateBankTextView);
        dateNBUTextView = findViewById(R.id.dateNBUTextView);

        calendar = Calendar.getInstance();

        recyclerViewForPrivateBank = findViewById(R.id.recyclerViewForPrivateBank);
        recyclerViewForNBU = findViewById(R.id.recyclerViewForNBU);

        privateBankAdapter = new PrivateBankAdapter();
        nbuAdapter = new NBUAdapter();

    }

    private void checkValPrivateBankForNull() {
        if (EUR_buy == 0 || EUR_buy == null) {
            EUR_buy = 0d;
        }
        if (EUR_sale == 0 || EUR_sale == null) {
            EUR_sale = 0d;
        }
        if (USD_buy == 0 || USD_buy == null) {
            USD_buy = 0d;
        }
        if (USD_sale == 0 || USD_sale == null) {
            USD_sale = 0d;
        }
        if (RUR_buy == 0 || RUR_buy == null) {
            RUR_buy = 0d;
        }
        if (RUR_sale == 0 || RUR_sale == null) {
            RUR_sale = 0d;
        }
    }

    private void checkValNBUForNull() {
        if (EUR == 0 || EUR == null) {
            EUR = 0d;
        }
        if (USD == 0 || USD == null) {
            USD = 0d;
        }
        if (RUR == 0 || RUR == null) {
            RUR = 0d;
        }
        if (BYN == 0 || BYN == null) {
            BYN = 0d;
        }
        if (PLN == 0 || PLN == null) {
            PLN = 0d;
        }
        if (CAD == 0 || CAD == null) {
            CAD = 0d;
        }
        if (HKD == 0 || HKD == null) {
            HKD = 0d;
        }
    }

    private void setValPrivateBankArrayList() {

        privateBankArrayList = new ArrayList<>();
        privateBankArrayList.add(new PrivateBank(getString(R.string.eur_val),
                EUR_buyDecimal.setScale(3, RoundingMode.HALF_UP).toString(),
                EUR_saleDecimal.setScale(3, RoundingMode.HALF_UP).toString()));
        privateBankArrayList.add(new PrivateBank(getString(R.string.usd_val),
                USD_buyDecimal.setScale(3, RoundingMode.HALF_UP).toString(),
                USD_saleDecimal.setScale(3, RoundingMode.HALF_UP).toString()));
        privateBankArrayList.add(new PrivateBank(getString(R.string.rur_val),
                RUR_buyDecimal.setScale(3, RoundingMode.HALF_UP).toString(),
                RUR_saleDecimal.setScale(3, RoundingMode.HALF_UP).toString()));

    }

    private void setValNBUArrayList() {

        nbuArrayList = new ArrayList<>();
        nbuArrayList.add(new NBU(getString(R.string.eur_val),
                EURDecimal.setScale(2, RoundingMode.HALF_UP).toString(),
                getString(R.string.eur_full), false));
        nbuArrayList.add(new NBU(getString(R.string.usd_val),
                USDDecimal.setScale(2, RoundingMode.HALF_UP).toString(),
                getString(R.string.usd_full), false));
        nbuArrayList.add(new NBU(getString(R.string.rur_val),
                RURDecimal.setScale(2, RoundingMode.HALF_UP).toString(),
                getString(R.string.rur_full), false));
        nbuArrayList.add(new NBU(getString(R.string.byn_val),
                BYNDecimal.setScale(2, RoundingMode.HALF_UP).toString(),
                getString(R.string.byn_full), false));
        nbuArrayList.add(new NBU(getString(R.string.pln_val),
                PLNDecimal.setScale(2, RoundingMode.HALF_UP).toString(),
                getString(R.string.pln_full), false));
        nbuArrayList.add(new NBU(getString(R.string.cad_val),
                CADDecimal.setScale(2, RoundingMode.HALF_UP).toString(),
                getString(R.string.cad_full), false));
        nbuArrayList.add(new NBU(getString(R.string.hkd_val),
                HKDDecimal.setScale(2, RoundingMode.HALF_UP).toString(),
                getString(R.string.hkd_full), false));

    }


    public void setDateForPrivateBank(View view) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                Date date = new Date(calendar.getTimeInMillis());

                SpannableString ss = new SpannableString(sdf.format(date));
                ss.setSpan(new UnderlineSpan(),0,sdf.format(date).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                datePrivateBankTextView.setText(ss);      // date for textView
                datePrivateBankForCourses = sdf.format(date);           // date for String

                new GetCoursesPrivateBank().execute();
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public void setDateForNBU(View view) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                SimpleDateFormat sdfForString = new SimpleDateFormat("yyyyMMdd");
                Date date = new Date(calendar.getTimeInMillis());

                SpannableString ss = new SpannableString(sdf.format(date));
                ss.setSpan(new UnderlineSpan(),0,sdf.format(date).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                dateNBUTextView.setText(ss);          // date for textView
                dateNBUForCourses = sdfForString.format(date);      // date for String

                new GetCoursesNBU().execute();
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private class GetCoursesPrivateBank extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result = getContent(API_PRIVATE_BANK + datePrivateBankForCourses);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray array = jsonObject.getJSONArray(getString(R.string.some_json));
                for (int i = 1; i < array.length(); i++) {
                    JSONObject some = (JSONObject) array.get(i);
                    if (some.getString(getString(R.string.val)).equals(getString(R.string.eur_val))) {
                        EUR_sale = some.getDouble(getString(R.string.sale_val));
                        EUR_buy = some.getDouble(getString(R.string.buy_val));
                    }
                    if (some.getString(getString(R.string.val)).equals(getString(R.string.usd_val))) {
                        USD_sale = some.getDouble(getString(R.string.sale_val));
                        USD_buy = some.getDouble(getString(R.string.buy_val));
                    }
                    if (some.getString(getString(R.string.val)).equals(getString(R.string.rur_val))) {
                        RUR_sale = some.getDouble(getString(R.string.sale_val));
                        RUR_buy = some.getDouble(getString(R.string.buy_val));
                    }
                }
                checkValPrivateBankForNull();

                EUR_buyDecimal = new BigDecimal(EUR_buy);
                EUR_saleDecimal = new BigDecimal(EUR_sale);
                USD_buyDecimal = new BigDecimal(USD_buy);
                USD_saleDecimal = new BigDecimal(USD_sale);
                RUR_buyDecimal = new BigDecimal(RUR_buy);
                RUR_saleDecimal = new BigDecimal(RUR_sale);

                if (privateBankArrayList != null) {
                    privateBankArrayList.clear();
                }
                setValPrivateBankArrayList();

                recyclerViewForPrivateBank.setHasFixedSize(true);
                recyclerViewForPrivateBank.setAdapter(privateBankAdapter);
                recyclerViewForPrivateBank.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                privateBankAdapter.setPrivateBankArrayList(privateBankArrayList);


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        private String getContent(String path) {
            try {
                URL url = new URL(path);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.setConnectTimeout(20000);
                BufferedReader reader = new BufferedReader(new InputStreamReader(c.getInputStream()));
                String result = "";
                String line = "";
                while ((line = reader.readLine()) != null) {
                    result += line + "\n";
                }
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }
    }

    private class GetCoursesNBU extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result = getContent(API_NBU + dateNBUForCourses + "&json");

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            try {

                JSONArray array = new JSONArray(result);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject some = (JSONObject) array.get(i);
                    if (some.getString(getString(R.string.cc)).equals(getString(R.string.eur_val))) {
                        EUR = some.getDouble(getString(R.string.rate));
                    }
                    if (some.getString(getString(R.string.cc)).equals(getString(R.string.usd_val))) {
                        USD = some.getDouble(getString(R.string.rate));
                    }
                    if (some.getString(getString(R.string.cc)).equals(getString(R.string.rur_val))) {
                        RUR = some.getDouble(getString(R.string.rate));
                    }
                    if (some.getString(getString(R.string.cc)).equals(getString(R.string.byn_val))) {
                        BYN = some.getDouble(getString(R.string.rate));
                    }
                    if (some.getString(getString(R.string.cc)).equals(getString(R.string.pln_val))) {
                        PLN = some.getDouble(getString(R.string.rate));
                    }
                    if (some.getString(getString(R.string.cc)).equals(getString(R.string.cad_val))) {
                        CAD = some.getDouble(getString(R.string.rate));
                    }
                    if (some.getString(getString(R.string.cc)).equals(getString(R.string.hkd_val))) {
                        HKD = some.getDouble(getString(R.string.rate));
                    }

                }
                checkValNBUForNull();

                EURDecimal = new BigDecimal(EUR);
                USDDecimal = new BigDecimal(USD);
                RURDecimal = new BigDecimal(RUR);
                BYNDecimal = new BigDecimal(BYN);
                PLNDecimal = new BigDecimal(PLN);
                CADDecimal = new BigDecimal(CAD);
                HKDDecimal = new BigDecimal(HKD);


                if (nbuArrayList != null) {
                    nbuArrayList.clear();
                }
                setValNBUArrayList();

                recyclerViewForNBU.setHasFixedSize(true);
                recyclerViewForNBU.setAdapter(nbuAdapter);
                recyclerViewForNBU.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                nbuAdapter.setNbuArrayList(nbuArrayList);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        private String getContent(String path) {
            try {
                URL url = new URL(path);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.setConnectTimeout(20000);
                BufferedReader reader = new BufferedReader(new InputStreamReader(c.getInputStream()));
                String result = "";
                String line = "";
                while ((line = reader.readLine()) != null) {
                    result += line + "\n";
                }
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }
    }

    private boolean getScreenOrientation(){
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            return false;
        else return true;
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
}