package demo.test.user.musicplayer.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.text.TextUtilsCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import demo.test.user.musicplayer.R;
import demo.test.user.musicplayer.activity.MainActivity;
import demo.test.user.musicplayer.adapter.NetListAdapter;
import demo.test.user.musicplayer.bean.Mp3Info;
import demo.test.user.musicplayer.constants.ConstantsForSelf;
import demo.test.user.musicplayer.service.PlayerService;

import static android.content.ContentValues.TAG;

/**
 * Created by user on 2018-01-02.
 */
public class NetListFragment extends Fragment implements View.OnClickListener {
    private Context context;
    private MainActivity activity;
    private PlayerService mPlayService;

    private LinearLayout ll_tv_search;
    private LinearLayout ll_et_search;
    private ImageView iv_search_btn;
    private LinearLayout ll_loading;
    private ListView lv_net;
    private NetListAdapter mAdapter;
    private List<Mp3Info> mList = new ArrayList<>();
    private EditText et_search;

    public static Fragment newInstance(int position) {
        NetListFragment net = new NetListFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        net.setArguments(args);
        return net;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        this.activity = (MainActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_net, container, false);
        // 初始化View控件
        initView(view);
        // 获取网络数据
        getNetList();
        return view;
    }

    private void getNetList() {
        new MyAsyncTask().execute(ConstantsForSelf.URL_AUTHO + ConstantsForSelf.URL_HOT_PATH);
    }

    /**
     * 初始化View
     *
     * @param view 父控件
     */
    private void initView(View view) {
        ll_tv_search = view.findViewById(R.id.ll_tv_search);
        ll_et_search = view.findViewById(R.id.ll_et_search);
        et_search = view.findViewById(R.id.et_search);
        iv_search_btn = view.findViewById(R.id.iv_search_btn);
        ll_loading = view.findViewById(R.id.ll_loading);
        iv_search_btn.setOnClickListener(this);
        ll_tv_search.setOnClickListener(this);
        et_search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // 隐藏输入框
                    hideInput();
                    ll_et_search.setVisibility(View.GONE);
                    ll_tv_search.setVisibility(View.VISIBLE);
                }
            }
        });

        lv_net = view.findViewById(R.id.lv_net);
        mAdapter = new NetListAdapter(context, mList);
        lv_net.setAdapter(mAdapter);
    }

    /**
     * 隐藏输入框
     */
    private void hideInput() {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(et_search.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_tv_search:
                ll_tv_search.setVisibility(View.GONE);
                ll_et_search.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    et_search.setShowSoftInputOnFocus(true);
                }
                // 显示输入框
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInputFromInputMethod(et_search.getWindowToken(),InputMethodManager.SHOW_IMPLICIT);
                break;
            case R.id.iv_search_btn:
                // 搜索
                searchHandle();
                break;
            default:
                break;
        }
    }

    /**
     * 搜索处理
     */
    private void searchHandle() {
        // 1.隐藏输入框
        hideInput();
        // 2.搜索
        String searchText = et_search.getText().toString().trim();
        if (!TextUtils.isEmpty(searchText)) {
            new MyAsyncTask().execute(ConstantsForSelf.URL_AUTHO + ConstantsForSelf.URL_SEARCH_PATH + searchText);
        } else {
            Toast.makeText(context, "内容不能为空", Toast.LENGTH_SHORT).show();
        }
        // 3.清空搜索框
        et_search.setText("");
    }

    /**
     * 搜索网络列表的异步处理类
     */
    class MyAsyncTask extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ll_loading.setVisibility(View.VISIBLE);
            mList.clear();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                Document doc = Jsoup.connect(params[0]).get();
                Elements songTitles = doc.select("span.song-title");
                Elements authorList = doc.select("span.author_list");
                int size = songTitles.size();
                for (int i = 0; i < size; i++) {
                    // 收费或百度音乐盒跳过
                    String title = songTitles.get(i).getElementsByTag("a").text();
                    String author = authorList.get(i).getElementsByTag("a").text();
//                        Log.i("tag", "title:" + title + "-author:" + author + "\n");
                    Mp3Info mp3Info = new Mp3Info();
                    mp3Info.setTitle(title);
                    mp3Info.setArtist(author);
                    mList.add(mp3Info);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ll_loading.setVisibility(View.GONE);
            ll_et_search.setVisibility(View.GONE);
            ll_tv_search.setVisibility(View.VISIBLE);
            Log.i(TAG, "onPostExecute: size:"+mList.size());
            mAdapter.notifyDataSetChanged();
        }
    };
}
