package com.afollestad.aesthetic;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import rx.Subscription;
import rx.functions.Action1;

import static com.afollestad.aesthetic.Rx.onErrorLogAndRethrow;

/** @author Aidan Follestad (afollestad) */
public class AestheticScrollView extends ScrollView {

  private Subscription subscription;

  public AestheticScrollView(Context context) {
    super(context);
  }

  public AestheticScrollView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public AestheticScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  private void invalidateColors(int color) {
    EdgeGlowUtil.setEdgeGlowColor(this, color);
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    subscription =
        Aesthetic.get()
            .colorAccent()
            .compose(Rx.<Integer>distinctToMainThread())
            .subscribe(
                new Action1<Integer>() {
                  @Override
                  public void call(Integer color) {
                    invalidateColors(color);
                  }
                },
                onErrorLogAndRethrow());
  }

  @Override
  protected void onDetachedFromWindow() {
    subscription.unsubscribe();
    super.onDetachedFromWindow();
  }
}
