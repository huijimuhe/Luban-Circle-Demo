package com.huijimuhe.luban_circle_demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.huijimuhe.luban_circle_demo.adapter.ImagePickerListAdapter;
import com.huijimuhe.luban_circle_demo.adapter.base.AbstractRenderAdapter;
import com.huijimuhe.luban_circle_demo.utils.ImageCompressUtils;
import com.huijimuhe.luban_circle_demo.utils.QiniuUtils;
import com.huijimuhe.luban_circle_demo.utils.ViewUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class MainActivity extends AppCompatActivity {
    private String TAG = "luban_circle_demo";
    public static final int REQUEST_IMAGE = 21;
    private List<String> mImagePathes = new ArrayList<>();
    private ImagePickerListAdapter mImagePickerAdapter;
    private RecyclerView mImagePickerListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post();
            }
        });

        initImagePicker();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE) {
            mImagePathes = result.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
            replaceImages();
        }

    }

    /**
     * 打开图片选择器
     */
    private void openPickImageGallery() {
        Intent intent = new Intent(MainActivity.this, MultiImageSelectorActivity.class);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    /**
     * 上传图片
     * TODO 更新你的上传规则
     */
    private void post() {
        //线程不安全,判断是否压缩完毕;原始图片地址-压缩后文件
        final Map<String, File> compressedFiles = new HashMap<>();
        //线程不安全,判断是否上传完毕;压缩后文件-上传KEY
        final Map<File, String> uploadedKeys = new HashMap<>();

        for (final String imgPath : mImagePathes) {
            //并行压缩图片
            ImageCompressUtils.from(MainActivity.this)
                    .load(imgPath)
                    .execute(new ImageCompressUtils.OnCompressListener() {
                        @Override
                        public void onSuccess(File file) {
                            //添加压缩结果
                            compressedFiles.put(imgPath, file);
                            //判断图片压缩是否已操作结束
                            if (compressedFiles.size() == mImagePathes.size()) {
                                for (final File resFile : compressedFiles.values()) {
                                    //并行上传图片
                                    QiniuUtils.from(MainActivity.this).queue(resFile, new QiniuUtils.UploadListener() {
                                        @Override
                                        public void onSuccess(File compressedFile, String key) {
                                            //添加压缩文件对应上传key
                                            uploadedKeys.put(compressedFile, key);
                                            //判断图片上传是否已结束
                                            if (uploadedKeys.size() == compressedFiles.size()) {
                                                //图片地址参数
                                                String img_urls = "";
                                                for (final String imgPath : mImagePathes) {
                                                    //按照原始图片顺序排序
                                                    File temp = compressedFiles.get(imgPath);
                                                    img_urls += uploadedKeys.get(temp) + ",";
                                                }
                                                //TODO 完成图片上传后，你的业务逻辑

                                            }
                                        }

                                        @Override
                                        public void onError(int code) {
                                            Log.d(TAG, String.format("code is %d", code));
                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d(TAG, "图片上传失败");
                        }
                    });
        }
    }

    /**
     * 初始化图片列表
     */
    private void initImagePicker() {
        //adapter
        mImagePickerAdapter = new ImagePickerListAdapter(new ArrayList<String>());
        mImagePickerAdapter.setOnItemClickListener(new AbstractRenderAdapter.onItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                Log.d(TAG, "clicked");
            }
        });

        //recycler view
        mImagePickerListView = ViewUtils.findViewById(this, R.id.list_image_picker);
        StaggeredGridLayoutManager linearLayoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);

        mImagePickerListView.setLayoutManager(linearLayoutManager);
        mImagePickerListView.setAdapter(mImagePickerAdapter);

        //footer
        View footer = getLayoutInflater().inflate(R.layout.footer_image_picker, null);
        mImagePickerAdapter.setFooterView(footer);
        footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPickImageGallery();
            }
        });
    }

    /**
     * 显示图片
     * TODO 请自行按需修改
     */
    private void replaceImages() {
        mImagePickerAdapter.replace(mImagePathes);
        if (mImagePathes.size() == 9) {
            mImagePickerAdapter.removeFooterView();
        } else if (!mImagePickerAdapter.hasFooterView()) {
            View footer = getLayoutInflater().inflate(R.layout.footer_image_picker, null);
            mImagePickerAdapter.setFooterView(footer);
        }
    }
}
