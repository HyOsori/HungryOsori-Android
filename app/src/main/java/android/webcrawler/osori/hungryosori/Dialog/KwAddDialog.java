package android.webcrawler.osori.hungryosori.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.webcrawler.osori.hungryosori.R;

public class KwAddDialog extends Dialog {


    private TextView mtitleView;
    private EditText mEditText1;
    private Button mRightButton;
    private Button mLeftButton;


    private View.OnClickListener mRightClickListener;
    private View.OnClickListener mLeftClickListener;

    private String mtitle;
    private String mcontent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //외부 화면 흐리게
        WindowManager.LayoutParams IpWindow = new WindowManager.LayoutParams();
        IpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        IpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(IpWindow);
        setContentView(R.layout.dialog_kw_add);

        mtitleView = (TextView)  findViewById(R.id.crawler_txt);
        mEditText1 = (EditText) findViewById(R.id.keyword_edt);
        mRightButton  = (Button) findViewById(R.id.ok_btn);
        mLeftButton  = (Button) findViewById(R.id.cancel_btn);

        setTitle(mtitle);
        setContent(mcontent);
        setClickListener(mLeftClickListener , mRightClickListener);
    }


    public KwAddDialog(Context context , String title , String content1 ,
                       View.OnClickListener leftListener , View.OnClickListener rightListener) {
        super(context , android.R.style.Theme_Translucent_NoTitleBar);
        this.mtitle = title;
        //this.mEditText1.setText(content1);
        this.mLeftClickListener = leftListener;
        this.mRightClickListener = rightListener;
    }

    private void setTitle(String title){
        mtitleView.setText(title);
    }

    private void setContent(String content){
        mEditText1.setText(content);
    }

    public String getEdit1Str(){
        return mEditText1.getText().toString();
    }


    private void setClickListener(View.OnClickListener left , View.OnClickListener right){
        if(left!=null && right!=null){
            mLeftButton.setOnClickListener(left);
            mRightButton.setOnClickListener(right);
        }else if(left!=null && right==null){
            mLeftButton.setOnClickListener(left);
        }else {

        }
    }
}
