package edu.stonybrook.middleboxes;

import android.app.ActionBar;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;

import edu.stonybrook.utils.UrlBuilder;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements OnClickListener{

    private String mServer=null;
    private String mHttpPort =null;
    private String mHttpAltPort = null;
    private String mRandomPort = null;
    private ListView mTestsListView;
    private CustomAdapter listViewAdapter;
    Context mContext;
    String[] testsArray;
    String[] testDescArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();
        mServer = getResources().getString(R.string.Server_IP);
        mHttpPort = getResources().getString(R.string.HTTP_Port);
        mHttpAltPort = getResources().getString(R.string.HTTP_Alt_Port);
        mRandomPort = getResources().getString(R.string.Random_Port);
        mTestsListView = (ListView)this.findViewById(R.id.testListView);

        testsArray = getResources().getStringArray(R.array.testsCollection);
        testDescArray = getResources().getStringArray(R.array.testText);
        listViewAdapter = new CustomAdapter();
        int testsCount = testsArray.length;
        int i;
        for(i=0;i<testsCount;i++){
            TableItem item = new TableItem(testsArray[i],testDescArray[i],"Run Tests");
            listViewAdapter.addItem(item);
        }
        mTestsListView.setAdapter(listViewAdapter);
        mTestsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                View v = view.findViewById(R.id.testInfoTextView);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                                                                                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                //ExpandAnimation expandAni = new ExpandAnimation(v,500);
                //v.startAnimation(expandAni);
                //v.setVisibility(View.VISIBLE);
                if( v.getVisibility() == View.VISIBLE) {
                    params.height = 0;
                    v.setLayoutParams(params);
                    v.setVisibility(View.INVISIBLE);
                }
                else{
                    params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
                    v.setLayoutParams(params);
                    v.setVisibility(View.VISIBLE);
                }
            }
        });
        Button testsRunButton = (Button)findViewById(R.id.runTests);
        testsRunButton.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        HTMLTests htmlTester = new HTMLTests(v);
        UrlBuilder urlBuilder_port8080 = new UrlBuilder(mServer,mHttpAltPort,v);
        String url = urlBuilder_port8080.getServerUrl();
        Observable<String> http404TestObserable = htmlTester.performHTTP404(url);
        http404TestObserable.subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new middleboxObserver("HTTP 404 Modified"));
        Observable<String> httpCustomHostTestObservable = htmlTester.performHTTPCustomHost(url);
        httpCustomHostTestObservable.subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new middleboxObserver("HTTP custom Host"));

        Observable<String> httpUserAgentTestObservable = htmlTester.performHTTPCustomHost(url);
        httpUserAgentTestObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new middleboxObserver("USER Agent Matched"));
    }


    private class CustomAdapter extends BaseAdapter{

        private ArrayList<TableItem> mData = new ArrayList<TableItem>();
        private LayoutInflater mlayoutInflator;

        public CustomAdapter(){
            mlayoutInflator = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        public void addItem(TableItem item){
            mData.add(item);
            //notifyDataSetChanged();
        }
        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
        public void updateResult(String tname, String tResult){
            Iterator iter = mData.iterator();
            while(iter.hasNext()){
                TableItem item = (TableItem)iter.next();
                if(item.testname.equals(tname)){
                    item.testResult = tResult;
                }
            }
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TableItem item = mData.get(position);
            View view = convertView;
            if(view == null){
                view = mlayoutInflator.inflate(R.layout.tests_list,null);
            }
            TextView textViewTestName = (TextView) view.findViewById(R.id.testName);
            TextView textViewTestResul = (TextView)view.findViewById(R.id.result);
            TextView textViewTestInfo = (TextView) view.findViewById(R.id.testInfoTextView);

            textViewTestName.setText(item.testname);
            textViewTestResul.setText(item.testResult);
            textViewTestInfo.setText(item.testDesc);
            return view;
        }
    }

    private class TableItem{
        public String testname=null;
        public String testDesc = null;
        public String testResult=null;
        public TableItem(String tName, String description, String tResult){
            testname = tName;
            testResult = tResult;
            testDesc = description;
        }
    }
    private class middleboxObserver implements Observer<String>{
        String testName;
        String result;
        public middleboxObserver(String tname){
            testName = tname;
        }
        @Override
        public void onCompleted() {
            listViewAdapter.updateResult(testName,result);
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(String s) {
            result = s;
        }
    }

}
