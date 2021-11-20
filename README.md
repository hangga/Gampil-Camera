# Gampil-Camera
Embed camera preview with API Camera 2 with surfaceview, just paste it in the layout.

<img width="300" src="https://github.com/hangga/Gampil-Camera/blob/main/skrinsut.png?raw=true"/>

## Setup

**Gradle**
```
allprojects {
    repositories {
    	...
	maven { url 'https://jitpack.io' }
    }
}
```

```
dependencies {
    implementation 'com.github.hangga:Gampil-Camera:v1.1.1'
}


```
## How to use
- Add GampilPreview to your activity layout. 
```
<id.hangga.gampil.GampilPreview
    android:id="@+id/gampilPreview"
    android:layout_width="match_parent"
    android:layout_height="match_parent"/>
```
- Initialization in java
```
GampilPreview gampilPreview = findViewById(R.id.gampilPreview);
```
- Take Picture
```
gampilPreview.takePicture(new GampilPreview.OnTakePicture() {
    @Override
    public void onPictureTaken(File file, Bitmap bitmap) {
        // Your code
    }
});
```
