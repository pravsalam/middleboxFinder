package edu.stonybrook.middleboxes;

import android.app.ActionBar;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;

import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements OnClickListener{

    private String mServer=null;
    private String mHttpPort =null;
    private String mHttpAltPort = null;
    private String mRandomPort = null;
    private ListView mTestsView;
    private CustomAdapter listViewAdapter;
    Context mContext;
    String[] testsArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();
        mServer = getResources().getString(R.string.Server_IP);
        mHttpPort = getResources().getString(R.string.HTTP_Port);
        mHttpAltPort = getResources().getString(R.string.HTTP_Alt_Port);
        mRandomPort = getResources().getString(R.string.Random_Port);
        mTestsView = (ListView)this.findViewById(R.id.testListView);

        testsArray = getResources().getStringArray(R.array.testsCollection);
        listViewAdapter = new CustomAdapter();
        int testsCount = testsArray.length;
        int i;
        for(i=0;i<testsCount;i++){
            TableItem item = new TableItem(testsArray[i],"");
            listViewAdapter.addItem(item);
        }
        mTestsView.setAdapter(listViewAdapter);

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
        Observable<String> http404Obserable = new HTMLTests().performHTTP404(mServer,mHttpAltPort);
        http404Obserable.subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                Log.i("Praveen", "Result is" + s);
                            }
                        });
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
                if(item.testname == tname){
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
            textViewTestName.setText(item.testname);
            textViewTestResul.setText(item.testResult);
            return view;
        }
    }

    private class TableItem{
        public String testname=null;
        public String testResult=null;
        public TableItem(String tName, String tResult){
            testname = tName;
            testResult = tResult;
        }
    }

}
