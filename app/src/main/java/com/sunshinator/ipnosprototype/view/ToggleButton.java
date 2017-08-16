package com.sunshinator.ipnosprototype.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.AnimRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sunshinator.ipnosprototype.R;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Label with an icon
 * <p>
 * Created by The Sunshinator on 25/10/2016.
 */

public class ToggleButton
    extends LinearLayout {

  public static final int[] ATTR_SET_DRAWABLE = {
      android.R.attr.drawableTop,
      android.R.attr.drawableBottom,
      android.R.attr.drawableStart,
      android.R.attr.drawableEnd,
      android.R.attr.drawableLeft,
      android.R.attr.drawableRight,
      };

  public static final int[] ATTR_SET_OTHER = {
      android.R.attr.text,
      android.R.attr.textSize,
      android.R.attr.checked
  };

  private CheckableImageView _icon;
  private TextView           _label;

  public ToggleButton( Context context ) {

    super( context );
    initializeViews( context, null );

  }

  public ToggleButton( Context context, AttributeSet attrs ) {

    super( context, attrs );
    initializeViews( context, attrs );

  }

  public ToggleButton( Context context, AttributeSet attrs, int defStyleAttr ) {

    super( context, attrs, defStyleAttr );
    initializeViews( context, attrs );

  }

  private void initializeViews( Context context, @Nullable AttributeSet attrs ) {

    LayoutInflater.from( context ).inflate( R.layout.compound_toggle_button, this );

    _icon = findViewById( R.id.icon );
    _label = findViewById( R.id.label );

    setCheckDrawable( attrs );
    setOtherAttributesToSubViews( attrs );
  }

  private void setCheckDrawable( AttributeSet attrs ) {
    TypedArray attributeValues = getContext().obtainStyledAttributes( attrs, ATTR_SET_DRAWABLE );

    int      index = 0;
    Drawable src   = null;

    // Search first found drawable attribute in set
    for ( ; index < ATTR_SET_DRAWABLE.length; index++ ) {
      src = attributeValues.getDrawable( index );

      if ( src != null ) {
        break;
      }
    }

    // The default layout is the image first and then the text.
    // To have a right/bottom image, view need to be reversed.
    if ( index % 2 == 1 ) {
      reverseChildViewsOrder();
    }

    // Layout for drawable on top or bottom are vertical, others are horizontal
    setOrientation( ( 1 < index && index < ATTR_SET_DRAWABLE.length )? HORIZONTAL : VERTICAL );

    attributeValues.recycle();

    _icon.setImageDrawable( src );
  }

  @SuppressLint( "ResourceType" )
  private void setOtherAttributesToSubViews( AttributeSet attrs ) {

    TypedArray attributeValues = getContext().obtainStyledAttributes( attrs, ATTR_SET_OTHER );

    _label.setText( attributeValues.getText( 0 ) );
    _label.setTextSize( attributeValues.getDimensionPixelSize( 1, 12 ) );
    _icon.setChecked( attributeValues.getBoolean( 2, false ) );

    attributeValues.recycle();
  }

  private void reverseChildViewsOrder() {
    List<View> views = new ArrayList<>();

    for ( int i = 0; i < getChildCount(); i++ ) {
      views.add( getChildAt( i ) );
    }

    removeAllViews();

    Collections.reverse( views );

    for ( int i = 0; i < views.size(); i++ ) {
      addView( views.get( i ) );
    }
  }

  public void setChecked( boolean isChecked ) {
    _icon.setChecked( isChecked );
  }

  public boolean isChecked() {
    return _icon.isChecked();
  }

  public void setText( @StringRes int res ) {
    _label.setText( res );
  }

  public void setIcon( @DrawableRes int res ) {
    _icon.setImageResource( res );
  }

  public void setTextColor( int color ) {
    _label.setTextColor( color );
  }

  public void startResourceAnimation( @AnimRes int animation ) {

    final Animation anim = AnimationUtils.loadAnimation( getContext(), animation );

    anim.setAnimationListener( new AnimationListener() {
      @Override
      public void onAnimationStart( Animation animation ) {}

      @Override
      public void onAnimationEnd( Animation animation ) {
        _icon.clearAnimation();
        _icon.startAnimation( anim );
      }

      @Override
      public void onAnimationRepeat( Animation animation ) {}
    } );

    //Start animation
    _icon.startAnimation( anim );
  }

  public void stopResourceAnimation() {

    Animation animation = _icon.getAnimation();

    if ( animation != null ) {
      animation.setAnimationListener( null );
      animation.cancel();
    }

    _icon.clearAnimation();
  }
}
