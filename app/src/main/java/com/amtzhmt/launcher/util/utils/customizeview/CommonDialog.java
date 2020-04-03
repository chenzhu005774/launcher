package com.amtzhmt.launcher.util.utils.customizeview;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amtzhmt.launcher.R;


/**
* description:自定义dialog
*/

public class CommonDialog extends Dialog {

    private TextView tvProgress;

    private ProgressBar mProgress;

    LinearLayout but_parent;
/**
 * 显示的图片
 */
private ImageView imageIv ;

/**
 * 显示的标题
 */
private TextView titleTv ;

/**
 * 显示的消息
 */
private TextView messageTv ;

/**
 * 确认和取消按钮
 */
private Button negtiveBn ,positiveBn;

    private int type =1;//默认的
/**
 * 按钮之间的分割线
 */
private View columnLineView ;
public CommonDialog(Context context) {
    super(context, R.style.CustomDialog);
}

 public CommonDialog(Context context,int type) {
        super(context, R.style.CustomDialog);
        this.type = type;
    }

    /**
 * 都是内容数据
 */
private String message;
private String title;
private String positive,negtive ;
private int imageResId = -1 ;

/**
 * 底部是否只有一个按钮
 */
private boolean isSingle = false;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (type==2){
        setContentView(R.layout.progress_dialog_layout);
    }else {
        setContentView(R.layout.common_dialog_layout);
    }
    //按空白处不能取消动画
    setCanceledOnTouchOutside(false);
    //初始化界面控件
    initView();
    //初始化界面数据
    refreshView();
    //初始化界面控件的事件
    initEvent();
}

/**
 * 初始化界面的确定和取消监听器
 */
private void initEvent() {
    //设置确定按钮被点击后，向外界提供监听
    positiveBn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if ( onClickBottomListener!= null) {
                onClickBottomListener.onPositiveClick();
            }
        }
    });
    //设置取消按钮被点击后，向外界提供监听
    negtiveBn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if ( onClickBottomListener!= null) {
                onClickBottomListener.onNegtiveClick();
            }
        }
    });
}

/**
 * 初始化界面控件的显示数据
 */
private void refreshView() {
    //如果用户自定了title和message
    if (!TextUtils.isEmpty(title)) {
        titleTv.setText(title);
        titleTv.setVisibility(View.VISIBLE);
    }else {
        titleTv.setVisibility(View.GONE);
    }
    if (!TextUtils.isEmpty(message)) {
        messageTv.setText(message);
    }
    //如果设置按钮的文字
    if (!TextUtils.isEmpty(positive)) {
        positiveBn.setText(positive);
    }else {
        positiveBn.setText("确定");
    }
    if (!TextUtils.isEmpty(negtive)) {
        negtiveBn.setText(negtive);
    }else {
        negtiveBn.setText("取消");
    }

    if (imageResId!=-1){
        imageIv.setImageResource(imageResId);
        imageIv.setVisibility(View.VISIBLE);
    }else {
        imageIv.setVisibility(View.GONE);
    }
    /**
     * 只显示一个按钮的时候隐藏取消按钮，回掉只执行确定的事件
     */
    if (isSingle){
        columnLineView.setVisibility(View.GONE);
        negtiveBn.setVisibility(View.GONE);
    }else {
        negtiveBn.setVisibility(View.VISIBLE);
        columnLineView.setVisibility(View.VISIBLE);
    }
}

@Override
public void show() {
    super.show();
    refreshView();
}

/**
 * 初始化界面控件
 */
private void initView() {
    negtiveBn = (Button) findViewById(R.id.negtive);
    positiveBn = (Button) findViewById(R.id.positive);
    titleTv = (TextView) findViewById(R.id.title);
    messageTv = (TextView) findViewById(R.id.message);
    imageIv = (ImageView) findViewById(R.id.image);
    columnLineView = findViewById(R.id.column_line);
    if (type==2){
        mProgress = (ProgressBar) findViewById(R.id.progress_pp);
        tvProgress = (TextView) findViewById(R.id.tv_progress_pp);
        but_parent = (LinearLayout)findViewById(R.id.but_parent);
    }
}

  public void setProgress(int progress){
      if (mProgress!=null)
      mProgress .setProgress(progress);
      tvProgress.setText(progress+"%");
  }
    public void setInfoMessage(String message,CommonDialog dialog) {
        but_parent.setVisibility(View.VISIBLE);
        messageTv.setText(message);
        mProgress.setVisibility(View.GONE);
        tvProgress.setVisibility(View.GONE);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                return false;
            }
        });
        dialog.setOnClickBottomListener(new OnClickBottomListener() {
            @Override
            public void onPositiveClick() {
                dismiss();
            }

            @Override
            public void onNegtiveClick() {
                dismiss();
            }
        });
    }
/**
 * 设置确定取消按钮的回调
 */
public OnClickBottomListener onClickBottomListener;
public CommonDialog setOnClickBottomListener(OnClickBottomListener onClickBottomListener) {
    this.onClickBottomListener = onClickBottomListener;
    return this;
}
public interface OnClickBottomListener{
    /**
     * 点击确定按钮事件
     */
    public void onPositiveClick();
    /**
     * 点击取消按钮事件
     */
    public void onNegtiveClick();
}

public String getMessage() {
    return message;
}

public CommonDialog setMessage(String message) {

    this.message = message;
    return this ;
}


public String getTitle() {
    return title;
}

public CommonDialog setTitle(String title) {
    this.title = title;
    return this ;
}

public String getPositive() {
    return positive;
}

public CommonDialog setPositive(String positive) {
    this.positive = positive;
    return this ;
}

public String getNegtive() {
    return negtive;
}

public CommonDialog setNegtive(String negtive) {
    this.negtive = negtive;
    return this ;
}

public int getImageResId() {
    return imageResId;
}

public boolean isSingle() {
    return isSingle;
}

public CommonDialog setSingle(boolean single) {
    isSingle = single;
    return this ;
}

public CommonDialog setImageResId(int imageResId) {
    this.imageResId = imageResId;
    return this ;
}

}
