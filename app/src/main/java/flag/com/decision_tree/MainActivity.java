package flag.com.decision_tree;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    private StdDBHelper dbHelper;
    int n=6;
    int max=3;
    node head;
    LinearLayout LL=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //建立SQLOHleper物件
        dbHelper = new StdDBHelper(this);
        db =dbHelper.getWritableDatabase();//開啟資料庫
        head =new node();
        head.WHERE="";
        head.times=1;

        try {
            //load();
            produce(head);
            trace(head);
            //view(head);
        }catch (Exception e) {
            e.printStackTrace();
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Main ERROR")
                    .setMessage(e.toString())
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setNegativeButton("cancel",null).create()
                    .show();
            Toast.makeText(this,"DayDay_LOAD: "+e.toString(),Toast.LENGTH_SHORT).show();
        }

    }
    public void trace(final  node head){
        LinearLayout L=(LinearLayout)findViewById(R.id.L);
        if(LL!=null)L.removeView(LL);
        LL=new LinearLayout(this);
        L.addView(LL, LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        LL.setOrientation(LinearLayout.VERTICAL);

        for(int i=0;i<head.NumOfChild;i++){
            LinearLayout Lout= new LinearLayout(this);
            LL.addView(Lout, LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
            Lout.setPadding(20,20,20,20);
            final LinearLayout Lin= new LinearLayout(this);
            Lin.setBackgroundColor(Color.parseColor("#3700B3"));
            Lout.addView(Lin, LinearLayout.LayoutParams.MATCH_PARENT,200);
            Lin.setGravity(Gravity.CENTER);
            TextView tv=new TextView(this);
            Lin.addView(tv);
            tv.setText(head.child_label[i]);
            tv.setTextColor(Color.parseColor("#FFFFFF"));
            tv.setTextSize(30);
            final String label=head.child_label[i];
            final node next=head.child[i];
            Lin.setOnClickListener(new  View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView tv4=(TextView) findViewById(R.id.tv2);
                    if(tv4.getText().equals(""))tv4.setText(label);
                    else tv4.setText(tv4.getText()+"+"+label);
                    trace(next);
                }
            });
        }
        if(head.NumOfChild!=0) {
            LinearLayout Lout = new LinearLayout(this);
            LL.addView(Lout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            Lout.setPadding(20, 20, 20, 20);
            final LinearLayout Lin = new LinearLayout(this);
            Lin.setBackgroundColor(Color.parseColor("#3700B3"));
            Lout.addView(Lin, LinearLayout.LayoutParams.MATCH_PARENT, 200);
            Lin.setGravity(Gravity.CENTER);
            TextView tv = new TextView(this);
            Lin.addView(tv);
            tv.setText("不知道");
            tv.setTextColor(Color.parseColor("#FFFFFF"));
            tv.setTextSize(30);
            final String label = "不知道";
            Lin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LinearLayout L=(LinearLayout)findViewById(R.id.L);
                    if(LL!=null)L.removeView(LL);
                    TextView tv4 = (TextView) findViewById(R.id.tv2);
                    if (tv4.getText().equals("")) tv4.setText(label);
                    else tv4.setText(tv4.getText() + "+" + label);

                    Cursor tmp;
                    if(!head.WHERE.equals(""))tmp = db.rawQuery("Select distinct A2 from Data where " + head.WHERE, null);
                    else tmp = db.rawQuery("Select distinct ans from Data", null);
                    tmp.moveToFirst();
                    String ans="";
                    for (int j = 0; j < tmp.getCount(); j++) {
                        if(!ans.equals(""))ans+=", ";
                        ans+=tmp.getString(0);
                        tmp.moveToNext();
                    }
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("辨證結果")
                                .setMessage(ans)
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).setNegativeButton("cancel",null).create()
                                .show();
                }
            });
        }
        if(head.NumOfChild==0){
            Cursor tmp=db.rawQuery("Select distinct A2 from Data where "+head.WHERE,null);
            tmp.moveToFirst();
            String ans="";
            for (int j = 0; j < tmp.getCount(); j++) {
                if(!ans.equals(""))ans+=", ";
                ans+=tmp.getString(0);
                tmp.moveToNext();
            }
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("辨證結果")
                    .setMessage(ans)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setNegativeButton("cancel",null).create()
                    .show();
            }

    }

    public void view(node head){
        TextView tv=(TextView) findViewById(R.id.textView);
        for(int i=0;i<head.NumOfChild;i++){
            for(int j=1;j<head.times*2;j++)tv.setText(tv.getText()+"\t");
            tv.setText(tv.getText()+head.child_label[i]);
            if(head.NumOfChild>0)tv.setText(tv.getText()+"\n");
            view(head.child[i]);
        }
        if(head.NumOfChild==0){
            Cursor tmp=db.rawQuery("Select A2 from Data where "+head.WHERE,null);
            tmp.moveToFirst();
            String ans="";
            for (int j = 0; j < tmp.getCount(); j++) {
                if(!ans.equals(""))ans+=", ";
                ans+="["+tmp.getString(0)+"]";
                tmp.moveToNext();
            }
            tv.setText(tv.getText()+ans+"\n");

        }

    }

    public class node{
        public String WHERE="";
        public int spilt=-1;
        public float Ent;
        public int NumOfChild=0;
        public node [] child;
        public String [] child_label;
        int times;
    }
    public void produce(node head){
        int select=getSplitAttri(head);
        if(select==-1)return;
        //取得所有屬性
        Cursor ColumnName;
        if(!(head.WHERE).equals(""))ColumnName = db.rawQuery("Select * from Data where "+head.WHERE,null);
        else ColumnName = db.rawQuery("Select * from Data ",null);
        ColumnName.moveToFirst();
        ArrayList <String> TYPE =new ArrayList<String>();
        for(int i=0;i<n-1;i++)TYPE.add(ColumnName.getColumnName(i));
        ColumnName.close();
        head.spilt=select;
        //取得該屬性的所有值
        if(!(head.WHERE).equals(""))ColumnName = db.rawQuery("Select distinct "+TYPE.get(select) +" from Data where "+head.WHERE,null);
        else ColumnName = db.rawQuery("Select distinct "+TYPE.get(select) +" from Data ",null);
        ColumnName.moveToFirst();
        ArrayList <String> attr =new ArrayList<String>();
        for(int j=0;j<ColumnName.getCount();j++){
            attr.add(ColumnName.getString(0));
            ColumnName.moveToNext();
        }
        ColumnName.close();
        head.NumOfChild=attr.size();
        head.child_label=new String[head.NumOfChild];
        head.child=new node[head.NumOfChild];

        for(int i=0;i<attr.size();i++){
            head.child_label[i]=attr.get(i);
            node child=new node();
            if(!head.WHERE.equals(""))child.WHERE=head.WHERE+" and "+TYPE.get(select)+" = '"+attr.get(i)+"' ";
            else child.WHERE=TYPE.get(select)+" = '"+attr.get(i)+"' ";
            child.times=head.times+1;
            head.child[i]=child;
            produce(child);
        }
        //TextView tv=(TextView) findViewById(R.id.TV);
        //tv.setText(tv.getText()+"\n"+TYPE.get(select));
    }
    //得知從哪個分裂可以得到最大的效益
    public int getSplitAttri(node head){
        //TextView tv=(TextView) findViewById(R.id.TV);
        int max_i=-1,total;
        float MAX_G=0;
        head.Ent=getEnt(head.WHERE);
        //取得所有屬性
        Cursor ColumnName;
        if(!(head.WHERE).equals(""))ColumnName = db.rawQuery("Select * from Data where "+head.WHERE,null);
        else ColumnName = db.rawQuery("Select * from Data ",null);
        ColumnName.moveToFirst();
        total=ColumnName.getCount();
        ArrayList <String> TYPE =new ArrayList<String>();
        for(int i=0;i<n-1;i++)TYPE.add(ColumnName.getColumnName(i));
        ColumnName.close();
        for(int i=0;i<TYPE.size();i++){
            if(!(head.WHERE).equals(""))ColumnName = db.rawQuery("Select distinct "+TYPE.get(i) +" from Data where "+head.WHERE,null);
            else ColumnName = db.rawQuery("Select distinct "+TYPE.get(i) +" from Data ",null);
            ColumnName.moveToFirst();
            ArrayList <String> attr =new ArrayList<String>();
            for(int j=0;j<ColumnName.getCount();j++){
                attr.add(ColumnName.getString(0));
                ColumnName.moveToNext();
            }
            ColumnName.close();

            float tmp_G=0;
            for(int j=0;j<attr.size();j++){
                String tmp=" and "+TYPE.get(i)+" = '"+attr.get(j) +"'";
                float attr_ent;
                if(!(head.WHERE).equals(""))attr_ent=getEnt(head.WHERE+tmp);
                else attr_ent=getEnt(TYPE.get(i)+" = '"+attr.get(j) +"'");
                if(!(head.WHERE).equals(""))ColumnName = db.rawQuery("Select * from Data where "+head.WHERE+tmp,null);
                else ColumnName = db.rawQuery("Select * from Data where "+TYPE.get(i)+" = '"+attr.get(j) +"'",null);
                ColumnName.moveToFirst();
                int t=ColumnName.getCount();
                ColumnName.close();
                float p=(float)t/(float)total;
                tmp_G+=p*attr_ent;
            }
            tmp_G=head.Ent-tmp_G;
            if(tmp_G>MAX_G||max_i==-1){
                max_i=i;
                MAX_G=tmp_G;
            }
        }
        if(MAX_G>0)return max_i;
        return -1;
    }
    public float getEnt(String str){
        try{
       float Ent=0;
        ArrayList <String> ans =new ArrayList <String>();
        Cursor tmp;
        if(!str.equals(""))tmp = db.rawQuery("Select distinct A2 from Data where "+str,null);
        else tmp = db.rawQuery("Select distinct A2 from Data ",null);
        tmp.moveToFirst();
        for(int i=0;i<tmp.getCount();i++){
            ans.add(tmp.getString(0));
            tmp.moveToNext();
        }
        tmp.close();

        int total;
        if(!str.equals(""))tmp = db.rawQuery("Select  A2 from Data where "+str,null);
        else tmp = db.rawQuery("Select  A2 from Data ",null);
        tmp.moveToFirst();
        total=tmp.getCount();
        tmp.close();

        for(int i=0;i<ans.size();i++){
            if(!str.equals(""))tmp = db.rawQuery("Select  * from Data where A2 = '"+ans.get(i)+"' and "+str,null);
            else tmp = db.rawQuery("Select  * from Data where A2 = '"+ans.get(i)+"'",null);
            tmp.moveToFirst();
            int num=tmp.getCount();
            float p=(float)num/(float)total;
            Ent+=-1*p*Math.log(p);
        }
       return Ent;} catch (Exception e){

         return 999;
        }
    }
    public void load(){
        db.execSQL("insert into Data values('月經提前','或多或少','淡紅','清稀','','脾虛型')");
        db.execSQL("insert into Data values('月經提前','或多或少','黝深','清稀','','腎氣不固型')");
        db.execSQL("insert into Data values('月經提前','量多','鮮紅','黏稠','','陽盛血熱型')");
        db.execSQL("insert into Data values('月經提前','正常','鮮紅','黏稠','','陽盛血熱型')");
        db.execSQL("insert into Data values('月經提前','量多','紫紅','黏稠','','陽盛血熱型')");
        db.execSQL("insert into Data values('月經提前','正常','紫紅','黏稠','','陽盛血熱型')");
        db.execSQL("insert into Data values('月經提前','或多或少','鮮紅','黏稠','','肝鬱血熱型')");
        db.execSQL("insert into Data values('月經提前','或多或少','紫紅','黏稠','','肝鬱血熱型')");
        db.execSQL("insert into Data values('月經提前','或多或少','鮮紅','有血塊','','肝鬱血熱型')");
        db.execSQL("insert into Data values('月經提前','或多或少','紫紅','有血塊','','肝鬱血熱型')");
        db.execSQL("insert into Data values('月經提前','或多或少','黝深','黏稠','','陰虛血熱型')");
        db.execSQL("insert into Data values('月經提前','正常','黝深','黏稠','','陰虛血熱型')");
        db.execSQL("insert into Data values('月經提前','量少','黝深','有血塊','','血瘀型')");
    }
    public void reset(View view){
        TextView tv4=(TextView) findViewById(R.id.tv2);
        tv4.setText("");
        trace(head);
    }
    public void load_Data(){
        try {
            InputStreamReader inputReader = new InputStreamReader( getResources().getAssets().open("TEST.csv") );
            Toast.makeText(this,"找到檔案了",Toast.LENGTH_SHORT).show();
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line="";
            while((line = bufReader.readLine()) != null) {
                String[] Day = line.split(",");
                ContentValues cv = new ContentValues();
                String str="";
                for(int i=1;i<=24;i++)cv.put("A"+Integer.toString(i),"NULL");
               for(int i=0;i<23;i++){

                       str += Integer.toString(i) + " : " + Day[i] + "\n";
                       cv.put("A"+Integer.toString(i+1),Day[i]);

               }
                /*new AlertDialog.Builder(MainActivity.this)
                        .setTitle("辨證結果")
                        .setMessage(str)
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).setNegativeButton("cancel",null).create()
                        .show();*/
                db.insert("Data",null,cv);
            }
        } catch (Exception e) {
            e.printStackTrace();
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("辨證結果")
                    .setMessage(e.toString())
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setNegativeButton("cancel",null).create()
                    .show();
            Toast.makeText(this,"DayDay_LOAD: "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }
}
