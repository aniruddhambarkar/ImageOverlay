# ImageOverlay
This library enables facility to add information overlays on images. One can add points/overlays on image and store information into it.Class PointView can contain information/message for particular point 
Features covered are
1. Includes setting overlay points on ImageView
2. Adding new and updating existing overlays

<b>Usage</b>

Including library 
Add it in your root build.gradle at the end of repositories:
	
	allprojects {
		repositories {
			maven { url 'https://jitpack.io' }
		}
	}
	

2. Add the dependency


		dependencies {
	        	implementation 'com.github.aniruddhambarkar:ImageOverlay:0.1.0'
		}


Example
1.  Include View in your XML

		<com.aniruddhambarkar.ImageOverlay
        		android:id="@+id/imageOverlay"
        		android:layout_width="match_parent"
        		android:layout_height="match_parent"
        		android:layout_above="@+id/btnSave"
        		android:background="@mipmap/ic_launcher_round"
        		app:point_size="45dp" />
			
        
 Supported attributes
 
		 point_drawable: To customize drawable to show for points over image
		 point_size : dimension of default point
		 
 
 To access the view into your activity/fragment
 
 
	 ImageOverlay imageOverlay;
	 imageOverlay = findViewById(R.id.imageOverlay);

 <b>Setting Points on View</b>

	  ArrayList<PointView> pointViews = new ArrayList<>();
		PointView pointView = new PointView();
		pointView.setX(100);
		pointView.setX(110);
		pointViews.add(pointView);
	 //sets points on the image
	 imageOverlay.setPointViews(pointViews);

	 <b>Getting Points from View</b>

	 ArrayList<PointView> pointViews = new ArrayList<>();
	 pointViews = imageOverlay.getPointViews();

<b>Clearing all Points from View</b>
	 imageOverlay.clearOverlays();
 
 Specific view can be deleted by clicking/accessing it and removing it from view
 
<b> Point Listeners</b>
 Supports listener for added new point and points click
 
 Class name : com.aniruddhambarkar.PointListener
 
		 MainActivity extends AppCompatActivity implements PointListener 
		 imageOverlay.setPointListener(this);

		 Methods
		  @Override
		    public void OnPointAdded(PointView pointView) {
				//callback to listen if point added.
		    }

		    @Override
		    public void OnPointClicked(PointView pointView) {
				// Click listner for point/overlay click
		    }
 
