package com.sunshinator.ipnosprototype.view;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.widget.Checkable;

/**
 * Found on StackOverflow
 */
public class CheckableImageView
    extends AppCompatImageView
    implements Checkable {

  private static final int[] ATTR_SET_CHECKED = { android.R.attr.state_checked };

  private boolean _checkedState;

  public CheckableImageView( final Context context, final AttributeSet attrs ) {
    super( context, attrs );
  }

  @Override
  public int[] onCreateDrawableState( final int extraSpace ) {
    final int[] drawableState = super.onCreateDrawableState( extraSpace + 1 );
    if ( isChecked() ) {
      mergeDrawableStates( drawableState, ATTR_SET_CHECKED );
    }
    return drawableState;
  }

  @Override
  public void toggle() {
    setChecked( !_checkedState );
  }

  @Override
  public boolean isChecked() {
    return _checkedState;
  }

  @Override
  public void setChecked( final boolean checked ) {
    if ( _checkedState != checked ) {
      _checkedState = checked;
      refreshDrawableState();
    }
  }
}