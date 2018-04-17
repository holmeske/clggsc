package com.sc.clgg.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sc.clgg.R;


public class CLGGDialogBuilder {

    public QDAlertDialog mDialog;
    private Context mContext;
    private View v;

    public CLGGDialogBuilder(Context context) {
        mContext = context;
        mDialog = new QDAlertDialog(context);
        v = LayoutInflater.from(context).inflate(R.layout.clgg_alertdialog, null);
    }

    public CLGGDialogBuilder(Context context, int style) {
        mContext = context;
        mDialog = new QDAlertDialog(context, style);
        v = LayoutInflater.from(context).inflate(R.layout.clgg_alertdialog, null);
    }

    /**
     * 关闭对话框，供外部调用
     */
    public void cancelDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    /**
     * Set the title using the given resource id.
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public CLGGDialogBuilder setTitle(int titleId) {
        TextView view = (TextView) v.findViewById(R.id.title);
        view.setText(titleId);
        view.setVisibility(View.VISIBLE);
        return this;
    }

    /**
     * Set the title displayed in the {@link Dialog}.
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public CLGGDialogBuilder setTitle(CharSequence title) {
        TextView view = (TextView) v.findViewById(R.id.title);
        if (view != null) {
            view.setText(title);
            view.setVisibility(View.VISIBLE);
        }
        return this;
    }

    /**
     * Set the message to display using the given resource id.
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public CLGGDialogBuilder setMessage(int messageId) {
        TextView view = (TextView) v.findViewById(R.id.desc);
        view.setMovementMethod(new ScrollingMovementMethod());
        view.setText(messageId);
        view.setVisibility(View.VISIBLE);
        return this;
    }

    /**
     * Set the message to display.
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public CLGGDialogBuilder setMessage(CharSequence message) {
        TextView view = (TextView) v.findViewById(R.id.desc);
        view.setMovementMethod(new ScrollingMovementMethod());
        view.setText(message);
        view.setVisibility(View.VISIBLE);
        return this;
    }

    /**
     * Set the resource id of the {@link Drawable} to be used in the title.
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public CLGGDialogBuilder setIcon(int iconId) {
        return this;
    }

    /**
     * Set the {@link Drawable} to be used in the title.
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public CLGGDialogBuilder setIcon(Drawable icon) {
        return this;
    }

    /**
     * Set an icon as supplied by a theme attribute. e.g.
     * android.R.attr.alertDialogIcon
     *
     * @param attrId ID of a theme attribute that points to a drawable resource.
     */
    public CLGGDialogBuilder setIconAttribute(int attrId) {
        return this;
    }

    /**
     * Set a listener to be invoked when the positive button of the dialog is
     * pressed.
     *
     * @param textId   The resource id of the text to display in the positive button
     * @param listener The {@link DialogInterface.OnClickListener} to use.
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public CLGGDialogBuilder setPositiveButton(int textId, final OnClickListener listener) {
        View line = v.findViewById(R.id.line);
        line.setVisibility(View.VISIBLE);
        TextView view = (TextView) v.findViewById(R.id.sure);
        view.setText(textId);
        view.setVisibility(View.VISIBLE);
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(mDialog, Dialog.BUTTON_POSITIVE);
                }
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }
            }
        });
        return this;
    }

    /**
     * set positive button bg
     *
     * @param bg
     */
    public CLGGDialogBuilder setPositiveButtonBG(int bg, int textColor) {
        TextView view = (TextView) v.findViewById(R.id.sure);
        view.setTextColor(textColor);
        view.setBackgroundResource(bg);
        return this;
    }

    /**
     * set positive button bg
     *
     * @param bg
     */
    public CLGGDialogBuilder setPositiveButtonBG(Drawable bg, int textColor) {
        TextView view = (TextView) v.findViewById(R.id.sure);
        view.setTextColor(textColor);
        view.setBackgroundDrawable(bg);
        return this;
    }

    /**
     * Set a listener to be invoked when the positive button of the dialog is
     * pressed.
     *
     * @param text     The text to display in the positive button
     * @param listener The {@link DialogInterface.OnClickListener} to use.
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public CLGGDialogBuilder setPositiveButton(CharSequence text, final OnClickListener listener) {
        View line = v.findViewById(R.id.line);
        line.setVisibility(View.VISIBLE);
        TextView view = (TextView) v.findViewById(R.id.sure);
        view.setText(text);
        view.setVisibility(View.VISIBLE);
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(mDialog, Dialog.BUTTON_POSITIVE);
                }
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }
            }
        });
        return this;
    }

    /**
     * Set a listener to be invoked when the negative button of the dialog is
     * pressed.
     *
     * @param textId   The resource id of the text to display in the negative button
     * @param listener The {@link DialogInterface.OnClickListener} to use.
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public CLGGDialogBuilder setNegativeButton(int textId, final OnClickListener listener) {
        View line = v.findViewById(R.id.line);
        line.setVisibility(View.VISIBLE);
        TextView view = (TextView) v.findViewById(R.id.cancel);
        view.setText(textId);
        view.setVisibility(View.VISIBLE);
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(mDialog, Dialog.BUTTON_NEGATIVE);
                }
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }
            }
        });
        return this;
    }

    /**
     * Set a listener to be invoked when the negative button of the dialog is
     * pressed.
     *
     * @param text     The text to display in the negative button
     * @param listener The {@link DialogInterface.OnClickListener} to use.
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public CLGGDialogBuilder setNegativeButton(CharSequence text, final OnClickListener listener) {
        View line = v.findViewById(R.id.line);
        line.setVisibility(View.VISIBLE);
        TextView view = (TextView) v.findViewById(R.id.cancel);
        view.setText(text);
        view.setVisibility(View.VISIBLE);
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(mDialog, Dialog.BUTTON_NEGATIVE);
                }
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }
            }
        });
        return this;
    }

    /**
     * Set a listener to be invoked when the neutral button of the dialog is
     * pressed.
     *
     * @param textId   The resource id of the text to display in the neutral button
     * @param listener The {@link DialogInterface.OnClickListener} to use.
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public CLGGDialogBuilder setNeutralButton(int textId, final OnClickListener listener) {
        View line = v.findViewById(R.id.line);
        line.setVisibility(View.VISIBLE);
        TextView view = (TextView) v.findViewById(R.id.Neutral);
        view.setText(textId);
        view.setVisibility(View.VISIBLE);
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(mDialog, Dialog.BUTTON_NEUTRAL);
                }
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }
            }
        });
        return this;
    }

    /**
     * Set a listener to be invoked when the neutral button of the dialog is
     * pressed.
     *
     * @param text     The text to display in the neutral button
     * @param listener The {@link DialogInterface.OnClickListener} to use.
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public CLGGDialogBuilder setNeutralButton(CharSequence text, final OnClickListener listener) {
        View line = v.findViewById(R.id.line);
        line.setVisibility(View.VISIBLE);
        TextView view = (TextView) v.findViewById(R.id.Neutral);
        view.setText(text);
        view.setVisibility(View.VISIBLE);
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(mDialog, Dialog.BUTTON_NEUTRAL);
                }
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }
            }
        });
        return this;
    }

    /**
     * Sets whether the dialog is cancelable or not. Default is true.
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public CLGGDialogBuilder setCancelable(boolean cancelable) {
        mDialog.setCancelable(cancelable);
        return this;
    }

    /**
     * Sets the callback that will be called if the dialog is canceled.
     *
     * @return This Builder object to allow for chaining of calls to set methods
     * @see #setCancelable(boolean)
     */
    public CLGGDialogBuilder setOnCancelListener(OnCancelListener onCancelListener) {
        mDialog.setOnCancelListener(onCancelListener);
        return this;
    }

    /**
     * Sets the callback that will be called if a key is dispatched to the
     * dialog.
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public CLGGDialogBuilder setOnKeyListener(OnKeyListener onKeyListener) {
        mDialog.setOnKeyListener(onKeyListener);
        return this;
    }

    /**
     * Set a list of items to be displayed in the dialog as the content, you
     * will be notified of the selected item via the supplied listener. This
     * should be an array type i.e. R.array.foo
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public CLGGDialogBuilder setItems(int itemsId, final OnClickListener listener) {
        return this;
    }

    /**
     * Set a list of items to be displayed in the dialog as the content, you
     * will be notified of the selected item via the supplied listener.
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public CLGGDialogBuilder setItems(CharSequence[] items, final OnClickListener listener) {
        Adapter adapter = new Adapter();
        adapter.checkedPosition = -1;
        adapter.items = items;
        adapter.showCheckbox = false;
        ListView lv = (ListView) v.findViewById(R.id.listview);
        if (items.length > 5) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) lv.getLayoutParams();
            lp.height = dip2px(mContext, 45) * 5 + 10;
            lv.setLayoutParams(lp);
        }
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listener.onClick(mDialog, position);
            }
        });
        lv.setVisibility(View.VISIBLE);
        return this;
    }

    public CLGGDialogBuilder setItems(String[] items, final OnClickListener listener) {
        Adapter adapter = new Adapter();
        adapter.checkedPosition = -1;
        adapter.items = items;
        adapter.showCheckbox = false;
        ListView lv = (ListView) v.findViewById(R.id.listview);
        if (items.length > 5) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) lv.getLayoutParams();
            lp.height = dip2px(mContext, 45) * 5 + 10;
            lv.setLayoutParams(lp);
        }
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listener.onClick(mDialog, position);
            }
        });
        lv.setVisibility(View.VISIBLE);
        return this;
    }

    /**
     * Set a list of items, which are supplied by the given {@link ListAdapter},
     * to be displayed in the dialog as the content, you will be notified of the
     * selected item via the supplied listener.
     *
     * @param adapter  The {@link ListAdapter} to supply the list of items
     * @param listener The listener that will be called when an item is clicked.
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public CLGGDialogBuilder setAdapter(final ListAdapter adapter, final OnClickListener listener) {
        return this;
    }

    /**
     * Set a list of items, which are supplied by the given {@link Cursor}, to
     * be displayed in the dialog as the content, you will be notified of the
     * selected item via the supplied listener.
     *
     * @param cursor      The {@link Cursor} to supply the list of items
     * @param listener    The listener that will be called when an item is clicked.
     * @param labelColumn The column name on the cursor containing the string to display
     *                    in the label.
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public CLGGDialogBuilder setCursor(final Cursor cursor, final OnClickListener listener, String labelColumn) {
        return this;
    }

    /**
     * Set a list of items to be displayed in the dialog as the content, you
     * will be notified of the selected item via the supplied listener. This
     * should be an array type, e.g. R.array.foo. The list will have a check
     * mark displayed to the right of the text for each checked item. Clicking
     * on an item in the list will not dismiss the dialog. Clicking on a button
     * will dismiss the dialog.
     *
     * @param itemsId      the resource id of an array i.e. R.array.foo
     * @param checkedItems specifies which items are checked. It should be null in which
     *                     case no items are checked. If non null it must be exactly the
     *                     same length as the array of items.
     * @param listener     notified when an item on the list is clicked. The dialog will
     *                     not be dismissed when an item is clicked. It will only be
     *                     dismissed if clicked on a button, if no buttons are supplied
     *                     it's up to the user to dismiss the dialog.
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public CLGGDialogBuilder setMultiChoiceItems(int itemsId, boolean[] checkedItems, final OnMultiChoiceClickListener listener) {
        return this;
    }

    /**
     * Set a list of items to be displayed in the dialog as the content, you
     * will be notified of the selected item via the supplied listener. The list
     * will have a check mark displayed to the right of the text for each
     * checked item. Clicking on an item in the list will not dismiss the
     * dialog. Clicking on a button will dismiss the dialog.
     *
     * @param items        the text of the items to be displayed in the list.
     * @param checkedItems specifies which items are checked. It should be null in which
     *                     case no items are checked. If non null it must be exactly the
     *                     same length as the array of items.
     * @param listener     notified when an item on the list is clicked. The dialog will
     *                     not be dismissed when an item is clicked. It will only be
     *                     dismissed if clicked on a button, if no buttons are supplied
     *                     it's up to the user to dismiss the dialog.
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public CLGGDialogBuilder setMultiChoiceItems(CharSequence[] items, boolean[] checkedItems, final OnMultiChoiceClickListener listener) {
        return this;
    }

    /**
     * Set a list of items to be displayed in the dialog as the content, you
     * will be notified of the selected item via the supplied listener. The list
     * will have a check mark displayed to the right of the text for each
     * checked item. Clicking on an item in the list will not dismiss the
     * dialog. Clicking on a button will dismiss the dialog.
     *
     * @param cursor          the cursor used to provide the items.
     * @param isCheckedColumn specifies the column name on the cursor to use to determine
     *                        whether a checkbox is checked or not. It must return an
     *                        integer value where 1 means checked and 0 means unchecked.
     * @param labelColumn     The column name on the cursor containing the string to display
     *                        in the label.
     * @param listener        notified when an item on the list is clicked. The dialog will
     *                        not be dismissed when an item is clicked. It will only be
     *                        dismissed if clicked on a button, if no buttons are supplied
     *                        it's up to the user to dismiss the dialog.
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public CLGGDialogBuilder setMultiChoiceItems(Cursor cursor, String isCheckedColumn, String labelColumn, final OnMultiChoiceClickListener listener) {
        return this;
    }

    /**
     * Set a list of items to be displayed in the dialog as the content, you
     * will be notified of the selected item via the supplied listener. This
     * should be an array type i.e. R.array.foo The list will have a check mark
     * displayed to the right of the text for the checked item. Clicking on an
     * item in the list will not dismiss the dialog. Clicking on a button will
     * dismiss the dialog.
     *
     * @param itemsId     the resource id of an array i.e. R.array.foo
     * @param checkedItem specifies which item is checked. If -1 no items are checked.
     * @param listener    notified when an item on the list is clicked. The dialog will
     *                    not be dismissed when an item is clicked. It will only be
     *                    dismissed if clicked on a button, if no buttons are supplied
     *                    it's up to the user to dismiss the dialog.
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public CLGGDialogBuilder setSingleChoiceItems(int itemsId, int checkedItem, final OnClickListener listener) {
        CharSequence[] items = mContext.getResources().getTextArray(itemsId);
        final Adapter adapter = new Adapter();
        adapter.checkedPosition = checkedItem;
        adapter.items = items;
        adapter.showCheckbox = true;
        ListView lv = (ListView) v.findViewById(R.id.listview);
        if (items.length > 5) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) lv.getLayoutParams();
            lp.height = dip2px(mContext, 45) * 5 + 10;
            lv.setLayoutParams(lp);
        }
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.checkedPosition = position;
                listener.onClick(mDialog, position);
            }
        });
        lv.setVisibility(View.VISIBLE);
        return this;
    }

    /**
     * Set a list of items to be displayed in the dialog as the content, you
     * will be notified of the selected item via the supplied listener. The list
     * will have a check mark displayed to the right of the text for the checked
     * item. Clicking on an item in the list will not dismiss the dialog.
     * Clicking on a button will dismiss the dialog.
     *
     * @param cursor      the cursor to retrieve the items from.
     * @param checkedItem specifies which item is checked. If -1 no items are checked.
     * @param labelColumn The column name on the cursor containing the string to display
     *                    in the label.
     * @param listener    notified when an item on the list is clicked. The dialog will
     *                    not be dismissed when an item is clicked. It will only be
     *                    dismissed if clicked on a button, if no buttons are supplied
     *                    it's up to the user to dismiss the dialog.
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public CLGGDialogBuilder setSingleChoiceItems(Cursor cursor, int checkedItem, String labelColumn, final OnClickListener listener) {
        return this;
    }

    /**
     * Set a list of items to be displayed in the dialog as the content, you
     * will be notified of the selected item via the supplied listener. The list
     * will have a check mark displayed to the right of the text for the checked
     * item. Clicking on an item in the list will not dismiss the dialog.
     * Clicking on a button will dismiss the dialog.
     *
     * @param items       the items to be displayed.
     * @param checkedItem specifies which item is checked. If -1 no items are checked.
     * @param listener    notified when an item on the list is clicked. The dialog will
     *                    not be dismissed when an item is clicked. It will only be
     *                    dismissed if clicked on a button, if no buttons are supplied
     *                    it's up to the user to dismiss the dialog.
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public CLGGDialogBuilder setSingleChoiceItems(CharSequence[] items, int checkedItem, final OnClickListener listener) {
        final Adapter adapter = new Adapter();
        adapter.checkedPosition = checkedItem;
        adapter.items = items;
        adapter.showCheckbox = true;
        ListView lv = (ListView) v.findViewById(R.id.listview);
        if (items.length > 5) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) lv.getLayoutParams();
            lp.height = dip2px(mContext, 45) * 5 + 10;
            lv.setLayoutParams(lp);
        }
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.checkedPosition = position;
                listener.onClick(mDialog, position);
            }
        });
        lv.setVisibility(View.VISIBLE);
        return this;
    }

    private int dip2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * Set a list of items to be displayed in the dialog as the content, you
     * will be notified of the selected item via the supplied listener. The list
     * will have a check mark displayed to the right of the text for the checked
     * item. Clicking on an item in the list will not dismiss the dialog.
     * Clicking on a button will dismiss the dialog.
     *
     * @param adapter     The {@link ListAdapter} to supply the list of items
     * @param checkedItem specifies which item is checked. If -1 no items are checked.
     * @param listener    notified when an item on the list is clicked. The dialog will
     *                    not be dismissed when an item is clicked. It will only be
     *                    dismissed if clicked on a button, if no buttons are supplied
     *                    it's up to the user to dismiss the dialog.
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public CLGGDialogBuilder setSingleChoiceItems(ListAdapter adapter, int checkedItem, final OnClickListener listener) {
        return this;
    }

    /**
     * Sets a listener to be invoked when an item in the list is selected.
     *
     * @param listener The listener to be invoked.
     * @return This Builder object to allow for chaining of calls to set methods
     * @see AdapterView#setOnItemSelectedListener(android.widget.AdapterView.OnItemSelectedListener)
     */
    public CLGGDialogBuilder setOnItemSelectedListener(final AdapterView.OnItemSelectedListener listener) {
        return this;
    }

    /**
     * Set a custom view to be the contents of the Dialog. If the supplied view
     * is an instance of a {@link ListView} the light background will be used.
     *
     * @param view The view to use as the contents of the Dialog.
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public CLGGDialogBuilder setView(View view) {
        setView(view, (int) mContext.getResources().getDimension(R.dimen.length_20), (int) mContext.getResources().getDimension(R.dimen.length_20));
        return this;
    }

    /***
     * 没有默认边距的
     *
     * @param view
     * @return
     */
    public CLGGDialogBuilder setViewNoPadding(View view) {
        setView(view, 0, 0);
        return this;
    }

    public CLGGDialogBuilder setView(View view, int left, int right) {
        ViewGroup viewG = (ViewGroup) v.findViewById(R.id.custom_view);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = left;
        lp.rightMargin = right;
        viewG.addView(view, lp);
        viewG.setVisibility(View.VISIBLE);
        return this;
    }

    /**
     * Set a custom view to be the contents of the Dialog, specifying the
     * spacing to appear around that view. If the supplied view is an instance
     * of a {@link ListView} the light background will be used.
     *
     * @param view              The view to use as the contents of the Dialog.
     * @param viewSpacingLeft   Spacing between the left edge of the view and the dialog frame
     * @param viewSpacingTop    Spacing between the top edge of the view and the dialog frame
     * @param viewSpacingRight  Spacing between the right edge of the view and the dialog
     *                          frame
     * @param viewSpacingBottom Spacing between the bottom edge of the view and the dialog
     *                          frame
     * @return This Builder object to allow for chaining of calls to set methods
     * <p>
     * <p>
     * This is currently hidden because it seems like people should just
     * be able to put padding around the view.
     * @hide
     */
    public CLGGDialogBuilder setView(View view, int viewSpacingLeft, int viewSpacingTop, int viewSpacingRight, int viewSpacingBottom) {
        RelativeLayout viewG = (RelativeLayout) v.findViewById(R.id.layoutContainer);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        viewG.addView(view, lp);
        viewG.setVisibility(View.VISIBLE);
        return this;
    }

    /**
     * Sets the Dialog to use the inverse background, regardless of what the
     * contents is.
     *
     * @param useInverseBackground Whether to use the inverse background
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public CLGGDialogBuilder setInverseBackgroundForced(boolean useInverseBackground) {
        return this;
    }

    public CLGGDialogBuilder create() {
        return this;
    }

    /**
     * Creates a {@link AlertDialog} with the arguments supplied to this builder
     * and {@link Dialog#show()}'s the dialog.
     */
    public CLGGDialogBuilder show() {
        TextView view1 = (TextView) v.findViewById(R.id.desc);
        TextView view2 = (TextView) v.findViewById(R.id.title);
        if (view1.getVisibility() == View.VISIBLE && view2.getVisibility() == View.GONE) {
            View topmargin = v.findViewById(R.id.topmargin);
            topmargin.setVisibility(View.VISIBLE);
        }
        mDialog.show();
        return this;
    }

    private class Adapter extends BaseAdapter {

        CharSequence[] items;
        int checkedPosition = -1;
        boolean showCheckbox = false;

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int position) {
            return items[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            if (convertView == null) {
                holder = new Holder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_listview, null);
                holder.title = (TextView) convertView.findViewById(R.id.title);
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
                holder.checkBox.setClickable(false);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            if (showCheckbox) {
                holder.checkBox.setVisibility(View.VISIBLE);
            } else {
                holder.checkBox.setVisibility(View.GONE);
            }
            holder.title.setText(items[position]);
            if (checkedPosition == position) {
                holder.checkBox.setChecked(true);
            } else {
                holder.checkBox.setChecked(false);
            }
            return convertView;
        }
    }

    private class Holder {
        TextView title;
        CheckBox checkBox;
    }

    public class QDAlertDialog extends AlertDialog {

        protected QDAlertDialog(Context context) {
            super(context);
        }

        protected QDAlertDialog(Context context, int style) {
            super(context, style);
//			super(context);
             /*WindowManager.LayoutParams lp=getWindow().getAttributes();
             lp.alpha=1.0f;
	         getWindow().setAttributes(lp);*/
        }

        public void show() {
            try {
                if (getContext() instanceof Activity) {
                    if (((Activity) getContext()).isFinishing()) {
                        return;
                    }
                }
                super.show();
                getWindow().setContentView(v);
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                getWindow().setAttributes(lp);
                getWindow().setGravity(Gravity.BOTTOM);
                getWindow().setBackgroundDrawable(null);
                getWindow().setWindowAnimations(R.style.dialog_show_anim);

                // 不加这句，软键盘弹不出来
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void setCanceledOnTouchOutside(boolean b) {
        mDialog.setCanceledOnTouchOutside(b);
    }

    public boolean isShowing() {
        return mDialog.isShowing();
    }

    public void dismiss() {
        if (mDialog != null) {
            try {
                mDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
