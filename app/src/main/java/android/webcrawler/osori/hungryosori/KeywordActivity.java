package android.webcrawler.osori.hungryosori;

import android.app.Activity;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.DividerItemDecoration;
import android.view.View;
import android.webcrawler.osori.hungryosori.Adapter.KeywordAdapter;
import android.webcrawler.osori.hungryosori.Dialog.KwAddDialog;
import android.webcrawler.osori.hungryosori.Model.KeywordInfo;
import android.webcrawler.osori.hungryosori.databinding.ActivityKeywordBinding;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class KeywordActivity extends Activity {

    private static final int LAYOUT = R.layout.activity_keyword;
    private ActivityKeywordBinding keyWordBinding;
    //private RecyclerView.Adapter adapter;
    private KeywordAdapter adapter;
    private ArrayList<KeywordInfo> data = new ArrayList<>();

    private AlertDialog.Builder alert;
    private KwAddDialog kwAddDialog;
    private ArrayList<KeywordInfo> server = new ArrayList<>();

    View.OnClickListener leftClickListener;
    View.OnClickListener rightClickListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyword);
        keyWordBinding = DataBindingUtil.setContentView(this, LAYOUT);
        alert  = new AlertDialog.Builder(this);

        leftClickListener =new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kwAddDialog.cancel();
            }
        };
        rightClickListener =new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(kwAddDialog.getEdit1Str().isEmpty()){
                    Toast.makeText(getApplicationContext(), "keyword is empty",Toast.LENGTH_SHORT).show();
                }
                else {
                    server.add(new KeywordInfo("pushToken", kwAddDialog.getEdit1Str(), keyWordBinding.crawlerTitleTxt.getText().toString(), "url", false));
                    setData();
                    kwAddDialog.dismiss();
                }
            }
        };


        kwAddDialog = new KwAddDialog(this, keyWordBinding.crawlerTitleTxt.getText().toString(), "keyword", leftClickListener, rightClickListener);

        //intent로 받은 crawler를 crawler title에 넣기



        setRecyclerView();
    }

    private void setRecyclerView(){
        keyWordBinding.keywordRecyclerView.setHasFixedSize(false);
        adapter = new KeywordAdapter(data);
        keyWordBinding.keywordRecyclerView.setAdapter(adapter);
        adapter.setItemClick(new KeywordAdapter.ItemClick(){
            @Override
            public void onClick(KeywordInfo kw, int position) {
                if(kw.getFunct() == true){
                    //추가
                    Toast.makeText(getApplicationContext(),position+" ",Toast.LENGTH_SHORT).show();

//                    View mView = getLayoutInflater().inflate(R.layout.dialog_addkw, null);
//                    final EditText mkeyword = (EditText) mView.findViewById(R.id.keyword_edt);
                    kwAddDialog.show();
                } else{

                }

            }

            @Override
            public void onLongClick(KeywordInfo kw, int position){
                final int pos = position;
                if(kw.getFunct() == true){

                } else{
                    //삭제
                    alert.setTitle("alert");
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //확인
                            server.remove(pos);
                            setData();
                        }
                    });
                    alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //취소
                        }
                    });
                    alert.show();
                }
            }
        });

        keyWordBinding.keywordRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getApplicationContext(), new LinearLayoutManager(this).getOrientation());
        keyWordBinding.keywordRecyclerView.addItemDecoration(dividerItemDecoration);

        setData();
    }

    private void setData(){
        data.clear();
// RecyclerView 에 들어갈 데이터를 추가합니다.
        for(KeywordInfo keyword : server){
            data.add(new KeywordInfo(keyword.getPushToken(), keyword.getKeyword(), keyword.getCrawler(), keyword.getUrl(), keyword.getFunct()));
        }
        data.add(new KeywordInfo(null, "add", null, null, true));

// 데이터 추가가 완료되었으면 notifyDataSetChanged() 메서드를 호출해 데이터 변경 체크를 실행합니다.
        adapter.notifyDataSetChanged();
    }
}
