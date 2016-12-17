package android.webcrawler.osori.hungryosori.Fragment;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webcrawler.osori.hungryosori.Adapter.CrawlerListAdapter;
import android.webcrawler.osori.hungryosori.CrawlerInfo.CrawlerInfos;
import android.webcrawler.osori.hungryosori.R;
import android.webcrawler.osori.hungryosori.Common.Constant;
import android.widget.ListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;

/**
 * Created by kunju on 2016-08-25.
 */
public class CrawlerFragment extends Fragment{

    private int page;
    private ListView listView;
    private DisplayImageOptions options;
    public CrawlerListAdapter listAdapter = null;

    public static CrawlerFragment newInstance(int pagePosition){
        if(pagePosition == Constant.PAGE_MY ||
                pagePosition == Constant.PAGE_ALL) {
            CrawlerFragment fragment = new CrawlerFragment();

            Bundle bundle = new Bundle();
            bundle.putInt("PAGE", pagePosition);
            fragment.setArguments(bundle);

            return fragment;
        }else {
            return null;
        }
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if(args != null){
            page      = args.getInt("PAGE");
        }

        options  = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.color.white)          // 이미지 로딩 중 나타나는 이미지
                .showImageForEmptyUri(R.color.white)        // 값이 없을 경우
                .showImageOnFail(R.color.white)             // 에러가 났을 경우
                .cacheInMemory(true)                        // In memory 캐시에 저장
                .cacheOnDisk(true)                          // Disk Cache 저장
                .considerExifParams(true)
                .postProcessor(new BitmapProcessor() {      // 이미지 후 처리
                    @Override
                    public Bitmap process(Bitmap bmp) {
                        return Bitmap.createScaledBitmap(bmp, 150, 150, false);
                    }
                })
                .build();

        if(page == Constant.PAGE_MY) {
            listAdapter = new CrawlerListAdapter(getActivity(), R.layout.list_crawler, R.id.list_crawler_textView_title,
                    CrawlerInfos.getInstance().getCrawlerInfoList(), options);
        }else if(page == Constant.PAGE_ALL){
            listAdapter = new CrawlerListAdapter(getActivity(), R.layout.list_crawler, R.id.list_crawler_textView_title,
                    CrawlerInfos.getInstance().getCrawlerInfoList(), options);
        }
        if(listAdapter != null) {
            CrawlerInfos.getInstance().attach(listAdapter);
        }
    }

    // 프레그먼트의 뷰를 생성한다
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_crawler, container, false);

        listView = (ListView)view.findViewById(R.id.fragment_crawler_listView);
        if(listAdapter != null) {
            listView.setAdapter(listAdapter);                       // 어댑터 설정
        }

        return view;
    }

}
