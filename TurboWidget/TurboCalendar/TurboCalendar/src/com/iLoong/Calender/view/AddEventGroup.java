package com.iLoong.Calender.view;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.iLoong.Calender.common.CalendarAllEvents;
import com.iLoong.Calender.common.CalenderHelper;
import com.iLoong.Calender.common.Parameter;
import com.iLoong.launcher.UI3DEngine.BitmapTexture;
import com.iLoong.launcher.UI3DEngine.Utils3D;
import com.iLoong.launcher.UI3DEngine.ViewGroup3D;
import com.iLoong.launcher.Widget3D.MainAppContext;


public class AddEventGroup extends ViewGroup3D
{
	
	private MainAppContext maincontext = null;
	private Context context = null;
	private  float OrigonToBigXianKuangWidth;
	private  float OrigonToBigXianKuangHeight;
	public static  float OrigonToSmallXianKuangWidth;
	//private final float OrigonToSmallXianKuangHeight = Parameter.Origin_To_Origin_Height + Parameter.Calender_Little_BianKuang_Height;
	private float nowHeight;
	private float nowWidth;
	private float BigMoveOff;
	//private final float LittleMoveOff = ( WidgetCalender.scale - WidgetCalender.height_scale ) / 2 * Parameter.BianKuang_Height;
	//private final float moveDistance = Parameter.BianKuang_Height + Parameter.BianKuang_To_Top;
	public static float kuangoffset = 1.0f;
	public static float moveHeight;
	private float addscale = WidgetCalender.scale;
	private float addHeight_scale = WidgetCalender.height_scale;
	public static int year;
	public static int month;
	public static int day;
	public AddEventGroup(
			String name ,
			MainAppContext maincontext ,
			final int year ,
			final int month ,
			final int day ,
			Context context )
	{
		super( name );
		this.maincontext = maincontext;
		this.context = context;
		AddEventGroup.year = year;
		AddEventGroup.month = month;
		AddEventGroup.day = day;
		addscale = WidgetCalender.scale;
		addHeight_scale = WidgetCalender.height_scale;
		nowHeight = Parameter.Calender_Day_Glass_Height * addHeight_scale * 5;
		nowWidth = Parameter.Calender_Day_Glass_Width * addscale * 7;
		
		BigMoveOff = ( WidgetCalender.scale - WidgetCalender.height_scale ) / 2 * Parameter.BigBackgroundHeight;
		
		kuangoffset = ( nowHeight - 142f ) / ( 112 * 4f );
//		moveHeight = ( Parameter.Calender_Workspace_Bottom + Parameter.Calender_Bottom_JinShuTiao ) * addscale + ( 112 / 2 ) * kuangoffset;
		moveHeight = WidgetCalender.getIntance().getHeight()/2 + Parameter.Origin_To_Down_Height*WidgetCalender.scale+Parameter.Calender_Workspace_Bottom*WidgetCalender.scale;
		OrigonToBigXianKuangWidth = WidgetCalender.getIntance().getWidth()/2 + Parameter.Calender_BianKuang_Width*addscale;
		OrigonToBigXianKuangHeight = WidgetCalender.getIntance().getHeight()/2 + Parameter.Calender_BianKuang_Height*addscale;
		OrigonToSmallXianKuangWidth =WidgetCalender.getIntance().getWidth()/2 + Parameter.Calender_Little_BianKuang_Width*addscale;
		
		
		this.width=Parameter.BigBackgroundWidth * addscale ;
		this.height= Parameter.BigBackgroundHeight * addHeight_scale;
		this.setPosition( (WidgetCalender.getIntance().getWidth()-this.width)/2 , OrigonToBigXianKuangHeight - BigMoveOff-this.height/2 );
//		this.setBackgroud( Workspace3D.reflectView);
		
		DayGlasses dayglasses = new DayGlasses( maincontext , "dayglasses" , WidgetCalender.getRegion( "background.jpg" ) , "mod_add_glass.obj" );
		dayglasses.build();
		dayglasses.move( OrigonToBigXianKuangWidth , OrigonToBigXianKuangHeight - BigMoveOff , 0f );
		this.addView( dayglasses );
		//		maincontext.mGdxApplication.postRunnable( new Runnable() {
		//			
		//			@Override
		//			public void run()
		//			{
		//				init( year , month , day );
		//			}
		//		} );
	}
	
	public void onpositionchangeforAdd(){
		
		addscale = WidgetCalender.scale;
		addHeight_scale = WidgetCalender.height_scale;
		nowHeight = Parameter.Calender_Day_Glass_Height * addHeight_scale * 5;
		nowWidth = Parameter.Calender_Day_Glass_Width * addscale * 7;
		
		kuangoffset = ( nowHeight - 142f ) / ( 112 * 4f );
		
//		moveHeight = ( Parameter.Calender_Workspace_Bottom + Parameter.Calender_Bottom_JinShuTiao ) * addscale + ( 112 / 2 ) * kuangoffset;
		moveHeight = WidgetCalender.getIntance().getHeight()/2 + Parameter.Origin_To_Down_Height*WidgetCalender.scale+Parameter.Calender_Workspace_Bottom*WidgetCalender.scale;
		BigMoveOff = ( WidgetCalender.scale - WidgetCalender.height_scale ) / 2 * Parameter.BigBackgroundHeight;
		
		OrigonToBigXianKuangWidth = WidgetCalender.getIntance().getWidth()/2 + Parameter.Calender_BianKuang_Width*addscale;
		OrigonToBigXianKuangHeight = WidgetCalender.getIntance().getHeight()/2 + Parameter.Calender_BianKuang_Height*addscale;
		OrigonToSmallXianKuangWidth =WidgetCalender.getIntance().getWidth()/2 + Parameter.Calender_Little_BianKuang_Width*addscale;
		
		this.width=Parameter.BigBackgroundWidth * addscale ;
		this.height= Parameter.BigBackgroundHeight * addHeight_scale;
		this.setPosition( (WidgetCalender.getIntance().getWidth()-this.width)/2 , OrigonToBigXianKuangHeight - BigMoveOff-this.height/2 );
		
		DayGlasses dayglasses =(DayGlasses)this.findView( "dayglasses" ); 
		dayglasses.build();
		dayglasses.move( OrigonToBigXianKuangWidth , OrigonToBigXianKuangHeight - BigMoveOff , 0f );
	}
	
	private ListViewMove3D lv;
	private EventGroup addEvent;
	public TextureRegion getTextureRegion(
			MainAppContext appContext ,
			String title ,
			String time )
	{
		Bitmap backImage = null;
		float width = Parameter.BianKuang_Width * addscale;
		float height;
		float Titletextsize;
		float Timetextsize;
		if( Utils3D.getScreenWidth() < 500 )
		{
			height = Parameter.BianKuang_Height * addscale;
			Titletextsize = 35f * addscale;
			Timetextsize = 23f * addscale;
		}
		else
		{
			height = Parameter.BianKuang_Height * kuangoffset;
			Titletextsize = 35f * kuangoffset;
			Timetextsize = 20f * kuangoffset;
		}
		if(Titletextsize<10f||Timetextsize<10f){
			Titletextsize = 15f;
			Timetextsize = 10f;
		}
		backImage = Bitmap.createBitmap( (int)width , (int)height , Config.ARGB_8888 );
		Canvas canvas = new Canvas( backImage );
		canvas.drawColor( Color.TRANSPARENT );// .TRANSPARENT .TRANSPARENT
		Paint paint = new Paint();
		paint.setAntiAlias( true );// 防锯齿
		paint.setDither( true );// 防抖动
		paint.setARGB( 255 , 99 , 99 , 99 );
		paint.setSubpixelText( true );
		//		paint.setStrokeJoin( Paint.Join.ROUND );
		//		paint.setStrokeCap( Paint.Cap.ROUND );
		paint.setTextSize( Titletextsize );
		//		paint.setShadowLayer( (float)( 4 / 1.7 ) , (float)( 2 / 1.7 ) , (float)( 2 / 1.7 ) , Color.BLACK );// Color.BLACK
		FontMetrics fontMetrics = paint.getFontMetrics();
		float lineHeight = (float)Math.ceil( fontMetrics.descent - fontMetrics.ascent );
		float posY = backImage.getHeight() - ( backImage.getHeight() - lineHeight ) / 2 - fontMetrics.bottom;
		Bitmap bt = null;
		Bitmap newbt = null;
		try
		{
			bt = BitmapFactory.decodeStream( maincontext.mWidgetContext.getAssets().open( "theme/widget/cometcalendar/comet/image/" + "changtiao.png" ) );
			newbt = CalenderHelper.resizeBitmap( bt , (int)width , (int)height );
			canvas.drawBitmap( newbt , 0f , 0f , paint );
		}
		catch( IOException e )
		{
			e.printStackTrace();
		}
		if( paint.measureText( title ) > width - 15 * addscale )
		{
			while( paint.measureText( title ) > width - paint.measureText( "..." ) - 2 * addscale - 26 * addscale )
			{
				title = title.substring( 0 , title.length() - 1 );
			}
			title += "...";
		}
		canvas.drawText( title , 26 * addscale , posY - 10 * kuangoffset , paint );
		paint.setTextSize( Timetextsize );
		if( Utils3D.getScreenWidth() < 500 )
		{
			canvas.drawText( time , 590 * addscale - paint.measureText( time ) - 12 * addscale , posY + 25 * addscale , paint );
		}
		else
		{
			canvas.drawText( time , 590 * addscale - paint.measureText( time ) - 12 * addscale , posY + 25 * kuangoffset , paint );
		}
		TextureRegion newTextureRegion = new TextureRegion( new BitmapTexture( backImage ) );
		if( backImage != null )
		{
			backImage.recycle();
		}
		if( bt != null )
		{
			bt.recycle();
		}
		if( newbt != null )
		{
			newbt.recycle();
		}
		return newTextureRegion;
	}
	
	@Override
	public boolean scroll(
			float x ,
			float y ,
			float deltaX ,
			float deltaY )
	{
		float k = 0;
		if( deltaY != 0 && deltaY != 0 )
		{
			k = deltaY / deltaX;
		}
		if( k > 1.7 || k < -1.7 )
		{
			return true;
		}
		return super.scroll( x , y , deltaX , deltaY );
	}
	
	public void removeEvents(){
		if( lv != null )
		{
			lv.removeAllViews();
			lv.dispose();
			this.removeView( lv );
			lv = null;
		}
		if( addEvent != null )
		{
			this.removeView( addEvent );
			addEvent=null;
		}
	}
	
	public void updateNowEvents(
			int newyear ,
			int newmonth ,
			int newday )
	{
		AddEventGroup.year = newyear;
		AddEventGroup.month = newmonth;
		AddEventGroup.day = newday;
		String nowstartTime = CalenderHelper.DateToLong( newyear , newmonth , newday , 0 , 0 , 0 ) + "";
		String nowendTime = CalenderHelper.DateToLong( newyear , newmonth , newday , 23 , 59 , 59 ) + "";
		long a = Long.parseLong( nowstartTime );
		long b = Long.parseLong( nowendTime );
		List<CalendarAllEvents> allEvents = CalenderHelper.MergeNewList( maincontext.mContainerContext.getContentResolver() , a , b );
		if( allEvents.size() > 0 )
		{
			List<CalendarAllEvents> newneedEvents = new ArrayList<CalendarAllEvents>();
			for( int i = 0 ; i < allEvents.size() ; i++ )
			{
				String startTime = allEvents.get( i ).getBegin();
				long x = Long.parseLong( startTime );
				String endTime = allEvents.get( i ).getEnd();
				long y = Long.parseLong( endTime );
				if( allEvents.get( i ).getAll_day().equals( "1" ) )
				{
					if( x >= a && x <= b )
					{
						newneedEvents.add( allEvents.get( i ) );
					}
				}
				else
				{
					if( ( x >= a && x <= b && y >= a && y <= b ) || ( x <= a && y >= b ) || ( x <= a && y >= a && y <= b ) || ( x >= a && x <= b && y >= b ) )
					{
						newneedEvents.add( allEvents.get( i ) );
					}
				}
			}
			if( newneedEvents.size() != 0 )
			{
				if( addEvent != null )
				{
					this.removeView( addEvent );
					addEvent=null;
				}
				if( lv != null )
				{
					lv.removeAllViews();
					lv.dispose();
					lv=null;
					lv = new ListViewMove3D( "lv" );
					lv.setSize( Parameter.BigBackgroundWidth * addscale , Parameter.BigBackgroundHeight * addHeight_scale );
					lv.setPosition( 0 , 0 );
					lv.paddingBottom = 30f * addHeight_scale;
				}
				else
				{
					lv = new ListViewMove3D( "lv" );
					lv.setSize( Parameter.BigBackgroundWidth * addscale , Parameter.BigBackgroundHeight * addHeight_scale );
					lv.setPosition( 0 , 0 );
					lv.paddingBottom = 30f * addHeight_scale;
				}
				for( int i = 0 ; i < newneedEvents.size() ; i++ )
				{
					String message = null;
					if( newneedEvents.get( i ).getAll_day().equals( "1" ) )
					{
						message = CalenderHelper.LongToDatePart( Long.parseLong( newneedEvents.get( i ).getBegin() ) );
					}
					else
					{
						message = CalenderHelper.LongToDate( Long.parseLong( newneedEvents.get( i ).getBegin() ) ) + "-" + CalenderHelper
								.LongToDate( Long.parseLong( newneedEvents.get( i ).getEnd() ) );
					}
					EventInfoGroup dg = new EventInfoGroup( "eventinfo" , maincontext , context , newneedEvents.get( i ) );
					if( Utils3D.getScreenWidth() < 500 )
					{
						dg.setSize( Parameter.BianKuang_Width * addscale , Parameter.BianKuang_Height * addscale + 30 * addHeight_scale );
					}
					else
					{
						dg.setSize( Parameter.BianKuang_Width * addscale , Parameter.BianKuang_Height * kuangoffset + 30 * addHeight_scale );
					}
					EventGlasses string = new EventGlasses( maincontext , "string" , getTextureRegion( maincontext , newneedEvents.get( i ).getTitle() , message ) , "mod_add01.obj" );
					string.build();
					string.move( nowWidth/2 , dg.height/2 , 0f );
					dg.addView( string );
					lv.addItem( dg );
				}
				EventGroup addEvent = new EventGroup( "addEvent" , maincontext , context );
				addEvent.setSize( Parameter.BianKuang_Width * addscale , Parameter.JiaHao_Height * addHeight_scale + 30 * addHeight_scale );
				AddGlasses jiahao = new AddGlasses( maincontext , "jiahao" , WidgetCalender.texjiahao_region , "mod_add.obj" );
				jiahao.build();
				jiahao.move( nowWidth/2, addEvent.height/2 , 0f );
				addEvent.addView( jiahao );
				lv.addItem( addEvent );
				this.addView( lv );
			}
			else if( newneedEvents.size() == 0 )
			{
				if( addEvent != null )
				{
					this.removeView( addEvent );
					addEvent=null;
				}
				if( lv != null )
				{
					lv.removeAllViews();
					lv.dispose();
					this.removeView( lv );
					lv = null;
				}
//				init( newyear , newmonth , newday );
				addEvent = new EventGroup( "addEvent" , maincontext , context );
				addEvent.setSize( ( Parameter.JiaHao_Width + 30 ) * addHeight_scale , ( Parameter.JiaHao_Height + 30 ) * addHeight_scale );
				addEvent.setPosition(this.width/2-addEvent.width/2  , this.height/2-addEvent.height/2);
				AddGlasses jiahao = new AddGlasses( maincontext , "jiahao" , WidgetCalender.texjiahao_region , "mod_add.obj" );
				jiahao.build();
				jiahao.move( addEvent.width/2 , addEvent.height/2 , 0f );
				addEvent.addView( jiahao );
				
				this.addView( addEvent );
			}
		}
		else
		{
			if( lv != null )
			{
				lv.removeAllViews();
				lv.dispose();
				this.removeView( lv );
				lv = null;
			}
			if( addEvent != null )
			{
				this.removeView( addEvent );
				addEvent=null;
			}
//			init( newyear , newmonth , newday );
			addEvent = new EventGroup( "addEvent" , maincontext , context );
			addEvent.setSize( ( Parameter.JiaHao_Width + 30 ) * addHeight_scale , ( Parameter.JiaHao_Height + 30 ) * addHeight_scale );
			addEvent.setPosition(this.width/2-addEvent.width/2  , this.height/2-addEvent.height/2);
			AddGlasses jiahao = new AddGlasses( maincontext , "jiahao" , WidgetCalender.texjiahao_region , "mod_add.obj" );
			jiahao.build();
			jiahao.move( addEvent.width/2 , addEvent.height/2 , 0f );
			addEvent.addView( jiahao );
			
			this.addView( addEvent );
		}
	}
	
	@Override
	public boolean pointerInParent(
			float x ,
			float y )
	{
		// TODO Auto-generated method stub
		return super.pointerInParent( x , y );
	}
}
