package com.vanniktech.emoji;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.util.AttributeSet;

import android.view.View;
import android.widget.EditText;

import androidx.annotation.CallSuper;
import androidx.annotation.DimenRes;
import androidx.annotation.Nullable;
import androidx.annotation.Px;
import androidx.appcompat.widget.AppCompatEditText;

import com.vanniktech.emoji.emoji.Emoji;

/** Reference implementation for an EditText with emoji support. */
@SuppressWarnings("CPD-START") public class EmojiEditText extends AppCompatEditText {
  private float emojiSize;
  private boolean disableKeyboardInput;

  public EmojiEditText(final Context context) {
    this(context, null);
  }

  public EmojiEditText(final Context context, final AttributeSet attrs) {
    super(context, attrs);

    if (!isInEditMode()) {
      EmojiManager.getInstance().verifyInstalled();
    }

    final Paint.FontMetrics fontMetrics = getPaint().getFontMetrics();
    final float defaultEmojiSize = fontMetrics.descent - fontMetrics.ascent;

    if (attrs == null) {
      emojiSize = defaultEmojiSize;
    } else {
      final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.EmojiEditText);

      try {
        emojiSize = a.getDimension(R.styleable.EmojiEditText_emojiSize, defaultEmojiSize);
      } finally {
        a.recycle();
      }
    }

    setText(getText());
  }

  @Override @CallSuper protected void onTextChanged(final CharSequence text, final int start, final int lengthBefore, final int lengthAfter) {
    final Paint.FontMetrics fontMetrics = getPaint().getFontMetrics();
    final float defaultEmojiSize = fontMetrics.descent - fontMetrics.ascent;
    EmojiManager.getInstance().replaceWithImages(getContext(), getText(), emojiSize, defaultEmojiSize);
  }

  @CallSuper public void backspace() {
    Utils.backspace(this);
  }

  @CallSuper public void input(final Emoji emoji) {
    Utils.input(this, emoji);
  }

  /** returns the emoji size */
  public final float getEmojiSize() {
    return emojiSize;
  }

  /** sets the emoji size in pixels and automatically invalidates the text and renders it with the new size */
  public final void setEmojiSize(@Px final int pixels) {
    setEmojiSize(pixels, true);
  }

  /** sets the emoji size in pixels and automatically invalidates the text and renders it with the new size when {@code shouldInvalidate} is true */
  public final void setEmojiSize(@Px final int pixels, final boolean shouldInvalidate) {
    emojiSize = pixels;

    if (shouldInvalidate) {
      setText(getText());
    }
  }

  /** sets the emoji size in pixels with the provided resource and automatically invalidates the text and renders it with the new size */
  public final void setEmojiSizeRes(@DimenRes final int res) {
    setEmojiSizeRes(res, true);
  }

  /** sets the emoji size in pixels with the provided resource and invalidates the text and renders it with the new size when {@code shouldInvalidate} is true */
  public final void setEmojiSizeRes(@DimenRes final int res, final boolean shouldInvalidate) {
    setEmojiSize(getResources().getDimensionPixelSize(res), shouldInvalidate);
  }

  @Override public void setOnFocusChangeListener(final OnFocusChangeListener l) {
    final OnFocusChangeListener onFocusChangeListener = getOnFocusChangeListener();

    if (onFocusChangeListener instanceof ForceEmojisOnlyFocusChangeListener) {
      final ForceEmojisOnlyFocusChangeListener cast = (ForceEmojisOnlyFocusChangeListener) onFocusChangeListener;
      super.setOnFocusChangeListener(new ForceEmojisOnlyFocusChangeListener(l, cast.emojiPopup));
    } else {
      super.setOnFocusChangeListener(l);
    }
  }

  public boolean isKeyboardInputDisabled() {
    return disableKeyboardInput;
  }

  /** Disables the keyboard input using a focus change listener and delegating to the previous focus change listener. */
  public void disableKeyboardInput(final EmojiPopup emojiPopup) {
    disableKeyboardInput = true;
    super.setOnFocusChangeListener(new ForceEmojisOnlyFocusChangeListener(getOnFocusChangeListener(), emojiPopup));
  }

  /** Enables the keyboard input. If it has been disabled before using {@link #disableKeyboardInput(EmojiPopup)} the OnFocusChangeListener will be preserved. */
  public void enableKeyboardInput() {
    disableKeyboardInput = false;
    final OnFocusChangeListener onFocusChangeListener = getOnFocusChangeListener();

    if (onFocusChangeListener instanceof ForceEmojisOnlyFocusChangeListener) {
      final ForceEmojisOnlyFocusChangeListener cast = (ForceEmojisOnlyFocusChangeListener) onFocusChangeListener;
      super.setOnFocusChangeListener(cast.onFocusChangeListener);
    }
  }

  static class ForceEmojisOnlyFocusChangeListener implements OnFocusChangeListener {
    final EmojiPopup emojiPopup;
    @Nullable final OnFocusChangeListener onFocusChangeListener;

    ForceEmojisOnlyFocusChangeListener(@Nullable final OnFocusChangeListener onFocusChangeListener, final EmojiPopup emojiPopup) {
      this.emojiPopup = emojiPopup;
      this.onFocusChangeListener = onFocusChangeListener;
    }

    @Override public void onFocusChange(final View view, final boolean hasFocus) {
      if (hasFocus) {
        emojiPopup.start();
        emojiPopup.show();
      } else {
        emojiPopup.dismiss();
      }

      if (onFocusChangeListener != null) {
        onFocusChangeListener.onFocusChange(view, hasFocus);
      }
    }
  }
}
