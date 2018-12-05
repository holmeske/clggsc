package com.sc.clgg.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Spannable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.NineGridViewAdapter;
import com.sc.clgg.R;
import com.sc.clgg.activity.friendscircle.CommentActivity;
import com.sc.clgg.activity.login.LoginRegisterActivity;
import com.sc.clgg.activity.friendscircle.PictureActivity;
import com.sc.clgg.bean.Check;
import com.sc.clgg.bean.TruckFriend;
import com.sc.clgg.dialog.AlertDialogHelper;
import com.sc.clgg.retrofit.RetrofitHelper;
import com.sc.clgg.tool.helper.LogHelper;
import com.sc.clgg.tool.helper.MeasureHelper;
import com.sc.clgg.tool.helper.TextHelper;
import com.sc.clgg.util.ConfigUtil;
import com.sc.clgg.util.PotatoKt;
import com.sc.clgg.util.Tools;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.graphics.Typeface.BOLD;
import static android.graphics.Typeface.NORMAL;

/**
 * @author：lvke
 * @date：2018/6/20 13:10
 */
public class TruckFriendsAdapter extends RecyclerView.Adapter<TruckFriendsAdapter.MyHolder> {

    public List<TruckFriend.A> listAll = new ArrayList<>();
    private Context mContext;
    private View.OnClickListener listener;
    private boolean noMore;

    public TruckFriendsAdapter() {
    }

    public void clear() {
        listAll.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new MyHolder(LayoutInflater.from(mContext).inflate(R.layout.item_truck_friends, parent, false));
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);

        if (payloads.isEmpty()) {
            // payloads 为 空，说明是更新整个 ViewHolder
            onBindViewHolder(holder, holder.getAdapterPosition());
        } else {
            // payloads 不为空，这只更新需要更新的 View 即可。
            LogHelper.e("局部刷新");

            if ((int) payloads.get(0) == 1) {
                holder.tv_loadmore.setVisibility(View.GONE);
            } else if ((int) payloads.get(0) == 2) {
                LogHelper.e("刷新局部");
                TruckFriend.A bean = listAll.get(CommentActivity.Companion.getPosition());

                setCommens(bean, holder);

                updateCommentNumber(holder, bean);

                CommentActivity.Companion.initValue();
            } else {
                holder.tv_loadmore.setVisibility(View.VISIBLE);
                holder.tv_loadmore.setText(R.string.load_more);
            }
        }

    }

    public void refreshLast() {
        if (listAll != null && listAll.size() > 0) {
            notifyItemChanged(listAll.size() - 1, 1);
        }
    }

    public void refresh(List<TruckFriend.A> newList, boolean noMore) {
        this.noMore = noMore;
        if (newList != null && newList.size() > 0) {
            notifyItemChanged(listAll.size() - 1, 1);

            listAll.addAll(newList);
            notifyItemRangeInserted(listAll.size(), newList.size());
        }
    }

    public void setLoadMoreListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        if (holder.getAdapterPosition() == listAll.size() - 1) {
            if (!noMore) {
                holder.tv_loadmore.setVisibility(View.VISIBLE);
                holder.tv_loadmore.setText(R.string.loading);
                if (listener != null) {
                    holder.tv_loadmore.setOnClickListener(listener);
                }
            }
        } else {
            holder.tv_loadmore.setVisibility(View.GONE);
        }

        TruckFriend.A bean = listAll.get(holder.getAdapterPosition());

        PotatoKt.setRoundedCornerPicture(holder.iv_head, mContext, bean.getHeadImg());

        if (bean.getType() == 1) {
            holder.tv_official.setVisibility(View.VISIBLE);
        } else {
            holder.tv_official.setVisibility(View.INVISIBLE);
        }
        holder.tv_nickname.setText(bean.getNickName() == null ? "" : bean.getNickName());
        holder.tv_describe.setText(bean.getClientSign() == null ? "" : bean.getClientSign());
        if (!TextUtils.isEmpty(bean.getMessage())) {
            holder.tv_message.setVisibility(View.VISIBLE);

            holder.tv_message.setText(bean.getMessage());
        } else {
            holder.tv_message.setVisibility(View.GONE);
        }

        holder.tv_message.post(new Runnable() {
            @Override
            public void run() {
                int lineCount = holder.tv_message.getLineCount();//行数
                int maxLineCount = holder.tv_message.getMaxLines();
                LogHelper.e("消息行数：" + lineCount + "   最大行数：" + maxLineCount);

                if (lineCount > 6) {
                    holder.tv_all_show.setVisibility(View.VISIBLE);
                    holder.tv_all_show.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (holder.tv_all_show.getText().equals("全文")) {
                                holder.tv_message.setMaxLines(10);
                                holder.tv_all_show.setText("收起");
                            } else {
                                holder.tv_message.setMaxLines(6);
                                holder.tv_all_show.setText("全文");
                            }
                            holder.tv_message.setText(bean.getMessage() == null ? "" : bean.getMessage());
                        }
                    });
                } else {
                    holder.tv_all_show.setVisibility(View.GONE);
                }
            }

        });

        setLauds(bean, holder.tv_lauds);

        setCommens(bean, holder);

        updateCommentNumber(holder, bean);

        updateGiveLikeNumber(holder, bean);

        if (isLike(bean)) {
            bean.setLike(true);
            holder.iv_laud.setImageResource(R.drawable.ico_like_yes);
        } else {
            bean.setLike(false);
            holder.iv_laud.setImageResource(R.drawable.ico_like_no);
        }

        holder.tv_time.setText(bean.getDealTime() == null ? "" : bean.getDealTime());


        if (bean.getDriverCircleImagesList() != null && bean.getDriverCircleImagesList().size() > 0) {
            holder.nineGridView.setVisibility(View.VISIBLE);
            setImages(bean, holder.nineGridView);
        } else {
            holder.nineGridView.setVisibility(View.GONE);
        }

        holder.iv_laud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(new ConfigUtil().getUserid())) {
                    mContext.startActivity(new Intent(mContext, LoginRegisterActivity.class));
                } else {
                    if (bean.isLike()) {
                        removeLike(bean, holder.tv_laud_count, holder.iv_laud, holder.tv_lauds);
                    } else {
                        like(bean, holder.tv_laud_count, holder.iv_laud, holder.tv_lauds, holder.getAdapterPosition());
                    }
                }
            }
        });

        holder.iv_commen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(new ConfigUtil().getUserid())) {
                    mContext.startActivity(new Intent(mContext, LoginRegisterActivity.class));
                } else {
                    mContext.startActivity(new Intent(mContext, CommentActivity.class)
                            .putExtra("circleMessageId", bean.getId())
                            .putExtra("commentUserId", bean.getUserId())
                            .putExtra("position", position));
                }
            }
        });

        if (new ConfigUtil().getUserid().equals(String.valueOf(bean.getUserId()))) {
            holder.tv_delete.setVisibility(View.VISIBLE);
            holder.tv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialogHelper().show((Activity) mContext, "确定删除?", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            new RetrofitHelper().removeMessage(bean.getId()).enqueue(new Callback<Check>() {
                                @Override
                                public void onResponse(Call<Check> call, Response<Check> response) {
                                    if (response.body().getSuccess()) {
                                        Tools.Toast("删除成功");

                                        listAll.remove(holder.getAdapterPosition());
                                        notifyItemRemoved(holder.getAdapterPosition());

                                    } else {
                                        Tools.Toast("删除失败");
                                    }
                                }

                                @Override
                                public void onFailure(Call<Check> call, Throwable t) {
                                    Tools.Toast("删除失败");
                                }
                            });
                        }
                    });
                }
            });
        } else {
            holder.tv_delete.setVisibility(View.GONE);
        }
    }

    private boolean isLike(TruckFriend.A bean) {
        if (bean != null) {
            for (TruckFriend.Laud l : bean.getDriverCircleLaudList()) {
                if (new ConfigUtil().getUserid().equals(String.valueOf(l.getUserId()))) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return listAll == null ? 0 : listAll.size();
    }

    /**
     * 更新点赞数
     *
     * @param holder
     * @param bean
     */
    private void updateGiveLikeNumber(MyHolder holder, TruckFriend.A bean) {
        if (bean.getDriverCircleLaudList() != null) {
            if (bean.getDriverCircleLaudList().size() > 99) {
                holder.tv_laud_count.setText("99+");
            } else {
                holder.tv_laud_count.setText(String.valueOf(bean.getDriverCircleLaudList().size()));
            }
        }
    }

    /**
     * 更新评论数
     *
     * @param holder
     * @param bean
     */
    private void updateCommentNumber(MyHolder holder, TruckFriend.A bean) {
        if (bean.getDriverCircleCommentList().size() > 99) {
            holder.tv_comment_count.setText("99+");
        } else {
            holder.tv_comment_count.setText(String.valueOf(bean.getDriverCircleCommentList().size()));
        }
    }

    private void setLauds(TruckFriend.A bean, TextView tv) {
        if (bean.getDriverCircleLaudList() != null && bean.getDriverCircleLaudList().size() > 0) {
            tv.setVisibility(View.VISIBLE);

            StringBuilder sb = new StringBuilder();
            for (TruckFriend.Laud laud : bean.getDriverCircleLaudList()) {
                sb.append(",").append(laud.getNickName() == null ? "" : laud.getNickName());
            }
            tv.setText(TextUtils.isEmpty(sb) ? "" : sb.substring(1, sb.length()));
        } else {
            tv.setVisibility(View.GONE);
        }
    }

    /**
     * 删除第一个字符
     *
     * @param str
     * @return
     */
    public String deleteFirstLetter(String str) {

        if (str == null || str.isEmpty()) {
            return "";
        }
        return str.substring(1, str.length());
    }

    /**
     * 取消点赞逻辑
     *
     * @param bean
     * @param tv_lauds
     */
    private void setLaudsFilte(TruckFriend.A bean, TextView tv_lauds) {
        StringBuilder sb = new StringBuilder();

        List<TruckFriend.Laud> laudList = bean.getDriverCircleLaudList();

        Iterator<TruckFriend.Laud> iterator = laudList.iterator();
        while (iterator.hasNext()) {
            TruckFriend.Laud laud = iterator.next();
            if (laudList.contains(laud)) {
                if (String.valueOf(laud.getUserId()).equals(new ConfigUtil().getUserid())) {
                    iterator.remove();
                } else {
                    sb.append(",").append(laud.getNickName() == null ? "" : laud.getNickName());
                }
            }
        }
        if (sb.length() > 0) {
            tv_lauds.setVisibility(View.VISIBLE);
            tv_lauds.setText(deleteFirstLetter(sb.toString()));
        } else {
            tv_lauds.setVisibility(View.GONE);
        }
    }

    private void showAllCommens(TruckFriend.A bean, MyHolder holder) {

        holder.ll_commen.removeAllViews();
        if (bean.getDriverCircleCommentList() != null) {
            for (TruckFriend.Commen commen : bean.getDriverCircleCommentList()) {

                TextView textView = new TextView(mContext);
                textView.setPadding(0, 0, 0, MeasureHelper.dp2px(mContext, 10));
                Spannable spannable = TextHelper.instance.setSpannable(mContext,
                        new String[]{commen.getNickName() == null ? ": " : commen.getNickName() + ": ", (commen.getComment() == null ? "" : commen.getComment())},
                        new int[]{R.color._6978ab, R.color._444},
                        new int[]{15, 15},
                        new int[]{BOLD, NORMAL});
                textView.setText(spannable);
                holder.ll_commen.addView(textView);
                if (new ConfigUtil().getUserid().equals(String.valueOf(commen.getUserId()))) {
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new AlertDialogHelper().show((Activity) mContext, "确定删除?", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    new RetrofitHelper().removeOpinion(commen.getId() + "").enqueue(new Callback<Check>() {
                                        @Override
                                        public void onResponse(Call<Check> call, Response<Check> response) {
                                            if (response.body().getSuccess()) {
                                                Tools.Toast("删除成功");
                                                holder.ll_commen.removeView(textView);
                                            } else {
                                                Tools.Toast("删除失败");
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Check> call, Throwable t) {
                                            Tools.Toast("删除失败");
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
            }
        }
        TextView textView = new TextView(mContext);
        textView.setPadding(0, 0, 0, MeasureHelper.dp2px(mContext, 10));
        Spannable spannable = TextHelper.instance.setSpannable(mContext,
                new String[]{"收起评论"},
                new int[]{R.color._6978ab},
                new int[]{15},
                new int[]{NORMAL});
        textView.setText(spannable);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCommens(bean, holder);
            }
        });
        holder.ll_commen.addView(textView);
    }

    private void setCommens(TruckFriend.A bean, MyHolder holder) {
        if (bean == null) {
            return;
        }

        holder.ll_commen.removeAllViews();
        if (bean.getDriverCircleCommentList() != null) {
            for (TruckFriend.Commen commen : bean.getDriverCircleCommentList()) {
                if (holder.ll_commen.getChildCount() == 3) {
                    TextView textView = new TextView(mContext);
                    textView.setPadding(0, 0, 0, MeasureHelper.dp2px(mContext, 10));
                    Spannable spannable = TextHelper.instance.setSpannable(mContext,
                            new String[]{"查看全部" + bean.getDriverCircleCommentList().size() + "条评论"},
                            new int[]{R.color._6978ab},
                            new int[]{15},
                            new int[]{NORMAL});
                    textView.setText(spannable);
                    holder.ll_commen.addView(textView);
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showAllCommens(bean, holder);
                        }
                    });
                    break;
                }

                TextView textView = new TextView(mContext);
                textView.setPadding(0, 0, 0, MeasureHelper.dp2px(mContext, 10));
                Spannable spannable = TextHelper.instance.setSpannable(mContext,
                        new String[]{commen.getNickName() == null ? ": " : commen.getNickName() + ": ", (commen.getComment() == null ? "" : commen.getComment())},
                        new int[]{R.color._6978ab, R.color._444},
                        new int[]{15, 15},
                        new int[]{BOLD, NORMAL});
                textView.setText(spannable);
                holder.ll_commen.addView(textView);
                if (new ConfigUtil().getUserid().equals(String.valueOf(commen.getUserId()))) {
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new AlertDialogHelper().show((Activity) mContext, "确定删除?", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    new RetrofitHelper().removeOpinion(commen.getId() + "").enqueue(new Callback<Check>() {
                                        @Override
                                        public void onResponse(Call<Check> call, Response<Check> response) {
                                            if (response.body().getSuccess()) {
                                                Tools.Toast("删除成功");
                                                bean.getDriverCircleCommentList().remove(commen);
                                                holder.ll_commen.removeView(textView);

                                                updateCommentNumber(holder, bean);
                                            } else {
                                                Tools.Toast("删除失败");
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Check> call, Throwable t) {
                                            Tools.Toast("删除失败");
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
            }
        }
    }

    private void like(TruckFriend.A bean, TextView tv, ImageView iv, TextView tv_lauds, int position) {
        iv.setEnabled(false);
        new RetrofitHelper().like(bean.getId(), bean.getUserId()).enqueue(new Callback<Check>() {
            @Override
            public void onResponse(Call<Check> call, Response<Check> response) {
                iv.setEnabled(true);
                if (response.body().getSuccess()) {
                    bean.setLike(true);
                    Toast.makeText(mContext, "点赞成功", Toast.LENGTH_SHORT).show();

                    StringBuilder sb = new StringBuilder();
                    if (bean.getDriverCircleLaudList().size() == 0) {
                        sb.append(new ConfigUtil().getNickName() == null ? "" : new ConfigUtil().getNickName());
                    } else {
                        for (TruckFriend.Laud laud : bean.getDriverCircleLaudList()) {
                            sb.append(",").append(laud.getNickName() == null ? "" : laud.getNickName());
                        }
                        sb.append(",").append(new ConfigUtil().getNickName() == null ? "" : new ConfigUtil().getNickName());

                    }
                    String text = sb.toString();

                    if (text.startsWith(",")) {
                        tv_lauds.setText(text.substring(1, sb.length()));
                    } else {
                        tv_lauds.setText(text);
                    }

                    tv_lauds.setVisibility(View.VISIBLE);

                    iv.setImageResource(R.drawable.ico_like_yes);
                    if (!tv.getText().equals("99+")) {
                        tv.setText(String.valueOf(Integer.parseInt(tv.getText().toString()) + 1));
                    }
                } else {
                    Toast.makeText(mContext, "点赞失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Check> call, Throwable t) {
                iv.setEnabled(true);
                Toast.makeText(mContext, "点赞失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void removeLike(TruckFriend.A bean, TextView tv, ImageView iv, TextView tv_lauds) {
        iv.setEnabled(false);
        new RetrofitHelper().removeLike(bean.getId()).enqueue(new Callback<Check>() {
            @Override
            public void onResponse(Call<Check> call, Response<Check> response) {
                iv.setEnabled(true);
                if (response.body().getSuccess()) {
                    bean.setLike(false);
                    Toast.makeText(mContext, "取消点赞成功", Toast.LENGTH_SHORT).show();

                    setLaudsFilte(bean, tv_lauds);

                    iv.setImageResource(R.drawable.ico_like_no);
                    if (!tv.getText().equals("99+")) {
                        tv.setText(String.valueOf(Integer.parseInt(tv.getText().toString()) - 1));
                    }
                } else {
                    Toast.makeText(mContext, "取消点赞失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Check> call, Throwable t) {
                iv.setEnabled(true);
                Toast.makeText(mContext, "取消点赞失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setImages(TruckFriend.A bean, NineGridView nineGridView) {
        ArrayList<ImageInfo> imageInfo = new ArrayList<>();
        if (bean != null) {
            for (TruckFriend.Image imageDetail : bean.getDriverCircleImagesList()) {
                ImageInfo info = new ImageInfo();
                info.setThumbnailUrl(imageDetail.getImgUrl());
                info.setBigImageUrl(imageDetail.getImgUrl());
                imageInfo.add(info);
            }
        }
        nineGridView.setAdapter(new ClickNineGridViewAdapter(mContext, imageInfo));
    }

    class MyHolder extends RecyclerView.ViewHolder {
        private NineGridView nineGridView;
        private ImageView iv_head;
        private TextView tv_nickname, tv_official, tv_describe, tv_message, tv_all_show, tv_comment_count, tv_laud_count, tv_time, tv_loadmore, tv_delete, tv_lauds;
        private LinearLayout ll_commen;
        private ImageView iv_laud, iv_commen;

        public MyHolder(View itemView) {
            super(itemView);
            iv_head = itemView.findViewById(R.id.iv_head);
            tv_nickname = itemView.findViewById(R.id.tv_nickname);
            tv_official = itemView.findViewById(R.id.tv_official);
            tv_describe = itemView.findViewById(R.id.tv_describe);
            tv_message = itemView.findViewById(R.id.tv_message);
            tv_all_show = itemView.findViewById(R.id.tv_all_show);
            tv_comment_count = itemView.findViewById(R.id.tv_comment_count);
            iv_laud = itemView.findViewById(R.id.iv_laud);
            iv_commen = itemView.findViewById(R.id.iv_commen);
            tv_laud_count = itemView.findViewById(R.id.tv_laud_count);
            tv_time = itemView.findViewById(R.id.tv_time);
            nineGridView = itemView.findViewById(R.id.nineGridView);

            ll_commen = itemView.findViewById(R.id.ll_commen);
            tv_loadmore = itemView.findViewById(R.id.tv_loadmore);

            tv_delete = itemView.findViewById(R.id.tv_delete);
            tv_lauds = itemView.findViewById(R.id.tv_lauds);
        }
    }

    class ClickNineGridViewAdapter extends NineGridViewAdapter {

        public ClickNineGridViewAdapter(Context context, List<ImageInfo> imageInfo) {
            super(context, imageInfo);
        }

        @Override
        protected void onImageItemClick(Context context, NineGridView nineGridView, int index, List<ImageInfo> imageInfo) {
            super.onImageItemClick(context, nineGridView, index, imageInfo);
            List<String> urls = new ArrayList<>();
            for (ImageInfo info : imageInfo) {
                urls.add(info.getBigImageUrl());
            }
            mContext.startActivity(new Intent(mContext, PictureActivity.class)
                    .putExtra("index", index)
                    .putStringArrayListExtra("urls", (ArrayList<String>) urls)
            );
        }
    }
}
