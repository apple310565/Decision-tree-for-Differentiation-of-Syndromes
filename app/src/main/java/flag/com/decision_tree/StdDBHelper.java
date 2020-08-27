package flag.com.decision_tree;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.io.FileInputStream;
import android.content.Context;
import android.widget.Toast;

public class StdDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Class";
    private static final int DATABASE_VERSION = 1;
    public StdDBHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        //try{
            /*
        db.execSQL("CREATE TABLE Data( "+
                "A1 Text,A2 Text,A3 Text,A4 Text,A5 Text,A6 Text,A7 Text,A8 Text,A9 Text,A10 Text,A11 Text,A12 Text,A13 Text,A14 Text,A15 Text,A16 Text,A17 Text,A18 Text,A19"+
                " Text,A20 Text,A21 Text,A22 Text,A23 Text,A24 Text,primary key(A1,A2,A3,A4,A5))");*/
        db.execSQL("CREATE TABLE Data("+
                "aa Text, bb Text ,cc Text ,dd Text ,ee Text ,A2 Text, primary key(aa,bb,ee,cc,dd,A2))");
      //  db.execSQL("CREATE TABLE Data(_date Text primary key, "+
        //        "flow int , color int , quality int , isStart int , isEnd int, carry int)");
    //}catch (Exception e) {

    //}
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int Version){
        db.execSQL("DROP TABLE IF EXISTS Data");
        onCreate(db);
    }
}

