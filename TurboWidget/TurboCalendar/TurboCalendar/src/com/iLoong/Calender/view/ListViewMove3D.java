package com.iLoong.Calender.view;


import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquation;
import aurelienribon.tweenengine.equations.Cubic;
import aurelienribon.tweenengine.equations.Linear;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.iLoong.Calender.common.Parameter;
import com.iLoong.launcher.Desktop3D.R3D;
import com.iLoong.launcher.UI3DEngine.Utils3D;
import com.iLoong.launcher.UI3DEngine.View3D;
import com.iLoong.launcher.UI3DEngine.ViewGroup3D;
import com.iLoong.launcher.tween.View3DTweenAccessor;


public class ListViewMove3D extends ViewGroup3D
{
	
	public static float BOUNCE_DISTANCE = 30;
	public static float DRAG_BRAKE = 10;
	private float mVelocityY = 0;
	private boolean isMove = false;
	private boolean isManualScrollTo = false;
	private boolean canScroll = false;
	private float scrollY = 0;//向上滑动距离
	private float downY = 0;
	private float downScrollY = 0;
	private float totalChildHeight = 0;
	private int firstVisibleIndex = -1;
	private int lastVisibleIndex = -1;
	private Timeline timeline = null;
	public float paddingBottom = 0;
	private float density = 1;
	public ListViewMove3D(
			String name )
	{
		super( name );
		density = Gdx.graphics.getDensity();
		BOUNCE_DISTANCE *= density;
		DRAG_BRAKE *= density;
	}
	
	public void addItem(
			View3D view )
	{
		addView( view );
		layout();
	}
	
	public void removeItem(
			View3D view )
	{
		removeView( view );
		layout();
	}
	
	private void layout()
	{
		totalChildHeight = 0;
		View3D cur_view;
		int size = getChildCount();
		float mPosy = 0;
		float curHeight = this.height - paddingBottom;
		for( int i = 0 ; i < size ; i++ )
		{
			cur_view = getChildAt( i );
			if( !cur_view.visible )
				continue;
			curHeight -= cur_view.height;
			mPosy = curHeight;
			cur_view.setPosition( 0 , mPosy );
			totalChildHeight += cur_view.height;
		}
		refreshVisibleChildren( scrollY );
	}
	
	private void refreshVisibleChildren(
			float scrollY )
	{
		firstVisibleIndex = -1;
		lastVisibleIndex = -1;
		if( totalChildHeight < this.height - paddingBottom || scrollY < ( totalChildHeight - ( this.height - paddingBottom ) ) / 2 )
		{
			View3D cur_view;
			int size = getChildCount();
			for( int i = 0 ; i < size && ( firstVisibleIndex == -1 || lastVisibleIndex == -1 ) ; i++ )
			{
				cur_view = getChildAt( i );
				if( !cur_view.visible )
					continue;
				if( inVisibleRegion( cur_view , scrollY ) )
				{
					if( firstVisibleIndex == -1 )
						firstVisibleIndex = i;
					else if( i == size - 1 )
						lastVisibleIndex = i;
				}
				else if( firstVisibleIndex != -1 )
					lastVisibleIndex = i - 1;
			}
			if( lastVisibleIndex == -1 )
				lastVisibleIndex = firstVisibleIndex;
		}
		else
		{
			View3D cur_view;
			int size = getChildCount();
			for( int i = size - 1 ; i >= 0 && ( firstVisibleIndex == -1 || lastVisibleIndex == -1 ) ; i-- )
			{
				cur_view = getChildAt( i );
				if( !cur_view.visible )
					continue;
				if( inVisibleRegion( cur_view , scrollY ) )
				{
					if( lastVisibleIndex == -1 )
						lastVisibleIndex = i;
					else if( i == 0 )
						firstVisibleIndex = i;
				}
				else if( lastVisibleIndex != -1 )
					firstVisibleIndex = i + 1;
			}
			if( firstVisibleIndex == -1 )
				firstVisibleIndex = lastVisibleIndex;
		}
		//Log.d("ListView3D", "first,last="+firstVisibleIndex+","+lastVisibleIndex);
	}
	
	private boolean inVisibleRegion(
			View3D view ,
			float scrollY )
	{
		if( view.y + scrollY > this.height - paddingBottom )
			return false;
		if( view.y + scrollY + view.height < 0 )
			return false;
		return true;
	}
	
	private void startAutoEffect()
	{
		float speed = 0.3f;
		isManualScrollTo = false;
		if( ( scrollY >= 0 && scrollY <= getScrollMax() ) && mVelocityY == 0 )
			return;
		timeline = Timeline.createSequence();
		if( scrollY < 0 || scrollY > getScrollMax() )
			mVelocityY = 0;
		float destScrollY = scrollY - mVelocityY;
		if( destScrollY < -BOUNCE_DISTANCE )
		{
			destScrollY = -BOUNCE_DISTANCE;
			if( mVelocityY != 0 )
				destScrollY /= 2;
		}
		else if( destScrollY > getScrollMax() + BOUNCE_DISTANCE )
		{
			if( mVelocityY != 0 )
				destScrollY = getScrollMax() + BOUNCE_DISTANCE / 2;
			else
				destScrollY = getScrollMax() + BOUNCE_DISTANCE;
		}
		if( destScrollY != scrollY )
		{
			TweenEquation equ = null;
			//if(destScrollY<0 || destScrollY>getScrollMax())equ = Linear.INOUT;
			equ = Cubic.OUT;
			speed *= Math.abs( destScrollY - scrollY ) / ( 300 * density );
//			Log.d( "ListView" , "speed=" + speed );
			if( speed > 10f )
				speed = 10f;
			if( speed < 0.8f && ( destScrollY > 0 && destScrollY < getScrollMax() ) )
				speed = 0.8f;
			else if( scrollY < 300 * density || scrollY > getScrollMax() - 300 * density )
				speed *= 2;
			timeline.push( Tween.to( this , View3DTweenAccessor.USER , speed ).ease( equ ).target( destScrollY ) );
		}
		speed = 1.5f;
//		Log.d( "ListView" , "speed=" + speed );
		//if(speed>10f)speed = 10f;
		//if(speed<0.8f)speed = 0.8f;
		if( destScrollY < 0 )
		{
			speed *= Math.abs( destScrollY ) / ( 250 * density );
			timeline.push( Tween.to( this , View3DTweenAccessor.USER , speed ).ease( Linear.INOUT ).target( 0 ) );
		}
		if( destScrollY > getScrollMax() )
		{
			speed *= Math.abs( destScrollY - getScrollMax() ) / ( 250 * density );
			timeline.push( Tween.to( this , View3DTweenAccessor.USER , speed ).ease( Linear.INOUT ).target( getScrollMax() ) );
		}
		timeline.start( View3DTweenAccessor.manager ).setCallback( this );
		mVelocityY = 0;
	}
	
	private void stopAutoEffect()
	{
		if( timeline != null && !timeline.isFinished() )
		{
			timeline.free();
			timeline = null;
		}
	}
	
	private boolean isAnimating()
	{
		if( timeline != null && !timeline.isFinished() )
			return true;
		return false;
	}
	
	@Override
	public void setUser(
			float value )
	{
		scrollY = value;
		refreshVisibleChildren( scrollY );
	}
	
	@Override
	public float getUser()
	{
		return scrollY;
	}
	
	private float getScrollMax()
	{
		float scrollMax = totalChildHeight - this.height + paddingBottom;
		if( scrollMax < 0 )
			scrollMax = 0;
		return scrollMax;
	}
	
//	float firstHeight = ( Parameter.Origin_To_Origin_Height + Parameter.Origin_To_Sun_Height - Parameter.Calender_Day_Glass_Height / 2 - Parameter.Calender_Week_Glass_Height / 2 - Parameter.Calender_Day_Glass_Height * 5 ) * WidgetCalender.scale - ( WidgetCalender.scale - WidgetCalender.height_scale ) / 2 * 100f - Parameter.Calender_Day_Glass_Height * WidgetCalender.height_scale / 2 - 20f * WidgetCalender.scale;
	
	@Override
	public void draw(
			SpriteBatch batch ,
			float parentAlpha )
	{
		Gdx.gl.glEnable( GL10.GL_SCISSOR_TEST );
		Gdx.gl.glScissor(
				0 ,
				(int)( Utils3D.getScreenHeight() - R3D.Workspace_cell_each_height * 4 + WidgetCalender.firstHeight ) ,
				Utils3D.getScreenWidth() ,
				(int)( Parameter.Calender_Day_Glass_Height * WidgetCalender.height_scale * 5 ) );
		super.draw( batch , parentAlpha );
		Gdx.gl.glDisable( GL10.GL_SCISSOR_TEST );
	}
	
	@Override
	protected void drawChildren(
			SpriteBatch batch ,
			float parentAlpha )
	{
		parentAlpha *= color.a;
		if( transform )
		{
			for( int i = 0 ; i < children.size() ; i++ )
			{
				View3D child = children.get( i );
				if( !child.visible )
					continue;
				if( child instanceof ViewGroup3D )
				{
					if( child.background9 != null )
					{
						if( child.is3dRotation() )
							child.applyTransformChild( batch );
						batch.setColor( child.color.r , child.color.g , child.color.b , child.color.a * parentAlpha );
						child.background9.draw( batch , child.x , child.y , child.width , child.height );
						if( child.is3dRotation() )
							child.resetTransformChild( batch );
					}
					child.draw( batch , parentAlpha );
					continue;
				}
				if( child.is3dRotation() )
					child.applyTransformChild( batch );
				if( child.background9 != null )
				{
					batch.setColor( child.color.r , child.color.g , child.color.b , child.color.a * parentAlpha );
					child.background9.draw( batch , child.x , child.y , child.width , child.height );
				}
				child.draw( batch , parentAlpha );
				if( child.is3dRotation() )
					child.resetTransformChild( batch );
			}
			//			batch.flush();
		}
		else
		{
			for( int i = firstVisibleIndex ; i < children.size() && i >= 0 && i <= lastVisibleIndex ; i++ )
			{
				View3D child = children.get( i );
				if( !child.visible )
					continue;
				child.x += x;
				child.y += y + scrollY + paddingBottom;
				if( child.background9 != null )
				{
					batch.setColor( child.color.r , child.color.g , child.color.b , child.color.a * parentAlpha );
					child.background9.draw( batch , child.x , child.y , child.width , child.height );
				}
				child.draw( batch , parentAlpha );
				child.x -= x;
				child.y -= y + scrollY + paddingBottom;
			}
		}
	}
	
	boolean ifworkspacemove = false;
	
	@Override
	public boolean onTouchDragged(
			float x ,
			float y ,
			int pointer )
	{
		if( pointer != 0 )
			return true;
		//if(Math.abs(y-downY)<DRAG_BRAKE)return true;
		if( !isMove )
		{
			if( ifworkspacemove )
			{
				return true;
			}
			if( Math.abs( y - recordY ) < Math.abs( x - recordX ) )
			{
				ifworkspacemove = true;
				isMove = false;
				releaseFocus();
				return true;
			}
			else
			{
				ifworkspacemove = false;
				isMove = true;
				downY = y;
				downScrollY = scrollY;
			}
		}
		if( !canScroll )
			return false;
		if( getChildCount() <= 0 )
			return true;
		if( isManualScrollTo )
		{
			return true;
		}
		float newScrollY = y - downY + downScrollY;
		if( newScrollY < 0 )
			newScrollY = newScrollY / 2;
		else if( newScrollY > getScrollMax() )
			newScrollY = getScrollMax() + ( newScrollY - getScrollMax() ) / 2;
		if( newScrollY < -BOUNCE_DISTANCE )
			newScrollY = -BOUNCE_DISTANCE;
		else if( newScrollY > getScrollMax() + BOUNCE_DISTANCE )
			newScrollY = getScrollMax() + BOUNCE_DISTANCE;
		refreshVisibleChildren( newScrollY );
		this.scrollY = newScrollY;
//		Log.d( "ListView3D" , "velocity:" + mVelocityY );
		return true;
	}
	
	float recordX = 0;;
	float recordY = 0;
	
	@Override
	public boolean onTouchDown(
			float x ,
			float y ,
			int pointer )
	{
		recordX = x;
		recordY = y;
		if( pointer != 0 )
			return true;
		canScroll = true;
		isMove = false;
		ifworkspacemove = false;
		if( getChildCount() <= 0 )
			return true;
		if( isManualScrollTo )
		{
			return true;
		}
		downY = y;
		downScrollY = scrollY;
		mVelocityY = 0;
		stopAutoEffect();
		isManualScrollTo = false;
//		WidgetCalender.getIntance().releaseFocus();
		requestFocus();
		//indicatorView.stopTween();
		return super.onTouchDown( x , y , pointer );
	}
	
	@Override
	public boolean onTouchUp(
			float x ,
			float y ,
			int pointer )
	{
		if( pointer != 0 )
			return true;
		canScroll = false;
		if( getChildCount() <= 0 )
			return super.onTouchUp( x , y , pointer );
		if( isManualScrollTo && isAnimating() )
		{
			return true;
		}
		//indicatorView.stopTween();
		if( isMove )
			startAutoEffect();
		releaseFocus();
//		WidgetCalender.getIntance().requestFocus();
		return super.onTouchUp( x , y , pointer );
	}
	
	@Override
	public boolean fling(
			float velocityX ,
			float velocityY )
	{
		if( getChildCount() <= 0 )
			return false;
//		Log.d( "ListView3D" , "veloX,Y=" + velocityX + "," + velocityY );
		if( isMove )
		{
			mVelocityY = velocityY;
			if( mVelocityY < 300 * density && mVelocityY > -300 * density )
			{
				mVelocityY /= 2;
			}
		}
		return true;
	}
	
	@Override
	public boolean onClick(
			float x ,
			float y )
	{
		if( isAnimating() )
			return true;
		return super.onClick( x , y - scrollY - paddingBottom );
	}
	
	@Override
	public boolean onLongClick(
			float x ,
			float y )
	{
		if( isAnimating() )
		{
			return true;
		}else{
			this.point.x = x;
			this.point.y = y;
			this.toAbsolute( point );
			Vector2 obj = new Vector2( point.x , point.y );
			this.setTag( obj );
			this.releaseFocus();
			return viewParent.onCtrlEvent( this , 0 );
		}
//		return super.onLongClick( x , y - scrollY - paddingBottom );
	}
	
	public void removeAllViews()
	{
		super.removeAllViews();
		mVelocityY = 0;
		isManualScrollTo = false;
		canScroll = false;
		scrollY = 0;//向上滑动距离
		downY = 0;
		totalChildHeight = 0;
		firstVisibleIndex = -1;
		lastVisibleIndex = -1;
	}
}
